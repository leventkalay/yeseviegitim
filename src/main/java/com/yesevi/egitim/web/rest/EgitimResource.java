package com.yesevi.egitim.web.rest;

import com.yesevi.egitim.domain.Egitim;
import com.yesevi.egitim.repository.EgitimRepository;
import com.yesevi.egitim.service.EgitimService;
import com.yesevi.egitim.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.yesevi.egitim.domain.Egitim}.
 */
@RestController
@RequestMapping("/api")
public class EgitimResource {

    private final Logger log = LoggerFactory.getLogger(EgitimResource.class);

    private static final String ENTITY_NAME = "egitim";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EgitimService egitimService;

    private final EgitimRepository egitimRepository;

    public EgitimResource(EgitimService egitimService, EgitimRepository egitimRepository) {
        this.egitimService = egitimService;
        this.egitimRepository = egitimRepository;
    }

    /**
     * {@code POST  /egitims} : Create a new egitim.
     *
     * @param egitim the egitim to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new egitim, or with status {@code 400 (Bad Request)} if the egitim has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/egitims")
    public ResponseEntity<Egitim> createEgitim(@RequestBody Egitim egitim) throws URISyntaxException {
        log.debug("REST request to save Egitim : {}", egitim);
        if (egitim.getId() != null) {
            throw new BadRequestAlertException("A new egitim cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Egitim result = egitimService.save(egitim);
        return ResponseEntity
            .created(new URI("/api/egitims/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /egitims/:id} : Updates an existing egitim.
     *
     * @param id the id of the egitim to save.
     * @param egitim the egitim to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated egitim,
     * or with status {@code 400 (Bad Request)} if the egitim is not valid,
     * or with status {@code 500 (Internal Server Error)} if the egitim couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/egitims/{id}")
    public ResponseEntity<Egitim> updateEgitim(@PathVariable(value = "id", required = false) final Long id, @RequestBody Egitim egitim)
        throws URISyntaxException {
        log.debug("REST request to update Egitim : {}, {}", id, egitim);
        if (egitim.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, egitim.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!egitimRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Egitim result = egitimService.save(egitim);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, egitim.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /egitims/:id} : Partial updates given fields of an existing egitim, field will ignore if it is null
     *
     * @param id the id of the egitim to save.
     * @param egitim the egitim to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated egitim,
     * or with status {@code 400 (Bad Request)} if the egitim is not valid,
     * or with status {@code 404 (Not Found)} if the egitim is not found,
     * or with status {@code 500 (Internal Server Error)} if the egitim couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/egitims/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Egitim> partialUpdateEgitim(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Egitim egitim
    ) throws URISyntaxException {
        log.debug("REST request to partial update Egitim partially : {}, {}", id, egitim);
        if (egitim.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, egitim.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!egitimRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Egitim> result = egitimService.partialUpdate(egitim);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, egitim.getId().toString())
        );
    }

    /**
     * {@code GET  /egitims} : get all the egitims.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of egitims in body.
     */
    @GetMapping("/egitims")
    public ResponseEntity<List<Egitim>> getAllEgitims(Pageable pageable) {
        log.debug("REST request to get a page of Egitims");
        Page<Egitim> page = egitimService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /egitims/:id} : get the "id" egitim.
     *
     * @param id the id of the egitim to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the egitim, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/egitims/{id}")
    public ResponseEntity<Egitim> getEgitim(@PathVariable Long id) {
        log.debug("REST request to get Egitim : {}", id);
        Optional<Egitim> egitim = egitimService.findOne(id);
        return ResponseUtil.wrapOrNotFound(egitim);
    }

    /**
     * {@code DELETE  /egitims/:id} : delete the "id" egitim.
     *
     * @param id the id of the egitim to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/egitims/{id}")
    public ResponseEntity<Void> deleteEgitim(@PathVariable Long id) {
        log.debug("REST request to delete Egitim : {}", id);
        egitimService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
