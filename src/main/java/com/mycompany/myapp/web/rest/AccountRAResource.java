package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AccountRA;
import com.mycompany.myapp.repository.AccountRARepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.AccountRA}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AccountRAResource {

    private final Logger log = LoggerFactory.getLogger(AccountRAResource.class);

    private static final String ENTITY_NAME = "accountRA";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountRARepository accountRARepository;

    public AccountRAResource(AccountRARepository accountRARepository) {
        this.accountRARepository = accountRARepository;
    }

    /**
     * {@code POST  /account-ras} : Create a new accountRA.
     *
     * @param accountRA the accountRA to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountRA, or with status {@code 400 (Bad Request)} if the accountRA has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/account-ras")
    public ResponseEntity<AccountRA> createAccountRA(@RequestBody AccountRA accountRA) throws URISyntaxException {
        log.debug("REST request to save AccountRA : {}", accountRA);
        if (accountRA.getId() != null) {
            throw new BadRequestAlertException("A new accountRA cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountRA result = accountRARepository.save(accountRA);
        return ResponseEntity
            .created(new URI("/api/account-ras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /account-ras/:id} : Updates an existing accountRA.
     *
     * @param id the id of the accountRA to save.
     * @param accountRA the accountRA to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountRA,
     * or with status {@code 400 (Bad Request)} if the accountRA is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountRA couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/account-ras/{id}")
    public ResponseEntity<AccountRA> updateAccountRA(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccountRA accountRA
    ) throws URISyntaxException {
        log.debug("REST request to update AccountRA : {}, {}", id, accountRA);
        if (accountRA.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountRA.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountRARepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AccountRA result = accountRARepository.save(accountRA);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, accountRA.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /account-ras/:id} : Partial updates given fields of an existing accountRA, field will ignore if it is null
     *
     * @param id the id of the accountRA to save.
     * @param accountRA the accountRA to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountRA,
     * or with status {@code 400 (Bad Request)} if the accountRA is not valid,
     * or with status {@code 404 (Not Found)} if the accountRA is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountRA couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/account-ras/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccountRA> partialUpdateAccountRA(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccountRA accountRA
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccountRA partially : {}, {}", id, accountRA);
        if (accountRA.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountRA.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountRARepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccountRA> result = accountRARepository
            .findById(accountRA.getId())
            .map(existingAccountRA -> {
                return existingAccountRA;
            })
            .map(accountRARepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, accountRA.getId().toString())
        );
    }

    /**
     * {@code GET  /account-ras} : get all the accountRAS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountRAS in body.
     */
    @GetMapping("/account-ras")
    public List<AccountRA> getAllAccountRAS() {
        log.debug("REST request to get all AccountRAS");
        return accountRARepository.findAll();
    }

    /**
     * {@code GET  /account-ras/:id} : get the "id" accountRA.
     *
     * @param id the id of the accountRA to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountRA, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/account-ras/{id}")
    public ResponseEntity<AccountRA> getAccountRA(@PathVariable Long id) {
        log.debug("REST request to get AccountRA : {}", id);
        Optional<AccountRA> accountRA = accountRARepository.findById(id);
        return ResponseUtil.wrapOrNotFound(accountRA);
    }

    /**
     * {@code DELETE  /account-ras/:id} : delete the "id" accountRA.
     *
     * @param id the id of the accountRA to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/account-ras/{id}")
    public ResponseEntity<Void> deleteAccountRA(@PathVariable Long id) {
        log.debug("REST request to delete AccountRA : {}", id);
        accountRARepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
