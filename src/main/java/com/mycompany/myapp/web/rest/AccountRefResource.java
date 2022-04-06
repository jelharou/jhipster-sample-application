package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AccountRef;
import com.mycompany.myapp.repository.AccountRefRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.AccountRef}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AccountRefResource {

    private final Logger log = LoggerFactory.getLogger(AccountRefResource.class);

    private static final String ENTITY_NAME = "accountRef";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountRefRepository accountRefRepository;

    public AccountRefResource(AccountRefRepository accountRefRepository) {
        this.accountRefRepository = accountRefRepository;
    }

    /**
     * {@code POST  /account-refs} : Create a new accountRef.
     *
     * @param accountRef the accountRef to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountRef, or with status {@code 400 (Bad Request)} if the accountRef has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/account-refs")
    public ResponseEntity<AccountRef> createAccountRef(@RequestBody AccountRef accountRef) throws URISyntaxException {
        log.debug("REST request to save AccountRef : {}", accountRef);
        if (accountRef.getId() != null) {
            throw new BadRequestAlertException("A new accountRef cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountRef result = accountRefRepository.save(accountRef);
        return ResponseEntity
            .created(new URI("/api/account-refs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /account-refs/:id} : Updates an existing accountRef.
     *
     * @param id the id of the accountRef to save.
     * @param accountRef the accountRef to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountRef,
     * or with status {@code 400 (Bad Request)} if the accountRef is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountRef couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/account-refs/{id}")
    public ResponseEntity<AccountRef> updateAccountRef(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccountRef accountRef
    ) throws URISyntaxException {
        log.debug("REST request to update AccountRef : {}, {}", id, accountRef);
        if (accountRef.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountRef.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountRefRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AccountRef result = accountRefRepository.save(accountRef);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, accountRef.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /account-refs/:id} : Partial updates given fields of an existing accountRef, field will ignore if it is null
     *
     * @param id the id of the accountRef to save.
     * @param accountRef the accountRef to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountRef,
     * or with status {@code 400 (Bad Request)} if the accountRef is not valid,
     * or with status {@code 404 (Not Found)} if the accountRef is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountRef couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/account-refs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccountRef> partialUpdateAccountRef(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccountRef accountRef
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccountRef partially : {}, {}", id, accountRef);
        if (accountRef.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountRef.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountRefRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccountRef> result = accountRefRepository
            .findById(accountRef.getId())
            .map(existingAccountRef -> {
                return existingAccountRef;
            })
            .map(accountRefRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, accountRef.getId().toString())
        );
    }

    /**
     * {@code GET  /account-refs} : get all the accountRefs.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountRefs in body.
     */
    @GetMapping("/account-refs")
    public List<AccountRef> getAllAccountRefs(@RequestParam(required = false) String filter) {
        if ("accountra-is-null".equals(filter)) {
            log.debug("REST request to get all AccountRefs where accountRA is null");
            return StreamSupport
                .stream(accountRefRepository.findAll().spliterator(), false)
                .filter(accountRef -> accountRef.getAccountRA() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all AccountRefs");
        return accountRefRepository.findAll();
    }

    /**
     * {@code GET  /account-refs/:id} : get the "id" accountRef.
     *
     * @param id the id of the accountRef to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountRef, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/account-refs/{id}")
    public ResponseEntity<AccountRef> getAccountRef(@PathVariable Long id) {
        log.debug("REST request to get AccountRef : {}", id);
        Optional<AccountRef> accountRef = accountRefRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(accountRef);
    }

    /**
     * {@code DELETE  /account-refs/:id} : delete the "id" accountRef.
     *
     * @param id the id of the accountRef to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/account-refs/{id}")
    public ResponseEntity<Void> deleteAccountRef(@PathVariable Long id) {
        log.debug("REST request to delete AccountRef : {}", id);
        accountRefRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
