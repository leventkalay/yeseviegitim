package com.yesevi.egitim.web.rest;

import com.yesevi.egitim.domain.Takvim;
import com.yesevi.egitim.repository.TakvimRepository;
import com.yesevi.egitim.service.TakvimService;
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
 * REST controller for managing {@link com.yesevi.egitim.domain.Takvim}.
 */
@RestController
@RequestMapping("/api")
public class TakvimResource {

    private final Logger log = LoggerFactory.getLogger(TakvimResource.class);

    private static final String ENTITY_NAME = "takvim";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TakvimService takvimService;

    private final TakvimRepository takvimRepository;

    public TakvimResource(TakvimService takvimService, TakvimRepository takvimRepository) {
        this.takvimService = takvimService;
        this.takvimRepository = takvimRepository;
    }

    /**
     * {@code POST  /takvims} : Create a new takvim.
     *
     * @param takvim the takvim to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new takvim, or with status {@code 400 (Bad Request)} if the takvim has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/takvims")
    public ResponseEntity<Takvim> createTakvim(@RequestBody Takvim takvim) throws URISyntaxException {
        log.debug("REST request to save Takvim : {}", takvim);
        if (takvim.getId() != null) {
            throw new BadRequestAlertException("A new takvim cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Takvim result = takvimService.save(takvim);
        return ResponseEntity
            .created(new URI("/api/takvims/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /takvims/:id} : Updates an existing takvim.
     *
     * @param id the id of the takvim to save.
     * @param takvim the takvim to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated takvim,
     * or with status {@code 400 (Bad Request)} if the takvim is not valid,
     * or with status {@code 500 (Internal Server Error)} if the takvim couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/takvims/{id}")
    public ResponseEntity<Takvim> updateTakvim(@PathVariable(value = "id", required = false) final Long id, @RequestBody Takvim takvim)
        throws URISyntaxException {
        log.debug("REST request to update Takvim : {}, {}", id, takvim);
        if (takvim.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, takvim.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!takvimRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Takvim result = takvimService.save(takvim);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, takvim.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /takvims/:id} : Partial updates given fields of an existing takvim, field will ignore if it is null
     *
     * @param id the id of the takvim to save.
     * @param takvim the takvim to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated takvim,
     * or with status {@code 400 (Bad Request)} if the takvim is not valid,
     * or with status {@code 404 (Not Found)} if the takvim is not found,
     * or with status {@code 500 (Internal Server Error)} if the takvim couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/takvims/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Takvim> partialUpdateTakvim(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Takvim takvim
    ) throws URISyntaxException {
        log.debug("REST request to partial update Takvim partially : {}, {}", id, takvim);
        if (takvim.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, takvim.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!takvimRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Takvim> result = takvimService.partialUpdate(takvim);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, takvim.getId().toString())
        );
    }

    /**
     * {@code GET  /takvims} : get all the takvims.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of takvims in body.
     */
    @GetMapping("/takvims")
    public ResponseEntity<List<Takvim>> getAllTakvims(Pageable pageable) {
        log.debug("REST request to get a page of Takvims");
        Page<Takvim> page = takvimService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /takvims/:id} : get the "id" takvim.
     *
     * @param id the id of the takvim to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the takvim, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/takvims/{id}")
    public ResponseEntity<Takvim> getTakvim(@PathVariable Long id) {
        log.debug("REST request to get Takvim : {}", id);
        Optional<Takvim> takvim = takvimService.findOne(id);
        return ResponseUtil.wrapOrNotFound(takvim);
    }

    /**
     * {@code DELETE  /takvims/:id} : delete the "id" takvim.
     *
     * @param id the id of the takvim to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/takvims/{id}")
    public ResponseEntity<Void> deleteTakvim(@PathVariable Long id) {
        log.debug("REST request to delete Takvim : {}", id);
        takvimService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
