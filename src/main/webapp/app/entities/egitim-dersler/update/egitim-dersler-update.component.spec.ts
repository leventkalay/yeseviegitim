jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EgitimDerslerService } from '../service/egitim-dersler.service';
import { IEgitimDersler, EgitimDersler } from '../egitim-dersler.model';
import { IEgitim } from 'app/entities/egitim/egitim.model';
import { EgitimService } from 'app/entities/egitim/service/egitim.service';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';

import { EgitimDerslerUpdateComponent } from './egitim-dersler-update.component';

describe('EgitimDersler Management Update Component', () => {
  let comp: EgitimDerslerUpdateComponent;
  let fixture: ComponentFixture<EgitimDerslerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let egitimDerslerService: EgitimDerslerService;
  let egitimService: EgitimService;
  let applicationUserService: ApplicationUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EgitimDerslerUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(EgitimDerslerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EgitimDerslerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    egitimDerslerService = TestBed.inject(EgitimDerslerService);
    egitimService = TestBed.inject(EgitimService);
    applicationUserService = TestBed.inject(ApplicationUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Egitim query and add missing value', () => {
      const egitimDersler: IEgitimDersler = { id: 456 };
      const egitim: IEgitim = { id: 69074 };
      egitimDersler.egitim = egitim;

      const egitimCollection: IEgitim[] = [{ id: 42728 }];
      jest.spyOn(egitimService, 'query').mockReturnValue(of(new HttpResponse({ body: egitimCollection })));
      const additionalEgitims = [egitim];
      const expectedCollection: IEgitim[] = [...additionalEgitims, ...egitimCollection];
      jest.spyOn(egitimService, 'addEgitimToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ egitimDersler });
      comp.ngOnInit();

      expect(egitimService.query).toHaveBeenCalled();
      expect(egitimService.addEgitimToCollectionIfMissing).toHaveBeenCalledWith(egitimCollection, ...additionalEgitims);
      expect(comp.egitimsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const egitimDersler: IEgitimDersler = { id: 456 };
      const kullanici: IApplicationUser = { id: 25857 };
      egitimDersler.kullanici = kullanici;

      const applicationUserCollection: IApplicationUser[] = [{ id: 12351 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [kullanici];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ egitimDersler });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const egitimDersler: IEgitimDersler = { id: 456 };
      const egitim: IEgitim = { id: 60713 };
      egitimDersler.egitim = egitim;
      const kullanici: IApplicationUser = { id: 14372 };
      egitimDersler.kullanici = kullanici;

      activatedRoute.data = of({ egitimDersler });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(egitimDersler));
      expect(comp.egitimsSharedCollection).toContain(egitim);
      expect(comp.applicationUsersSharedCollection).toContain(kullanici);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<EgitimDersler>>();
      const egitimDersler = { id: 123 };
      jest.spyOn(egitimDerslerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ egitimDersler });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: egitimDersler }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(egitimDerslerService.update).toHaveBeenCalledWith(egitimDersler);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<EgitimDersler>>();
      const egitimDersler = new EgitimDersler();
      jest.spyOn(egitimDerslerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ egitimDersler });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: egitimDersler }));
      saveSubject.complete();

      // THEN
      expect(egitimDerslerService.create).toHaveBeenCalledWith(egitimDersler);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<EgitimDersler>>();
      const egitimDersler = { id: 123 };
      jest.spyOn(egitimDerslerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ egitimDersler });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(egitimDerslerService.update).toHaveBeenCalledWith(egitimDersler);
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

    describe('trackApplicationUserById', () => {
      it('Should return tracked ApplicationUser primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackApplicationUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
