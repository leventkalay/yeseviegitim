jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EgitimService } from '../service/egitim.service';
import { IEgitim, Egitim } from '../egitim.model';
import { IKurum } from 'app/entities/kurum/kurum.model';
import { KurumService } from 'app/entities/kurum/service/kurum.service';
import { IEgitimTuru } from 'app/entities/egitim-turu/egitim-turu.model';
import { EgitimTuruService } from 'app/entities/egitim-turu/service/egitim-turu.service';
import { IEgitmen } from 'app/entities/egitmen/egitmen.model';
import { EgitmenService } from 'app/entities/egitmen/service/egitmen.service';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';

import { EgitimUpdateComponent } from './egitim-update.component';

describe('Egitim Management Update Component', () => {
  let comp: EgitimUpdateComponent;
  let fixture: ComponentFixture<EgitimUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let egitimService: EgitimService;
  let kurumService: KurumService;
  let egitimTuruService: EgitimTuruService;
  let egitmenService: EgitmenService;
  let applicationUserService: ApplicationUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EgitimUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(EgitimUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EgitimUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    egitimService = TestBed.inject(EgitimService);
    kurumService = TestBed.inject(KurumService);
    egitimTuruService = TestBed.inject(EgitimTuruService);
    egitmenService = TestBed.inject(EgitmenService);
    applicationUserService = TestBed.inject(ApplicationUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Kurum query and add missing value', () => {
      const egitim: IEgitim = { id: 456 };
      const kurum: IKurum = { id: 55857 };
      egitim.kurum = kurum;

      const kurumCollection: IKurum[] = [{ id: 84907 }];
      jest.spyOn(kurumService, 'query').mockReturnValue(of(new HttpResponse({ body: kurumCollection })));
      const additionalKurums = [kurum];
      const expectedCollection: IKurum[] = [...additionalKurums, ...kurumCollection];
      jest.spyOn(kurumService, 'addKurumToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ egitim });
      comp.ngOnInit();

      expect(kurumService.query).toHaveBeenCalled();
      expect(kurumService.addKurumToCollectionIfMissing).toHaveBeenCalledWith(kurumCollection, ...additionalKurums);
      expect(comp.kurumsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call EgitimTuru query and add missing value', () => {
      const egitim: IEgitim = { id: 456 };
      const egitimTuru: IEgitimTuru = { id: 4399 };
      egitim.egitimTuru = egitimTuru;

      const egitimTuruCollection: IEgitimTuru[] = [{ id: 96565 }];
      jest.spyOn(egitimTuruService, 'query').mockReturnValue(of(new HttpResponse({ body: egitimTuruCollection })));
      const additionalEgitimTurus = [egitimTuru];
      const expectedCollection: IEgitimTuru[] = [...additionalEgitimTurus, ...egitimTuruCollection];
      jest.spyOn(egitimTuruService, 'addEgitimTuruToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ egitim });
      comp.ngOnInit();

      expect(egitimTuruService.query).toHaveBeenCalled();
      expect(egitimTuruService.addEgitimTuruToCollectionIfMissing).toHaveBeenCalledWith(egitimTuruCollection, ...additionalEgitimTurus);
      expect(comp.egitimTurusSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Egitmen query and add missing value', () => {
      const egitim: IEgitim = { id: 456 };
      const egitmen: IEgitmen = { id: 62245 };
      egitim.egitmen = egitmen;

      const egitmenCollection: IEgitmen[] = [{ id: 17922 }];
      jest.spyOn(egitmenService, 'query').mockReturnValue(of(new HttpResponse({ body: egitmenCollection })));
      const additionalEgitmen = [egitmen];
      const expectedCollection: IEgitmen[] = [...additionalEgitmen, ...egitmenCollection];
      jest.spyOn(egitmenService, 'addEgitmenToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ egitim });
      comp.ngOnInit();

      expect(egitmenService.query).toHaveBeenCalled();
      expect(egitmenService.addEgitmenToCollectionIfMissing).toHaveBeenCalledWith(egitmenCollection, ...additionalEgitmen);
      expect(comp.egitmenSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const egitim: IEgitim = { id: 456 };
      const applicationUser: IApplicationUser = { id: 91314 };
      egitim.applicationUser = applicationUser;

      const applicationUserCollection: IApplicationUser[] = [{ id: 33235 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [applicationUser];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ egitim });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const egitim: IEgitim = { id: 456 };
      const kurum: IKurum = { id: 9844 };
      egitim.kurum = kurum;
      const egitimTuru: IEgitimTuru = { id: 18545 };
      egitim.egitimTuru = egitimTuru;
      const egitmen: IEgitmen = { id: 45903 };
      egitim.egitmen = egitmen;
      const applicationUser: IApplicationUser = { id: 25076 };
      egitim.applicationUser = applicationUser;

      activatedRoute.data = of({ egitim });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(egitim));
      expect(comp.kurumsSharedCollection).toContain(kurum);
      expect(comp.egitimTurusSharedCollection).toContain(egitimTuru);
      expect(comp.egitmenSharedCollection).toContain(egitmen);
      expect(comp.applicationUsersSharedCollection).toContain(applicationUser);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Egitim>>();
      const egitim = { id: 123 };
      jest.spyOn(egitimService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ egitim });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: egitim }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(egitimService.update).toHaveBeenCalledWith(egitim);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Egitim>>();
      const egitim = new Egitim();
      jest.spyOn(egitimService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ egitim });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: egitim }));
      saveSubject.complete();

      // THEN
      expect(egitimService.create).toHaveBeenCalledWith(egitim);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Egitim>>();
      const egitim = { id: 123 };
      jest.spyOn(egitimService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ egitim });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(egitimService.update).toHaveBeenCalledWith(egitim);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackKurumById', () => {
      it('Should return tracked Kurum primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackKurumById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackEgitimTuruById', () => {
      it('Should return tracked EgitimTuru primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEgitimTuruById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackEgitmenById', () => {
      it('Should return tracked Egitmen primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEgitmenById(0, entity);
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
