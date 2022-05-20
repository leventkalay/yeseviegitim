jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DersService } from '../service/ders.service';
import { IDers, Ders } from '../ders.model';
import { IEgitim } from 'app/entities/egitim/egitim.model';
import { EgitimService } from 'app/entities/egitim/service/egitim.service';

import { DersUpdateComponent } from './ders-update.component';

describe('Ders Management Update Component', () => {
  let comp: DersUpdateComponent;
  let fixture: ComponentFixture<DersUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dersService: DersService;
  let egitimService: EgitimService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DersUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(DersUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DersUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dersService = TestBed.inject(DersService);
    egitimService = TestBed.inject(EgitimService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Egitim query and add missing value', () => {
      const ders: IDers = { id: 456 };
      const egitim: IEgitim = { id: 27031 };
      ders.egitim = egitim;

      const egitimCollection: IEgitim[] = [{ id: 68426 }];
      jest.spyOn(egitimService, 'query').mockReturnValue(of(new HttpResponse({ body: egitimCollection })));
      const additionalEgitims = [egitim];
      const expectedCollection: IEgitim[] = [...additionalEgitims, ...egitimCollection];
      jest.spyOn(egitimService, 'addEgitimToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ders });
      comp.ngOnInit();

      expect(egitimService.query).toHaveBeenCalled();
      expect(egitimService.addEgitimToCollectionIfMissing).toHaveBeenCalledWith(egitimCollection, ...additionalEgitims);
      expect(comp.egitimsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ders: IDers = { id: 456 };
      const egitim: IEgitim = { id: 24711 };
      ders.egitim = egitim;

      activatedRoute.data = of({ ders });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(ders));
      expect(comp.egitimsSharedCollection).toContain(egitim);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ders>>();
      const ders = { id: 123 };
      jest.spyOn(dersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ders });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ders }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(dersService.update).toHaveBeenCalledWith(ders);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ders>>();
      const ders = new Ders();
      jest.spyOn(dersService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ders });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ders }));
      saveSubject.complete();

      // THEN
      expect(dersService.create).toHaveBeenCalledWith(ders);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ders>>();
      const ders = { id: 123 };
      jest.spyOn(dersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ders });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dersService.update).toHaveBeenCalledWith(ders);
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
