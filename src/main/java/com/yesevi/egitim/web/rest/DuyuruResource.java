package com.yesevi.egitim.web.rest;

import com.yesevi.egitim.domain.Duyuru;
import com.yesevi.egitim.repository.DuyuruRepository;
import com.yesevi.egitim.service.DuyuruService;
import com.yesevi.egitim.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.yesevi.egitim.domain.Duyuru}.
 */
@RestController
@RequestMapping("/api")
public class DuyuruResource {

    private final Logger log = LoggerFactory.getLogger(DuyuruResource.class);

    private static final String ENTITY_NAME = "duyuru";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DuyuruService duyuruService;

    private final DuyuruRepository duyuruRepository;

    public DuyuruResource(DuyuruService duyuruService, DuyuruRepository duyuruRepository) {
        this.duyuruService = duyuruService;
        this.duyuruRepository = duyuruRepository;
    }

    /**
     * {@code POST  /duyurus} : Create a new duyuru.
     *
     * @param duyuru the duyuru to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new duyuru, or with status {@code 400 (Bad Request)} if the duyuru has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/duyurus")
    public ResponseEntity<Duyuru> createDuyuru(@Valid @RequestBody Duyuru duyuru) throws URISyntaxException {
        log.debug("REST request to save Duyuru : {}", duyuru);
        if (duyuru.getId() != null) {
            throw new BadRequestAlertException("A new duyuru cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Duyuru result = duyuruService.save(duyuru);
        return ResponseEntity
            .created(new URI("/api/duyurus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /duyurus/:id} : Updates an existing duyuru.
     *
     * @param id the id of the duyuru to save.
     * @param duyuru the duyuru to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated duyuru,
     * or with status {@code 400 (Bad Request)} if the duyuru is not valid,
     * or with status {@code 500 (Internal Server Error)} if the duyuru couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/duyurus/{id}")
    public ResponseEntity<Duyuru> updateDuyuru(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Duyuru duyuru
    ) throws URISyntaxException {
        log.debug("REST request to update Duyuru : {}, {}", id, duyuru);
        if (duyuru.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, duyuru.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!duyuruRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Duyuru result = duyuruService.save(duyuru);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, duyuru.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /duyurus/:id} : Partial updates given fields of an existing duyuru, field will ignore if it is null
     *
     * @param id the id of the duyuru to save.
     * @param duyuru the duyuru to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated duyuru,
     * or with status {@code 400 (Bad Request)} if the duyuru is not valid,
     * or with status {@code 404 (Not Found)} if the duyuru is not found,
     * or with status {@code 500 (Internal Server Error)} if the duyuru couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/duyurus/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Duyuru> partialUpdateDuyuru(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Duyuru duyuru
    ) throws URISyntaxException {
        log.debug("REST request to partial update Duyuru partially : {}, {}", id, duyuru);
        if (duyuru.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, duyuru.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!duyuruRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Duyuru> result = duyuruService.partialUpdate(duyuru);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, duyuru.getId().toString())
        );
    }

    /**
     * {@code GET  /duyurus} : get all the duyurus.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of duyurus in body.
     */
    @GetMapping("/duyurus")
    public ResponseEntity<List<Duyuru>> getAllDuyurus(Pageable pageable) {
        log.debug("REST request to get a page of Duyurus");
        Page<Duyuru> page = duyuruService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /duyurus/:id} : get the "id" duyuru.
     *
     * @param id the id of the duyuru to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the duyuru, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/duyurus/{id}")
    public ResponseEntity<Duyuru> getDuyuru(@PathVariable Long id) {
        log.debug("REST request to get Duyuru : {}", id);
        Optional<Duyuru> duyuru = duyuruService.findOne(id);
        return ResponseUtil.wrapOrNotFound(duyuru);
    }

    /**
     * {@code DELETE  /duyurus/:id} : delete the "id" duyuru.
     *
     * @param id the id of the duyuru to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/duyurus/{id}")
    public ResponseEntity<Void> deleteDuyuru(@PathVariable Long id) {
        log.debug("REST request to delete Duyuru : {}", id);
        duyuruService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
