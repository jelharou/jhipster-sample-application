package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AccountRef;
import com.mycompany.myapp.repository.AccountRefRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AccountRefResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccountRefResourceIT {

    private static final String ENTITY_API_URL = "/api/account-refs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AccountRefRepository accountRefRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountRefMockMvc;

    private AccountRef accountRef;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountRef createEntity(EntityManager em) {
        AccountRef accountRef = new AccountRef();
        return accountRef;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountRef createUpdatedEntity(EntityManager em) {
        AccountRef accountRef = new AccountRef();
        return accountRef;
    }

    @BeforeEach
    public void initTest() {
        accountRef = createEntity(em);
    }

    @Test
    @Transactional
    void createAccountRef() throws Exception {
        int databaseSizeBeforeCreate = accountRefRepository.findAll().size();
        // Create the AccountRef
        restAccountRefMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountRef)))
            .andExpect(status().isCreated());

        // Validate the AccountRef in the database
        List<AccountRef> accountRefList = accountRefRepository.findAll();
        assertThat(accountRefList).hasSize(databaseSizeBeforeCreate + 1);
        AccountRef testAccountRef = accountRefList.get(accountRefList.size() - 1);
    }

    @Test
    @Transactional
    void createAccountRefWithExistingId() throws Exception {
        // Create the AccountRef with an existing ID
        accountRef.setId(1L);

        int databaseSizeBeforeCreate = accountRefRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountRefMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountRef)))
            .andExpect(status().isBadRequest());

        // Validate the AccountRef in the database
        List<AccountRef> accountRefList = accountRefRepository.findAll();
        assertThat(accountRefList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAccountRefs() throws Exception {
        // Initialize the database
        accountRefRepository.saveAndFlush(accountRef);

        // Get all the accountRefList
        restAccountRefMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountRef.getId().intValue())));
    }

    @Test
    @Transactional
    void getAccountRef() throws Exception {
        // Initialize the database
        accountRefRepository.saveAndFlush(accountRef);

        // Get the accountRef
        restAccountRefMockMvc
            .perform(get(ENTITY_API_URL_ID, accountRef.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountRef.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingAccountRef() throws Exception {
        // Get the accountRef
        restAccountRefMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAccountRef() throws Exception {
        // Initialize the database
        accountRefRepository.saveAndFlush(accountRef);

        int databaseSizeBeforeUpdate = accountRefRepository.findAll().size();

        // Update the accountRef
        AccountRef updatedAccountRef = accountRefRepository.findById(accountRef.getId()).get();
        // Disconnect from session so that the updates on updatedAccountRef are not directly saved in db
        em.detach(updatedAccountRef);

        restAccountRefMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAccountRef.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAccountRef))
            )
            .andExpect(status().isOk());

        // Validate the AccountRef in the database
        List<AccountRef> accountRefList = accountRefRepository.findAll();
        assertThat(accountRefList).hasSize(databaseSizeBeforeUpdate);
        AccountRef testAccountRef = accountRefList.get(accountRefList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingAccountRef() throws Exception {
        int databaseSizeBeforeUpdate = accountRefRepository.findAll().size();
        accountRef.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountRefMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountRef.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountRef))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountRef in the database
        List<AccountRef> accountRefList = accountRefRepository.findAll();
        assertThat(accountRefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccountRef() throws Exception {
        int databaseSizeBeforeUpdate = accountRefRepository.findAll().size();
        accountRef.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountRefMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountRef))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountRef in the database
        List<AccountRef> accountRefList = accountRefRepository.findAll();
        assertThat(accountRefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccountRef() throws Exception {
        int databaseSizeBeforeUpdate = accountRefRepository.findAll().size();
        accountRef.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountRefMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountRef)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountRef in the database
        List<AccountRef> accountRefList = accountRefRepository.findAll();
        assertThat(accountRefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAccountRefWithPatch() throws Exception {
        // Initialize the database
        accountRefRepository.saveAndFlush(accountRef);

        int databaseSizeBeforeUpdate = accountRefRepository.findAll().size();

        // Update the accountRef using partial update
        AccountRef partialUpdatedAccountRef = new AccountRef();
        partialUpdatedAccountRef.setId(accountRef.getId());

        restAccountRefMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountRef.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountRef))
            )
            .andExpect(status().isOk());

        // Validate the AccountRef in the database
        List<AccountRef> accountRefList = accountRefRepository.findAll();
        assertThat(accountRefList).hasSize(databaseSizeBeforeUpdate);
        AccountRef testAccountRef = accountRefList.get(accountRefList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateAccountRefWithPatch() throws Exception {
        // Initialize the database
        accountRefRepository.saveAndFlush(accountRef);

        int databaseSizeBeforeUpdate = accountRefRepository.findAll().size();

        // Update the accountRef using partial update
        AccountRef partialUpdatedAccountRef = new AccountRef();
        partialUpdatedAccountRef.setId(accountRef.getId());

        restAccountRefMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountRef.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountRef))
            )
            .andExpect(status().isOk());

        // Validate the AccountRef in the database
        List<AccountRef> accountRefList = accountRefRepository.findAll();
        assertThat(accountRefList).hasSize(databaseSizeBeforeUpdate);
        AccountRef testAccountRef = accountRefList.get(accountRefList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingAccountRef() throws Exception {
        int databaseSizeBeforeUpdate = accountRefRepository.findAll().size();
        accountRef.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountRefMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accountRef.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountRef))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountRef in the database
        List<AccountRef> accountRefList = accountRefRepository.findAll();
        assertThat(accountRefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccountRef() throws Exception {
        int databaseSizeBeforeUpdate = accountRefRepository.findAll().size();
        accountRef.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountRefMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountRef))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountRef in the database
        List<AccountRef> accountRefList = accountRefRepository.findAll();
        assertThat(accountRefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccountRef() throws Exception {
        int databaseSizeBeforeUpdate = accountRefRepository.findAll().size();
        accountRef.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountRefMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(accountRef))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountRef in the database
        List<AccountRef> accountRefList = accountRefRepository.findAll();
        assertThat(accountRefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAccountRef() throws Exception {
        // Initialize the database
        accountRefRepository.saveAndFlush(accountRef);

        int databaseSizeBeforeDelete = accountRefRepository.findAll().size();

        // Delete the accountRef
        restAccountRefMockMvc
            .perform(delete(ENTITY_API_URL_ID, accountRef.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AccountRef> accountRefList = accountRefRepository.findAll();
        assertThat(accountRefList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
