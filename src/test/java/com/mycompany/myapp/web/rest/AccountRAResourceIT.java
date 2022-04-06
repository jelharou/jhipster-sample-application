package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AccountRA;
import com.mycompany.myapp.repository.AccountRARepository;
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
 * Integration tests for the {@link AccountRAResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccountRAResourceIT {

    private static final String ENTITY_API_URL = "/api/account-ras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AccountRARepository accountRARepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountRAMockMvc;

    private AccountRA accountRA;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountRA createEntity(EntityManager em) {
        AccountRA accountRA = new AccountRA();
        return accountRA;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountRA createUpdatedEntity(EntityManager em) {
        AccountRA accountRA = new AccountRA();
        return accountRA;
    }

    @BeforeEach
    public void initTest() {
        accountRA = createEntity(em);
    }

    @Test
    @Transactional
    void createAccountRA() throws Exception {
        int databaseSizeBeforeCreate = accountRARepository.findAll().size();
        // Create the AccountRA
        restAccountRAMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountRA)))
            .andExpect(status().isCreated());

        // Validate the AccountRA in the database
        List<AccountRA> accountRAList = accountRARepository.findAll();
        assertThat(accountRAList).hasSize(databaseSizeBeforeCreate + 1);
        AccountRA testAccountRA = accountRAList.get(accountRAList.size() - 1);
    }

    @Test
    @Transactional
    void createAccountRAWithExistingId() throws Exception {
        // Create the AccountRA with an existing ID
        accountRA.setId(1L);

        int databaseSizeBeforeCreate = accountRARepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountRAMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountRA)))
            .andExpect(status().isBadRequest());

        // Validate the AccountRA in the database
        List<AccountRA> accountRAList = accountRARepository.findAll();
        assertThat(accountRAList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAccountRAS() throws Exception {
        // Initialize the database
        accountRARepository.saveAndFlush(accountRA);

        // Get all the accountRAList
        restAccountRAMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountRA.getId().intValue())));
    }

    @Test
    @Transactional
    void getAccountRA() throws Exception {
        // Initialize the database
        accountRARepository.saveAndFlush(accountRA);

        // Get the accountRA
        restAccountRAMockMvc
            .perform(get(ENTITY_API_URL_ID, accountRA.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountRA.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingAccountRA() throws Exception {
        // Get the accountRA
        restAccountRAMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAccountRA() throws Exception {
        // Initialize the database
        accountRARepository.saveAndFlush(accountRA);

        int databaseSizeBeforeUpdate = accountRARepository.findAll().size();

        // Update the accountRA
        AccountRA updatedAccountRA = accountRARepository.findById(accountRA.getId()).get();
        // Disconnect from session so that the updates on updatedAccountRA are not directly saved in db
        em.detach(updatedAccountRA);

        restAccountRAMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAccountRA.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAccountRA))
            )
            .andExpect(status().isOk());

        // Validate the AccountRA in the database
        List<AccountRA> accountRAList = accountRARepository.findAll();
        assertThat(accountRAList).hasSize(databaseSizeBeforeUpdate);
        AccountRA testAccountRA = accountRAList.get(accountRAList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingAccountRA() throws Exception {
        int databaseSizeBeforeUpdate = accountRARepository.findAll().size();
        accountRA.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountRAMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountRA.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountRA))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountRA in the database
        List<AccountRA> accountRAList = accountRARepository.findAll();
        assertThat(accountRAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccountRA() throws Exception {
        int databaseSizeBeforeUpdate = accountRARepository.findAll().size();
        accountRA.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountRAMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountRA))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountRA in the database
        List<AccountRA> accountRAList = accountRARepository.findAll();
        assertThat(accountRAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccountRA() throws Exception {
        int databaseSizeBeforeUpdate = accountRARepository.findAll().size();
        accountRA.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountRAMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountRA)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountRA in the database
        List<AccountRA> accountRAList = accountRARepository.findAll();
        assertThat(accountRAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAccountRAWithPatch() throws Exception {
        // Initialize the database
        accountRARepository.saveAndFlush(accountRA);

        int databaseSizeBeforeUpdate = accountRARepository.findAll().size();

        // Update the accountRA using partial update
        AccountRA partialUpdatedAccountRA = new AccountRA();
        partialUpdatedAccountRA.setId(accountRA.getId());

        restAccountRAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountRA.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountRA))
            )
            .andExpect(status().isOk());

        // Validate the AccountRA in the database
        List<AccountRA> accountRAList = accountRARepository.findAll();
        assertThat(accountRAList).hasSize(databaseSizeBeforeUpdate);
        AccountRA testAccountRA = accountRAList.get(accountRAList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateAccountRAWithPatch() throws Exception {
        // Initialize the database
        accountRARepository.saveAndFlush(accountRA);

        int databaseSizeBeforeUpdate = accountRARepository.findAll().size();

        // Update the accountRA using partial update
        AccountRA partialUpdatedAccountRA = new AccountRA();
        partialUpdatedAccountRA.setId(accountRA.getId());

        restAccountRAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountRA.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountRA))
            )
            .andExpect(status().isOk());

        // Validate the AccountRA in the database
        List<AccountRA> accountRAList = accountRARepository.findAll();
        assertThat(accountRAList).hasSize(databaseSizeBeforeUpdate);
        AccountRA testAccountRA = accountRAList.get(accountRAList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingAccountRA() throws Exception {
        int databaseSizeBeforeUpdate = accountRARepository.findAll().size();
        accountRA.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountRAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accountRA.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountRA))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountRA in the database
        List<AccountRA> accountRAList = accountRARepository.findAll();
        assertThat(accountRAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccountRA() throws Exception {
        int databaseSizeBeforeUpdate = accountRARepository.findAll().size();
        accountRA.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountRAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountRA))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountRA in the database
        List<AccountRA> accountRAList = accountRARepository.findAll();
        assertThat(accountRAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccountRA() throws Exception {
        int databaseSizeBeforeUpdate = accountRARepository.findAll().size();
        accountRA.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountRAMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(accountRA))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountRA in the database
        List<AccountRA> accountRAList = accountRARepository.findAll();
        assertThat(accountRAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAccountRA() throws Exception {
        // Initialize the database
        accountRARepository.saveAndFlush(accountRA);

        int databaseSizeBeforeDelete = accountRARepository.findAll().size();

        // Delete the accountRA
        restAccountRAMockMvc
            .perform(delete(ENTITY_API_URL_ID, accountRA.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AccountRA> accountRAList = accountRARepository.findAll();
        assertThat(accountRAList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
