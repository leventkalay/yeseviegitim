jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { KurumService } from '../service/kurum.service';
import { IKurum, Kurum } from '../kurum.model';

import { KurumUpdateComponent } from './kurum-update.component';

describe('Kurum Management Update Component', () => {
  let comp: KurumUpdateComponent;
  let fixture: ComponentFixture<KurumUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let kurumService: KurumService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [KurumUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(KurumUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(KurumUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    kurumService = TestBed.inject(KurumService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const kurum: IKurum = { id: 456 };

      activatedRoute.data = of({ kurum });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(kurum));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Kurum>>();
      const kurum = { id: 123 };
      jest.spyOn(kurumService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kurum });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: kurum }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(kurumService.update).toHaveBeenCalledWith(kurum);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Kurum>>();
      const kurum = new Kurum();
      jest.spyOn(kurumService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kurum });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: kurum }));
      saveSubject.complete();

      // THEN
      expect(kurumService.create).toHaveBeenCalledWith(kurum);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Kurum>>();
      const kurum = { id: 123 };
      jest.spyOn(kurumService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kurum });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(kurumService.update).toHaveBeenCalledWith(kurum);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
