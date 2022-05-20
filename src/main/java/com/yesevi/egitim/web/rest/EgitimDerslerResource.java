package com.yesevi.egitim.web.rest;

import com.yesevi.egitim.domain.EgitimDersler;
import com.yesevi.egitim.repository.EgitimDerslerRepository;
import com.yesevi.egitim.service.EgitimDerslerService;
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
 * REST controller for managing {@link com.yesevi.egitim.domain.EgitimDersler}.
 */
@RestController
@RequestMapping("/api")
public class EgitimDerslerResource {

    private final Logger log = LoggerFactory.getLogger(EgitimDerslerResource.class);

    private static final String ENTITY_NAME = "egitimDersler";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EgitimDerslerService egitimDerslerService;

    private final EgitimDerslerRepository egitimDerslerRepository;

    public EgitimDerslerResource(EgitimDerslerService egitimDerslerService, EgitimDerslerRepository egitimDerslerRepository) {
        this.egitimDerslerService = egitimDerslerService;
        this.egitimDerslerRepository = egitimDerslerRepository;
    }

    /**
     * {@code POST  /egitim-derslers} : Create a new egitimDersler.
     *
     * @param egitimDersler the egitimDersler to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new egitimDersler, or with status {@code 400 (Bad Request)} if the egitimDersler has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/egitim-derslers")
    public ResponseEntity<EgitimDersler> createEgitimDersler(@RequestBody EgitimDersler egitimDersler) throws URISyntaxException {
        log.debug("REST request to save EgitimDersler : {}", egitimDersler);
        if (egitimDersler.getId() != null) {
            throw new BadRequestAlertException("A new egitimDersler cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EgitimDersler result = egitimDerslerService.save(egitimDersler);
        return ResponseEntity
            .created(new URI("/api/egitim-derslers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /egitim-derslers/:id} : Updates an existing egitimDersler.
     *
     * @param id the id of the egitimDersler to save.
     * @param egitimDersler the egitimDersler to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated egitimDersler,
     * or with status {@code 400 (Bad Request)} if the egitimDersler is not valid,
     * or with status {@code 500 (Internal Server Error)} if the egitimDersler couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/egitim-derslers/{id}")
    public ResponseEntity<EgitimDersler> updateEgitimDersler(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EgitimDersler egitimDersler
    ) throws URISyntaxException {
        log.debug("REST request to update EgitimDersler : {}, {}", id, egitimDersler);
        if (egitimDersler.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, egitimDersler.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!egitimDerslerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EgitimDersler result = egitimDerslerService.save(egitimDersler);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, egitimDersler.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /egitim-derslers/:id} : Partial updates given fields of an existing egitimDersler, field will ignore if it is null
     *
     * @param id the id of the egitimDersler to save.
     * @param egitimDersler the egitimDersler to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated egitimDersler,
     * or with status {@code 400 (Bad Request)} if the egitimDersler is not valid,
     * or with status {@code 404 (Not Found)} if the egitimDersler is not found,
     * or with status {@code 500 (Internal Server Error)} if the egitimDersler couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/egitim-derslers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EgitimDersler> partialUpdateEgitimDersler(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EgitimDersler egitimDersler
    ) throws URISyntaxException {
        log.debug("REST request to partial update EgitimDersler partially : {}, {}", id, egitimDersler);
        if (egitimDersler.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, egitimDersler.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!egitimDerslerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EgitimDersler> result = egitimDerslerService.partialUpdate(egitimDersler);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, egitimDersler.getId().toString())
        );
    }

    /**
     * {@code GET  /egitim-derslers} : get all the egitimDerslers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of egitimDerslers in body.
     */
    @GetMapping("/egitim-derslers")
    public ResponseEntity<List<EgitimDersler>> getAllEgitimDerslers(Pageable pageable) {
        log.debug("REST request to get a page of EgitimDerslers");
        Page<EgitimDersler> page = egitimDerslerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /egitim-derslers/:id} : get the "id" egitimDersler.
     *
     * @param id the id of the egitimDersler to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the egitimDersler, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/egitim-derslers/{id}")
    public ResponseEntity<EgitimDersler> getEgitimDersler(@PathVariable Long id) {
        log.debug("REST request to get EgitimDersler : {}", id);
        Optional<EgitimDersler> egitimDersler = egitimDerslerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(egitimDersler);
    }

    /**
     * {@code DELETE  /egitim-derslers/:id} : delete the "id" egitimDersler.
     *
     * @param id the id of the egitimDersler to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/egitim-derslers/{id}")
    public ResponseEntity<Void> deleteEgitimDersler(@PathVariable Long id) {
        log.debug("REST request to delete EgitimDersler : {}", id);
        egitimDerslerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
