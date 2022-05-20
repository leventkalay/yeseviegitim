package com.yesevi.egitim.web.rest;

import com.yesevi.egitim.domain.Ders;
import com.yesevi.egitim.repository.DersRepository;
import com.yesevi.egitim.service.DersService;
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
 * REST controller for managing {@link com.yesevi.egitim.domain.Ders}.
 */
@RestController
@RequestMapping("/api")
public class DersResource {

    private final Logger log = LoggerFactory.getLogger(DersResource.class);

    private static final String ENTITY_NAME = "ders";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DersService dersService;

    private final DersRepository dersRepository;

    public DersResource(DersService dersService, DersRepository dersRepository) {
        this.dersService = dersService;
        this.dersRepository = dersRepository;
    }

    /**
     * {@code POST  /ders} : Create a new ders.
     *
     * @param ders the ders to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ders, or with status {@code 400 (Bad Request)} if the ders has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ders")
    public ResponseEntity<Ders> createDers(@RequestBody Ders ders) throws URISyntaxException {
        log.debug("REST request to save Ders : {}", ders);
        if (ders.getId() != null) {
            throw new BadRequestAlertException("A new ders cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ders result = dersService.save(ders);
        return ResponseEntity
            .created(new URI("/api/ders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ders/:id} : Updates an existing ders.
     *
     * @param id the id of the ders to save.
     * @param ders the ders to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ders,
     * or with status {@code 400 (Bad Request)} if the ders is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ders couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ders/{id}")
    public ResponseEntity<Ders> updateDers(@PathVariable(value = "id", required = false) final Long id, @RequestBody Ders ders)
        throws URISyntaxException {
        log.debug("REST request to update Ders : {}, {}", id, ders);
        if (ders.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ders.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ders result = dersService.save(ders);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ders.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ders/:id} : Partial updates given fields of an existing ders, field will ignore if it is null
     *
     * @param id the id of the ders to save.
     * @param ders the ders to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ders,
     * or with status {@code 400 (Bad Request)} if the ders is not valid,
     * or with status {@code 404 (Not Found)} if the ders is not found,
     * or with status {@code 500 (Internal Server Error)} if the ders couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ders/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Ders> partialUpdateDers(@PathVariable(value = "id", required = false) final Long id, @RequestBody Ders ders)
        throws URISyntaxException {
        log.debug("REST request to partial update Ders partially : {}, {}", id, ders);
        if (ders.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ders.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ders> result = dersService.partialUpdate(ders);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ders.getId().toString())
        );
    }

    /**
     * {@code GET  /ders} : get all the ders.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ders in body.
     */
    @GetMapping("/ders")
    public ResponseEntity<List<Ders>> getAllDers(Pageable pageable) {
        log.debug("REST request to get a page of Ders");
        Page<Ders> page = dersService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ders/:id} : get the "id" ders.
     *
     * @param id the id of the ders to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ders, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ders/{id}")
    public ResponseEntity<Ders> getDers(@PathVariable Long id) {
        log.debug("REST request to get Ders : {}", id);
        Optional<Ders> ders = dersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ders);
    }

    /**
     * {@code DELETE  /ders/:id} : delete the "id" ders.
     *
     * @param id the id of the ders to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ders/{id}")
    public ResponseEntity<Void> deleteDers(@PathVariable Long id) {
        log.debug("REST request to delete Ders : {}", id);
        dersService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
