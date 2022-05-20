jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EgitimTuruService } from '../service/egitim-turu.service';
import { IEgitimTuru, EgitimTuru } from '../egitim-turu.model';

import { EgitimTuruUpdateComponent } from './egitim-turu-update.component';

describe('EgitimTuru Management Update Component', () => {
  let comp: EgitimTuruUpdateComponent;
  let fixture: ComponentFixture<EgitimTuruUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let egitimTuruService: EgitimTuruService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EgitimTuruUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(EgitimTuruUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EgitimTuruUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    egitimTuruService = TestBed.inject(EgitimTuruService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const egitimTuru: IEgitimTuru = { id: 456 };

      activatedRoute.data = of({ egitimTuru });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(egitimTuru));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<EgitimTuru>>();
      const egitimTuru = { id: 123 };
      jest.spyOn(egitimTuruService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ egitimTuru });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: egitimTuru }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(egitimTuruService.update).toHaveBeenCalledWith(egitimTuru);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<EgitimTuru>>();
      const egitimTuru = new EgitimTuru();
      jest.spyOn(egitimTuruService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ egitimTuru });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: egitimTuru }));
      saveSubject.complete();

      // THEN
      expect(egitimTuruService.create).toHaveBeenCalledWith(egitimTuru);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<EgitimTuru>>();
      const egitimTuru = { id: 123 };
      jest.spyOn(egitimTuruService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ egitimTuru });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(egitimTuruService.update).toHaveBeenCalledWith(egitimTuru);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
