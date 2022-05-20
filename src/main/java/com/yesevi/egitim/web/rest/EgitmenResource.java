package com.yesevi.egitim.web.rest;

import com.yesevi.egitim.domain.Egitmen;
import com.yesevi.egitim.repository.EgitmenRepository;
import com.yesevi.egitim.service.EgitmenService;
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
 * REST controller for managing {@link com.yesevi.egitim.domain.Egitmen}.
 */
@RestController
@RequestMapping("/api")
public class EgitmenResource {

    private final Logger log = LoggerFactory.getLogger(EgitmenResource.class);

    private static final String ENTITY_NAME = "egitmen";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EgitmenService egitmenService;

    private final EgitmenRepository egitmenRepository;

    public EgitmenResource(EgitmenService egitmenService, EgitmenRepository egitmenRepository) {
        this.egitmenService = egitmenService;
        this.egitmenRepository = egitmenRepository;
    }

    /**
     * {@code POST  /egitmen} : Create a new egitmen.
     *
     * @param egitmen the egitmen to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new egitmen, or with status {@code 400 (Bad Request)} if the egitmen has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/egitmen")
    public ResponseEntity<Egitmen> createEgitmen(@RequestBody Egitmen egitmen) throws URISyntaxException {
        log.debug("REST request to save Egitmen : {}", egitmen);
        if (egitmen.getId() != null) {
            throw new BadRequestAlertException("A new egitmen cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Egitmen result = egitmenService.save(egitmen);
        return ResponseEntity
            .created(new URI("/api/egitmen/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /egitmen/:id} : Updates an existing egitmen.
     *
     * @param id the id of the egitmen to save.
     * @param egitmen the egitmen to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated egitmen,
     * or with status {@code 400 (Bad Request)} if the egitmen is not valid,
     * or with status {@code 500 (Internal Server Error)} if the egitmen couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/egitmen/{id}")
    public ResponseEntity<Egitmen> updateEgitmen(@PathVariable(value = "id", required = false) final Long id, @RequestBody Egitmen egitmen)
        throws URISyntaxException {
        log.debug("REST request to update Egitmen : {}, {}", id, egitmen);
        if (egitmen.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, egitmen.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!egitmenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Egitmen result = egitmenService.save(egitmen);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, egitmen.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /egitmen/:id} : Partial updates given fields of an existing egitmen, field will ignore if it is null
     *
     * @param id the id of the egitmen to save.
     * @param egitmen the egitmen to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated egitmen,
     * or with status {@code 400 (Bad Request)} if the egitmen is not valid,
     * or with status {@code 404 (Not Found)} if the egitmen is not found,
     * or with status {@code 500 (Internal Server Error)} if the egitmen couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/egitmen/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Egitmen> partialUpdateEgitmen(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Egitmen egitmen
    ) throws URISyntaxException {
        log.debug("REST request to partial update Egitmen partially : {}, {}", id, egitmen);
        if (egitmen.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, egitmen.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!egitmenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Egitmen> result = egitmenService.partialUpdate(egitmen);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, egitmen.getId().toString())
        );
    }

    /**
     * {@code GET  /egitmen} : get all the egitmen.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of egitmen in body.
     */
    @GetMapping("/egitmen")
    public ResponseEntity<List<Egitmen>> getAllEgitmen(Pageable pageable) {
        log.debug("REST request to get a page of Egitmen");
        Page<Egitmen> page = egitmenService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /egitmen/:id} : get the "id" egitmen.
     *
     * @param id the id of the egitmen to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the egitmen, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/egitmen/{id}")
    public ResponseEntity<Egitmen> getEgitmen(@PathVariable Long id) {
        log.debug("REST request to get Egitmen : {}", id);
        Optional<Egitmen> egitmen = egitmenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(egitmen);
    }

    /**
     * {@code DELETE  /egitmen/:id} : delete the "id" egitmen.
     *
     * @param id the id of the egitmen to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/egitmen/{id}")
    public ResponseEntity<Void> deleteEgitmen(@PathVariable Long id) {
        log.debug("REST request to delete Egitmen : {}", id);
        egitmenService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
