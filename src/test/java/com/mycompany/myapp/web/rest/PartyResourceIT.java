package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Party;
import com.mycompany.myapp.repository.PartyRepository;
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
 * Integration tests for the {@link PartyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PartyResourceIT {

    private static final String ENTITY_API_URL = "/api/parties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartyMockMvc;

    private Party party;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Party createEntity(EntityManager em) {
        Party party = new Party();
        return party;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Party createUpdatedEntity(EntityManager em) {
        Party party = new Party();
        return party;
    }

    @BeforeEach
    public void initTest() {
        party = createEntity(em);
    }

    @Test
    @Transactional
    void createParty() throws Exception {
        int databaseSizeBeforeCreate = partyRepository.findAll().size();
        // Create the Party
        restPartyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(party)))
            .andExpect(status().isCreated());

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeCreate + 1);
        Party testParty = partyList.get(partyList.size() - 1);
    }

    @Test
    @Transactional
    void createPartyWithExistingId() throws Exception {
        // Create the Party with an existing ID
        party.setId(1L);

        int databaseSizeBeforeCreate = partyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(party)))
            .andExpect(status().isBadRequest());

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllParties() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(party);

        // Get all the partyList
        restPartyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(party.getId().intValue())));
    }

    @Test
    @Transactional
    void getParty() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(party);

        // Get the party
        restPartyMockMvc
            .perform(get(ENTITY_API_URL_ID, party.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(party.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingParty() throws Exception {
        // Get the party
        restPartyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewParty() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(party);

        int databaseSizeBeforeUpdate = partyRepository.findAll().size();

        // Update the party
        Party updatedParty = partyRepository.findById(party.getId()).get();
        // Disconnect from session so that the updates on updatedParty are not directly saved in db
        em.detach(updatedParty);

        restPartyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedParty.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedParty))
            )
            .andExpect(status().isOk());

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
        Party testParty = partyList.get(partyList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingParty() throws Exception {
        int databaseSizeBeforeUpdate = partyRepository.findAll().size();
        party.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, party.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(party))
            )
            .andExpect(status().isBadRequest());

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParty() throws Exception {
        int databaseSizeBeforeUpdate = partyRepository.findAll().size();
        party.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(party))
            )
            .andExpect(status().isBadRequest());

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParty() throws Exception {
        int databaseSizeBeforeUpdate = partyRepository.findAll().size();
        party.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(party)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartyWithPatch() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(party);

        int databaseSizeBeforeUpdate = partyRepository.findAll().size();

        // Update the party using partial update
        Party partialUpdatedParty = new Party();
        partialUpdatedParty.setId(party.getId());

        restPartyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParty))
            )
            .andExpect(status().isOk());

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
        Party testParty = partyList.get(partyList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdatePartyWithPatch() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(party);

        int databaseSizeBeforeUpdate = partyRepository.findAll().size();

        // Update the party using partial update
        Party partialUpdatedParty = new Party();
        partialUpdatedParty.setId(party.getId());

        restPartyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParty))
            )
            .andExpect(status().isOk());

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
        Party testParty = partyList.get(partyList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingParty() throws Exception {
        int databaseSizeBeforeUpdate = partyRepository.findAll().size();
        party.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, party.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(party))
            )
            .andExpect(status().isBadRequest());

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParty() throws Exception {
        int databaseSizeBeforeUpdate = partyRepository.findAll().size();
        party.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(party))
            )
            .andExpect(status().isBadRequest());

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParty() throws Exception {
        int databaseSizeBeforeUpdate = partyRepository.findAll().size();
        party.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(party)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParty() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(party);

        int databaseSizeBeforeDelete = partyRepository.findAll().size();

        // Delete the party
        restPartyMockMvc
            .perform(delete(ENTITY_API_URL_ID, party.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Party> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
