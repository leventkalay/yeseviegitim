package com.yesevi.egitim.web.rest;

import com.yesevi.egitim.domain.EgitimTuru;
import com.yesevi.egitim.repository.EgitimTuruRepository;
import com.yesevi.egitim.service.EgitimTuruService;
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
 * REST controller for managing {@link com.yesevi.egitim.domain.EgitimTuru}.
 */
@RestController
@RequestMapping("/api")
public class EgitimTuruResource {

    private final Logger log = LoggerFactory.getLogger(EgitimTuruResource.class);

    private static final String ENTITY_NAME = "egitimTuru";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EgitimTuruService egitimTuruService;

    private final EgitimTuruRepository egitimTuruRepository;

    public EgitimTuruResource(EgitimTuruService egitimTuruService, EgitimTuruRepository egitimTuruRepository) {
        this.egitimTuruService = egitimTuruService;
        this.egitimTuruRepository = egitimTuruRepository;
    }

    /**
     * {@code POST  /egitim-turus} : Create a new egitimTuru.
     *
     * @param egitimTuru the egitimTuru to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new egitimTuru, or with status {@code 400 (Bad Request)} if the egitimTuru has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/egitim-turus")
    public ResponseEntity<EgitimTuru> createEgitimTuru(@RequestBody EgitimTuru egitimTuru) throws URISyntaxException {
        log.debug("REST request to save EgitimTuru : {}", egitimTuru);
        if (egitimTuru.getId() != null) {
            throw new BadRequestAlertException("A new egitimTuru cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EgitimTuru result = egitimTuruService.save(egitimTuru);
        return ResponseEntity
            .created(new URI("/api/egitim-turus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /egitim-turus/:id} : Updates an existing egitimTuru.
     *
     * @param id the id of the egitimTuru to save.
     * @param egitimTuru the egitimTuru to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated egitimTuru,
     * or with status {@code 400 (Bad Request)} if the egitimTuru is not valid,
     * or with status {@code 500 (Internal Server Error)} if the egitimTuru couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/egitim-turus/{id}")
    public ResponseEntity<EgitimTuru> updateEgitimTuru(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EgitimTuru egitimTuru
    ) throws URISyntaxException {
        log.debug("REST request to update EgitimTuru : {}, {}", id, egitimTuru);
        if (egitimTuru.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, egitimTuru.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!egitimTuruRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EgitimTuru result = egitimTuruService.save(egitimTuru);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, egitimTuru.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /egitim-turus/:id} : Partial updates given fields of an existing egitimTuru, field will ignore if it is null
     *
     * @param id the id of the egitimTuru to save.
     * @param egitimTuru the egitimTuru to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated egitimTuru,
     * or with status {@code 400 (Bad Request)} if the egitimTuru is not valid,
     * or with status {@code 404 (Not Found)} if the egitimTuru is not found,
     * or with status {@code 500 (Internal Server Error)} if the egitimTuru couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/egitim-turus/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EgitimTuru> partialUpdateEgitimTuru(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EgitimTuru egitimTuru
    ) throws URISyntaxException {
        log.debug("REST request to partial update EgitimTuru partially : {}, {}", id, egitimTuru);
        if (egitimTuru.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, egitimTuru.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!egitimTuruRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EgitimTuru> result = egitimTuruService.partialUpdate(egitimTuru);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, egitimTuru.getId().toString())
        );
    }

    /**
     * {@code GET  /egitim-turus} : get all the egitimTurus.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of egitimTurus in body.
     */
    @GetMapping("/egitim-turus")
    public ResponseEntity<List<EgitimTuru>> getAllEgitimTurus(Pageable pageable) {
        log.debug("REST request to get a page of EgitimTurus");
        Page<EgitimTuru> page = egitimTuruService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /egitim-turus/:id} : get the "id" egitimTuru.
     *
     * @param id the id of the egitimTuru to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the egitimTuru, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/egitim-turus/{id}")
    public ResponseEntity<EgitimTuru> getEgitimTuru(@PathVariable Long id) {
        log.debug("REST request to get EgitimTuru : {}", id);
        Optional<EgitimTuru> egitimTuru = egitimTuruService.findOne(id);
        return ResponseUtil.wrapOrNotFound(egitimTuru);
    }

    /**
     * {@code DELETE  /egitim-turus/:id} : delete the "id" egitimTuru.
     *
     * @param id the id of the egitimTuru to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/egitim-turus/{id}")
    public ResponseEntity<Void> deleteEgitimTuru(@PathVariable Long id) {
        log.debug("REST request to delete EgitimTuru : {}", id);
        egitimTuruService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
