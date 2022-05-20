jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TakvimService } from '../service/takvim.service';
import { ITakvim, Takvim } from '../takvim.model';
import { IEgitim } from 'app/entities/egitim/egitim.model';
import { EgitimService } from 'app/entities/egitim/service/egitim.service';

import { TakvimUpdateComponent } from './takvim-update.component';

describe('Takvim Management Update Component', () => {
  let comp: TakvimUpdateComponent;
  let fixture: ComponentFixture<TakvimUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let takvimService: TakvimService;
  let egitimService: EgitimService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TakvimUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TakvimUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TakvimUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    takvimService = TestBed.inject(TakvimService);
    egitimService = TestBed.inject(EgitimService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Egitim query and add missing value', () => {
      const takvim: ITakvim = { id: 456 };
      const egitim: IEgitim = { id: 35747 };
      takvim.egitim = egitim;

      const egitimCollection: IEgitim[] = [{ id: 37971 }];
      jest.spyOn(egitimService, 'query').mockReturnValue(of(new HttpResponse({ body: egitimCollection })));
      const additionalEgitims = [egitim];
      const expectedCollection: IEgitim[] = [...additionalEgitims, ...egitimCollection];
      jest.spyOn(egitimService, 'addEgitimToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ takvim });
      comp.ngOnInit();

      expect(egitimService.query).toHaveBeenCalled();
      expect(egitimService.addEgitimToCollectionIfMissing).toHaveBeenCalledWith(egitimCollection, ...additionalEgitims);
      expect(comp.egitimsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const takvim: ITakvim = { id: 456 };
      const egitim: IEgitim = { id: 12190 };
      takvim.egitim = egitim;

      activatedRoute.data = of({ takvim });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(takvim));
      expect(comp.egitimsSharedCollection).toContain(egitim);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Takvim>>();
      const takvim = { id: 123 };
      jest.spyOn(takvimService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ takvim });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: takvim }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(takvimService.update).toHaveBeenCalledWith(takvim);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Takvim>>();
      const takvim = new Takvim();
      jest.spyOn(takvimService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ takvim });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: takvim }));
      saveSubject.complete();

      // THEN
      expect(takvimService.create).toHaveBeenCalledWith(takvim);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Takvim>>();
      const takvim = { id: 123 };
      jest.spyOn(takvimService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ takvim });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(takvimService.update).toHaveBeenCalledWith(takvim);
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
