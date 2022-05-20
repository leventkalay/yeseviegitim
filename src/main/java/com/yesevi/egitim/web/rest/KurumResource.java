package com.yesevi.egitim.web.rest;

import com.yesevi.egitim.domain.Kurum;
import com.yesevi.egitim.repository.KurumRepository;
import com.yesevi.egitim.service.KurumService;
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
 * REST controller for managing {@link com.yesevi.egitim.domain.Kurum}.
 */
@RestController
@RequestMapping("/api")
public class KurumResource {

    private final Logger log = LoggerFactory.getLogger(KurumResource.class);

    private static final String ENTITY_NAME = "kurum";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KurumService kurumService;

    private final KurumRepository kurumRepository;

    public KurumResource(KurumService kurumService, KurumRepository kurumRepository) {
        this.kurumService = kurumService;
        this.kurumRepository = kurumRepository;
    }

    /**
     * {@code POST  /kurums} : Create a new kurum.
     *
     * @param kurum the kurum to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new kurum, or with status {@code 400 (Bad Request)} if the kurum has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/kurums")
    public ResponseEntity<Kurum> createKurum(@RequestBody Kurum kurum) throws URISyntaxException {
        log.debug("REST request to save Kurum : {}", kurum);
        if (kurum.getId() != null) {
            throw new BadRequestAlertException("A new kurum cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Kurum result = kurumService.save(kurum);
        return ResponseEntity
            .created(new URI("/api/kurums/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /kurums/:id} : Updates an existing kurum.
     *
     * @param id the id of the kurum to save.
     * @param kurum the kurum to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kurum,
     * or with status {@code 400 (Bad Request)} if the kurum is not valid,
     * or with status {@code 500 (Internal Server Error)} if the kurum couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/kurums/{id}")
    public ResponseEntity<Kurum> updateKurum(@PathVariable(value = "id", required = false) final Long id, @RequestBody Kurum kurum)
        throws URISyntaxException {
        log.debug("REST request to update Kurum : {}, {}", id, kurum);
        if (kurum.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, kurum.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kurumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Kurum result = kurumService.save(kurum);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kurum.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /kurums/:id} : Partial updates given fields of an existing kurum, field will ignore if it is null
     *
     * @param id the id of the kurum to save.
     * @param kurum the kurum to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kurum,
     * or with status {@code 400 (Bad Request)} if the kurum is not valid,
     * or with status {@code 404 (Not Found)} if the kurum is not found,
     * or with status {@code 500 (Internal Server Error)} if the kurum couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/kurums/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Kurum> partialUpdateKurum(@PathVariable(value = "id", required = false) final Long id, @RequestBody Kurum kurum)
        throws URISyntaxException {
        log.debug("REST request to partial update Kurum partially : {}, {}", id, kurum);
        if (kurum.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, kurum.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kurumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Kurum> result = kurumService.partialUpdate(kurum);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kurum.getId().toString())
        );
    }

    /**
     * {@code GET  /kurums} : get all the kurums.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of kurums in body.
     */
    @GetMapping("/kurums")
    public ResponseEntity<List<Kurum>> getAllKurums(Pageable pageable) {
        log.debug("REST request to get a page of Kurums");
        Page<Kurum> page = kurumService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /kurums/:id} : get the "id" kurum.
     *
     * @param id the id of the kurum to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the kurum, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/kurums/{id}")
    public ResponseEntity<Kurum> getKurum(@PathVariable Long id) {
        log.debug("REST request to get Kurum : {}", id);
        Optional<Kurum> kurum = kurumService.findOne(id);
        return ResponseUtil.wrapOrNotFound(kurum);
    }

    /**
     * {@code DELETE  /kurums/:id} : delete the "id" kurum.
     *
     * @param id the id of the kurum to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/kurums/{id}")
    public ResponseEntity<Void> deleteKurum(@PathVariable Long id) {
        log.debug("REST request to delete Kurum : {}", id);
        kurumService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
