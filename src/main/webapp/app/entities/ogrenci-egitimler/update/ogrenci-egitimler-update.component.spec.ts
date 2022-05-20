jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OgrenciEgitimlerService } from '../service/ogrenci-egitimler.service';
import { IOgrenciEgitimler, OgrenciEgitimler } from '../ogrenci-egitimler.model';
import { IEgitim } from 'app/entities/egitim/egitim.model';
import { EgitimService } from 'app/entities/egitim/service/egitim.service';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';

import { OgrenciEgitimlerUpdateComponent } from './ogrenci-egitimler-update.component';

describe('OgrenciEgitimler Management Update Component', () => {
  let comp: OgrenciEgitimlerUpdateComponent;
  let fixture: ComponentFixture<OgrenciEgitimlerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ogrenciEgitimlerService: OgrenciEgitimlerService;
  let egitimService: EgitimService;
  let applicationUserService: ApplicationUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [OgrenciEgitimlerUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(OgrenciEgitimlerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OgrenciEgitimlerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ogrenciEgitimlerService = TestBed.inject(OgrenciEgitimlerService);
    egitimService = TestBed.inject(EgitimService);
    applicationUserService = TestBed.inject(ApplicationUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Egitim query and add missing value', () => {
      const ogrenciEgitimler: IOgrenciEgitimler = { id: 456 };
      const egitim: IEgitim = { id: 47890 };
      ogrenciEgitimler.egitim = egitim;

      const egitimCollection: IEgitim[] = [{ id: 26205 }];
      jest.spyOn(egitimService, 'query').mockReturnValue(of(new HttpResponse({ body: egitimCollection })));
      const additionalEgitims = [egitim];
      const expectedCollection: IEgitim[] = [...additionalEgitims, ...egitimCollection];
      jest.spyOn(egitimService, 'addEgitimToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ogrenciEgitimler });
      comp.ngOnInit();

      expect(egitimService.query).toHaveBeenCalled();
      expect(egitimService.addEgitimToCollectionIfMissing).toHaveBeenCalledWith(egitimCollection, ...additionalEgitims);
      expect(comp.egitimsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const ogrenciEgitimler: IOgrenciEgitimler = { id: 456 };
      const kullanici: IApplicationUser = { id: 71630 };
      ogrenciEgitimler.kullanici = kullanici;

      const applicationUserCollection: IApplicationUser[] = [{ id: 97891 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [kullanici];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ogrenciEgitimler });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ogrenciEgitimler: IOgrenciEgitimler = { id: 456 };
      const egitim: IEgitim = { id: 93458 };
      ogrenciEgitimler.egitim = egitim;
      const kullanici: IApplicationUser = { id: 39119 };
      ogrenciEgitimler.kullanici = kullanici;

      activatedRoute.data = of({ ogrenciEgitimler });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(ogrenciEgitimler));
      expect(comp.egitimsSharedCollection).toContain(egitim);
      expect(comp.applicationUsersSharedCollection).toContain(kullanici);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OgrenciEgitimler>>();
      const ogrenciEgitimler = { id: 123 };
      jest.spyOn(ogrenciEgitimlerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ogrenciEgitimler });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ogrenciEgitimler }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(ogrenciEgitimlerService.update).toHaveBeenCalledWith(ogrenciEgitimler);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OgrenciEgitimler>>();
      const ogrenciEgitimler = new OgrenciEgitimler();
      jest.spyOn(ogrenciEgitimlerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ogrenciEgitimler });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ogrenciEgitimler }));
      saveSubject.complete();

      // THEN
      expect(ogrenciEgitimlerService.create).toHaveBeenCalledWith(ogrenciEgitimler);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OgrenciEgitimler>>();
      const ogrenciEgitimler = { id: 123 };
      jest.spyOn(ogrenciEgitimlerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ogrenciEgitimler });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ogrenciEgitimlerService.update).toHaveBeenCalledWith(ogrenciEgitimler);
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
