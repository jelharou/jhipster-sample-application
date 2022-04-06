package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PartyId;
import com.mycompany.myapp.repository.PartyIdRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.PartyId}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PartyIdResource {

    private final Logger log = LoggerFactory.getLogger(PartyIdResource.class);

    private static final String ENTITY_NAME = "partyId";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PartyIdRepository partyIdRepository;

    public PartyIdResource(PartyIdRepository partyIdRepository) {
        this.partyIdRepository = partyIdRepository;
    }

    /**
     * {@code POST  /party-ids} : Create a new partyId.
     *
     * @param partyId the partyId to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partyId, or with status {@code 400 (Bad Request)} if the partyId has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/party-ids")
    public ResponseEntity<PartyId> createPartyId(@RequestBody PartyId partyId) throws URISyntaxException {
        log.debug("REST request to save PartyId : {}", partyId);
        if (partyId.getId() != null) {
            throw new BadRequestAlertException("A new partyId cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PartyId result = partyIdRepository.save(partyId);
        return ResponseEntity
            .created(new URI("/api/party-ids/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /party-ids/:id} : Updates an existing partyId.
     *
     * @param id the id of the partyId to save.
     * @param partyId the partyId to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyId,
     * or with status {@code 400 (Bad Request)} if the partyId is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partyId couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/party-ids/{id}")
    public ResponseEntity<PartyId> updatePartyId(@PathVariable(value = "id", required = false) final Long id, @RequestBody PartyId partyId)
        throws URISyntaxException {
        log.debug("REST request to update PartyId : {}, {}", id, partyId);
        if (partyId.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyId.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partyIdRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PartyId result = partyIdRepository.save(partyId);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, partyId.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /party-ids/:id} : Partial updates given fields of an existing partyId, field will ignore if it is null
     *
     * @param id the id of the partyId to save.
     * @param partyId the partyId to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyId,
     * or with status {@code 400 (Bad Request)} if the partyId is not valid,
     * or with status {@code 404 (Not Found)} if the partyId is not found,
     * or with status {@code 500 (Internal Server Error)} if the partyId couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/party-ids/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PartyId> partialUpdatePartyId(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PartyId partyId
    ) throws URISyntaxException {
        log.debug("REST request to partial update PartyId partially : {}, {}", id, partyId);
        if (partyId.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyId.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partyIdRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PartyId> result = partyIdRepository
            .findById(partyId.getId())
            .map(existingPartyId -> {
                return existingPartyId;
            })
            .map(partyIdRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, partyId.getId().toString())
        );
    }

    /**
     * {@code GET  /party-ids} : get all the partyIds.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of partyIds in body.
     */
    @GetMapping("/party-ids")
    public List<PartyId> getAllPartyIds() {
        log.debug("REST request to get all PartyIds");
        return partyIdRepository.findAll();
    }

    /**
     * {@code GET  /party-ids/:id} : get the "id" partyId.
     *
     * @param id the id of the partyId to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partyId, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/party-ids/{id}")
    public ResponseEntity<PartyId> getPartyId(@PathVariable Long id) {
        log.debug("REST request to get PartyId : {}", id);
        Optional<PartyId> partyId = partyIdRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(partyId);
    }

    /**
     * {@code DELETE  /party-ids/:id} : delete the "id" partyId.
     *
     * @param id the id of the partyId to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/party-ids/{id}")
    public ResponseEntity<Void> deletePartyId(@PathVariable Long id) {
        log.debug("REST request to delete PartyId : {}", id);
        partyIdRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
