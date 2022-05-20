jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DuyuruService } from '../service/duyuru.service';
import { IDuyuru, Duyuru } from '../duyuru.model';
import { IEgitim } from 'app/entities/egitim/egitim.model';
import { EgitimService } from 'app/entities/egitim/service/egitim.service';

import { DuyuruUpdateComponent } from './duyuru-update.component';

describe('Duyuru Management Update Component', () => {
  let comp: DuyuruUpdateComponent;
  let fixture: ComponentFixture<DuyuruUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let duyuruService: DuyuruService;
  let egitimService: EgitimService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DuyuruUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(DuyuruUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DuyuruUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    duyuruService = TestBed.inject(DuyuruService);
    egitimService = TestBed.inject(EgitimService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Egitim query and add missing value', () => {
      const duyuru: IDuyuru = { id: 456 };
      const egitim: IEgitim = { id: 77717 };
      duyuru.egitim = egitim;

      const egitimCollection: IEgitim[] = [{ id: 87807 }];
      jest.spyOn(egitimService, 'query').mockReturnValue(of(new HttpResponse({ body: egitimCollection })));
      const additionalEgitims = [egitim];
      const expectedCollection: IEgitim[] = [...additionalEgitims, ...egitimCollection];
      jest.spyOn(egitimService, 'addEgitimToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ duyuru });
      comp.ngOnInit();

      expect(egitimService.query).toHaveBeenCalled();
      expect(egitimService.addEgitimToCollectionIfMissing).toHaveBeenCalledWith(egitimCollection, ...additionalEgitims);
      expect(comp.egitimsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const duyuru: IDuyuru = { id: 456 };
      const egitim: IEgitim = { id: 45621 };
      duyuru.egitim = egitim;

      activatedRoute.data = of({ duyuru });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(duyuru));
      expect(comp.egitimsSharedCollection).toContain(egitim);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Duyuru>>();
      const duyuru = { id: 123 };
      jest.spyOn(duyuruService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ duyuru });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: duyuru }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(duyuruService.update).toHaveBeenCalledWith(duyuru);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Duyuru>>();
      const duyuru = new Duyuru();
      jest.spyOn(duyuruService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ duyuru });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: duyuru }));
      saveSubject.complete();

      // THEN
      expect(duyuruService.create).toHaveBeenCalledWith(duyuru);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Duyuru>>();
      const duyuru = { id: 123 };
      jest.spyOn(duyuruService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ duyuru });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(duyuruService.update).toHaveBeenCalledWith(duyuru);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEgitimById', () => {
      it('Should return tracked Egitim primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEgitimById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
