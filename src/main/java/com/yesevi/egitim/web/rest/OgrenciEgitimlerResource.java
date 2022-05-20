package com.yesevi.egitim.web.rest;

import com.yesevi.egitim.domain.OgrenciEgitimler;
import com.yesevi.egitim.repository.OgrenciEgitimlerRepository;
import com.yesevi.egitim.service.OgrenciEgitimlerService;
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
 * REST controller for managing {@link com.yesevi.egitim.domain.OgrenciEgitimler}.
 */
@RestController
@RequestMapping("/api")
public class OgrenciEgitimlerResource {

    private final Logger log = LoggerFactory.getLogger(OgrenciEgitimlerResource.class);

    private static final String ENTITY_NAME = "ogrenciEgitimler";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OgrenciEgitimlerService ogrenciEgitimlerService;

    private final OgrenciEgitimlerRepository ogrenciEgitimlerRepository;

    public OgrenciEgitimlerResource(
        OgrenciEgitimlerService ogrenciEgitimlerService,
        OgrenciEgitimlerRepository ogrenciEgitimlerRepository
    ) {
        this.ogrenciEgitimlerService = ogrenciEgitimlerService;
        this.ogrenciEgitimlerRepository = ogrenciEgitimlerRepository;
    }

    /**
     * {@code POST  /ogrenci-egitimlers} : Create a new ogrenciEgitimler.
     *
     * @param ogrenciEgitimler the ogrenciEgitimler to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ogrenciEgitimler, or with status {@code 400 (Bad Request)} if the ogrenciEgitimler has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ogrenci-egitimlers")
    public ResponseEntity<OgrenciEgitimler> createOgrenciEgitimler(@RequestBody OgrenciEgitimler ogrenciEgitimler)
        throws URISyntaxException {
        log.debug("REST request to save OgrenciEgitimler : {}", ogrenciEgitimler);
        if (ogrenciEgitimler.getId() != null) {
            throw new BadRequestAlertException("A new ogrenciEgitimler cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OgrenciEgitimler result = ogrenciEgitimlerService.save(ogrenciEgitimler);
        return ResponseEntity
            .created(new URI("/api/ogrenci-egitimlers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ogrenci-egitimlers/:id} : Updates an existing ogrenciEgitimler.
     *
     * @param id the id of the ogrenciEgitimler to save.
     * @param ogrenciEgitimler the ogrenciEgitimler to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ogrenciEgitimler,
     * or with status {@code 400 (Bad Request)} if the ogrenciEgitimler is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ogrenciEgitimler couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ogrenci-egitimlers/{id}")
    public ResponseEntity<OgrenciEgitimler> updateOgrenciEgitimler(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OgrenciEgitimler ogrenciEgitimler
    ) throws URISyntaxException {
        log.debug("REST request to update OgrenciEgitimler : {}, {}", id, ogrenciEgitimler);
        if (ogrenciEgitimler.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ogrenciEgitimler.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ogrenciEgitimlerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OgrenciEgitimler result = ogrenciEgitimlerService.save(ogrenciEgitimler);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ogrenciEgitimler.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ogrenci-egitimlers/:id} : Partial updates given fields of an existing ogrenciEgitimler, field will ignore if it is null
     *
     * @param id the id of the ogrenciEgitimler to save.
     * @param ogrenciEgitimler the ogrenciEgitimler to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ogrenciEgitimler,
     * or with status {@code 400 (Bad Request)} if the ogrenciEgitimler is not valid,
     * or with status {@code 404 (Not Found)} if the ogrenciEgitimler is not found,
     * or with status {@code 500 (Internal Server Error)} if the ogrenciEgitimler couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ogrenci-egitimlers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OgrenciEgitimler> partialUpdateOgrenciEgitimler(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OgrenciEgitimler ogrenciEgitimler
    ) throws URISyntaxException {
        log.debug("REST request to partial update OgrenciEgitimler partially : {}, {}", id, ogrenciEgitimler);
        if (ogrenciEgitimler.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ogrenciEgitimler.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ogrenciEgitimlerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OgrenciEgitimler> result = ogrenciEgitimlerService.partialUpdate(ogrenciEgitimler);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ogrenciEgitimler.getId().toString())
        );
    }

    /**
     * {@code GET  /ogrenci-egitimlers} : get all the ogrenciEgitimlers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ogrenciEgitimlers in body.
     */
    @GetMapping("/ogrenci-egitimlers")
    public ResponseEntity<List<OgrenciEgitimler>> getAllOgrenciEgitimlers(Pageable pageable) {
        log.debug("REST request to get a page of OgrenciEgitimlers");
        Page<OgrenciEgitimler> page = ogrenciEgitimlerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ogrenci-egitimlers/:id} : get the "id" ogrenciEgitimler.
     *
     * @param id the id of the ogrenciEgitimler to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ogrenciEgitimler, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ogrenci-egitimlers/{id}")
    public ResponseEntity<OgrenciEgitimler> getOgrenciEgitimler(@PathVariable Long id) {
        log.debug("REST request to get OgrenciEgitimler : {}", id);
        Optional<OgrenciEgitimler> ogrenciEgitimler = ogrenciEgitimlerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ogrenciEgitimler);
    }

    /**
     * {@code DELETE  /ogrenci-egitimlers/:id} : delete the "id" ogrenciEgitimler.
     *
     * @param id the id of the ogrenciEgitimler to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ogrenci-egitimlers/{id}")
    public ResponseEntity<Void> deleteOgrenciEgitimler(@PathVariable Long id) {
        log.debug("REST request to delete OgrenciEgitimler : {}", id);
        ogrenciEgitimlerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
