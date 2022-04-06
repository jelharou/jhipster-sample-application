package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PartyId;
import com.mycompany.myapp.repository.PartyIdRepository;
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
 * Integration tests for the {@link PartyIdResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PartyIdResourceIT {

    private static final String ENTITY_API_URL = "/api/party-ids";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PartyIdRepository partyIdRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartyIdMockMvc;

    private PartyId partyId;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartyId createEntity(EntityManager em) {
        PartyId partyId = new PartyId();
        return partyId;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartyId createUpdatedEntity(EntityManager em) {
        PartyId partyId = new PartyId();
        return partyId;
    }

    @BeforeEach
    public void initTest() {
        partyId = createEntity(em);
    }

    @Test
    @Transactional
    void createPartyId() throws Exception {
        int databaseSizeBeforeCreate = partyIdRepository.findAll().size();
        // Create the PartyId
        restPartyIdMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partyId)))
            .andExpect(status().isCreated());

        // Validate the PartyId in the database
        List<PartyId> partyIdList = partyIdRepository.findAll();
        assertThat(partyIdList).hasSize(databaseSizeBeforeCreate + 1);
        PartyId testPartyId = partyIdList.get(partyIdList.size() - 1);
    }

    @Test
    @Transactional
    void createPartyIdWithExistingId() throws Exception {
        // Create the PartyId with an existing ID
        partyId.setId(1L);

        int databaseSizeBeforeCreate = partyIdRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartyIdMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partyId)))
            .andExpect(status().isBadRequest());

        // Validate the PartyId in the database
        List<PartyId> partyIdList = partyIdRepository.findAll();
        assertThat(partyIdList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPartyIds() throws Exception {
        // Initialize the database
        partyIdRepository.saveAndFlush(partyId);

        // Get all the partyIdList
        restPartyIdMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partyId.getId().intValue())));
    }

    @Test
    @Transactional
    void getPartyId() throws Exception {
        // Initialize the database
        partyIdRepository.saveAndFlush(partyId);

        // Get the partyId
        restPartyIdMockMvc
            .perform(get(ENTITY_API_URL_ID, partyId.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partyId.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPartyId() throws Exception {
        // Get the partyId
        restPartyIdMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPartyId() throws Exception {
        // Initialize the database
        partyIdRepository.saveAndFlush(partyId);

        int databaseSizeBeforeUpdate = partyIdRepository.findAll().size();

        // Update the partyId
        PartyId updatedPartyId = partyIdRepository.findById(partyId.getId()).get();
        // Disconnect from session so that the updates on updatedPartyId are not directly saved in db
        em.detach(updatedPartyId);

        restPartyIdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPartyId.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPartyId))
            )
            .andExpect(status().isOk());

        // Validate the PartyId in the database
        List<PartyId> partyIdList = partyIdRepository.findAll();
        assertThat(partyIdList).hasSize(databaseSizeBeforeUpdate);
        PartyId testPartyId = partyIdList.get(partyIdList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingPartyId() throws Exception {
        int databaseSizeBeforeUpdate = partyIdRepository.findAll().size();
        partyId.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyIdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partyId.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partyId))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyId in the database
        List<PartyId> partyIdList = partyIdRepository.findAll();
        assertThat(partyIdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPartyId() throws Exception {
        int databaseSizeBeforeUpdate = partyIdRepository.findAll().size();
        partyId.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyIdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partyId))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyId in the database
        List<PartyId> partyIdList = partyIdRepository.findAll();
        assertThat(partyIdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPartyId() throws Exception {
        int databaseSizeBeforeUpdate = partyIdRepository.findAll().size();
        partyId.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyIdMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partyId)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartyId in the database
        List<PartyId> partyIdList = partyIdRepository.findAll();
        assertThat(partyIdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartyIdWithPatch() throws Exception {
        // Initialize the database
        partyIdRepository.saveAndFlush(partyId);

        int databaseSizeBeforeUpdate = partyIdRepository.findAll().size();

        // Update the partyId using partial update
        PartyId partialUpdatedPartyId = new PartyId();
        partialUpdatedPartyId.setId(partyId.getId());

        restPartyIdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartyId.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPartyId))
            )
            .andExpect(status().isOk());

        // Validate the PartyId in the database
        List<PartyId> partyIdList = partyIdRepository.findAll();
        assertThat(partyIdList).hasSize(databaseSizeBeforeUpdate);
        PartyId testPartyId = partyIdList.get(partyIdList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdatePartyIdWithPatch() throws Exception {
        // Initialize the database
        partyIdRepository.saveAndFlush(partyId);

        int databaseSizeBeforeUpdate = partyIdRepository.findAll().size();

        // Update the partyId using partial update
        PartyId partialUpdatedPartyId = new PartyId();
        partialUpdatedPartyId.setId(partyId.getId());

        restPartyIdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartyId.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPartyId))
            )
            .andExpect(status().isOk());

        // Validate the PartyId in the database
        List<PartyId> partyIdList = partyIdRepository.findAll();
        assertThat(partyIdList).hasSize(databaseSizeBeforeUpdate);
        PartyId testPartyId = partyIdList.get(partyIdList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingPartyId() throws Exception {
        int databaseSizeBeforeUpdate = partyIdRepository.findAll().size();
        partyId.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyIdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partyId.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partyId))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyId in the database
        List<PartyId> partyIdList = partyIdRepository.findAll();
        assertThat(partyIdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPartyId() throws Exception {
        int databaseSizeBeforeUpdate = partyIdRepository.findAll().size();
        partyId.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyIdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partyId))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyId in the database
        List<PartyId> partyIdList = partyIdRepository.findAll();
        assertThat(partyIdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPartyId() throws Exception {
        int databaseSizeBeforeUpdate = partyIdRepository.findAll().size();
        partyId.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyIdMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(partyId)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartyId in the database
        List<PartyId> partyIdList = partyIdRepository.findAll();
        assertThat(partyIdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePartyId() throws Exception {
        // Initialize the database
        partyIdRepository.saveAndFlush(partyId);

        int databaseSizeBeforeDelete = partyIdRepository.findAll().size();

        // Delete the partyId
        restPartyIdMockMvc
            .perform(delete(ENTITY_API_URL_ID, partyId.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PartyId> partyIdList = partyIdRepository.findAll();
        assertThat(partyIdList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
