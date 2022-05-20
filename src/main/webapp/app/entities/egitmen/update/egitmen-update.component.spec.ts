jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EgitmenService } from '../service/egitmen.service';
import { IEgitmen, Egitmen } from '../egitmen.model';

import { EgitmenUpdateComponent } from './egitmen-update.component';

describe('Egitmen Management Update Component', () => {
  let comp: EgitmenUpdateComponent;
  let fixture: ComponentFixture<EgitmenUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let egitmenService: EgitmenService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EgitmenUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(EgitmenUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EgitmenUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    egitmenService = TestBed.inject(EgitmenService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const egitmen: IEgitmen = { id: 456 };

      activatedRoute.data = of({ egitmen });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(egitmen));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Egitmen>>();
      const egitmen = { id: 123 };
      jest.spyOn(egitmenService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ egitmen });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: egitmen }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(egitmenService.update).toHaveBeenCalledWith(egitmen);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Egitmen>>();
      const egitmen = new Egitmen();
      jest.spyOn(egitmenService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ egitmen });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: egitmen }));
      saveSubject.complete();

      // THEN
      expect(egitmenService.create).toHaveBeenCalledWith(egitmen);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Egitmen>>();
      const egitmen = { id: 123 };
      jest.spyOn(egitmenService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ egitmen });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(egitmenService.update).toHaveBeenCalledWith(egitmen);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
