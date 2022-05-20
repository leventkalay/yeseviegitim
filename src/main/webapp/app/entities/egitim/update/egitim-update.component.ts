import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEgitim, Egitim } from '../egitim.model';
import { EgitimService } from '../service/egitim.service';
import { IKurum } from 'app/entities/kurum/kurum.model';
import { KurumService } from 'app/entities/kurum/service/kurum.service';
import { IEgitimTuru } from 'app/entities/egitim-turu/egitim-turu.model';
import { EgitimTuruService } from 'app/entities/egitim-turu/service/egitim-turu.service';
import { IEgitmen } from 'app/entities/egitmen/egitmen.model';
import { EgitmenService } from 'app/entities/egitmen/service/egitmen.service';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';

@Component({
  selector: 'jhi-egitim-update',
  templateUrl: './egitim-update.component.html',
})
export class EgitimUpdateComponent implements OnInit {
  isSaving = false;

  kurumsSharedCollection: IKurum[] = [];
  egitimTurusSharedCollection: IEgitimTuru[] = [];
  egitmenSharedCollection: IEgitmen[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];

  editForm = this.fb.group({
    id: [],
    egitimBaslik: [],
    egitimAltBaslik: [],
    egitimBaslamaTarihi: [],
    egitimBitisTarihi: [],
    dersSayisi: [],
    egitimSuresi: [],
    egitimYeri: [],
    egitimPuani: [],
    aktif: [],
    kurum: [],
    egitimTuru: [],
    egitmen: [],
    applicationUser: [],
  });

  constructor(
    protected egitimService: EgitimService,
    protected kurumService: KurumService,
    protected egitimTuruService: EgitimTuruService,
    protected egitmenService: EgitmenService,
    protected applicationUserService: ApplicationUserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ egitim }) => {
      this.updateForm(egitim);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const egitim = this.createFromForm();
    if (egitim.id !== undefined) {
      this.subscribeToSaveResponse(this.egitimService.update(egitim));
    } else {
      this.subscribeToSaveResponse(this.egitimService.create(egitim));
    }
  }

  trackKurumById(index: number, item: IKurum): number {
    return item.id!;
  }

  trackEgitimTuruById(index: number, item: IEgitimTuru): number {
    return item.id!;
  }

  trackEgitmenById(index: number, item: IEgitmen): number {
    return item.id!;
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEgitim>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(egitim: IEgitim): void {
    this.editForm.patchValue({
      id: egitim.id,
      egitimBaslik: egitim.egitimBaslik,
      egitimAltBaslik: egitim.egitimAltBaslik,
      egitimBaslamaTarihi: egitim.egitimBaslamaTarihi,
      egitimBitisTarihi: egitim.egitimBitisTarihi,
      dersSayisi: egitim.dersSayisi,
      egitimSuresi: egitim.egitimSuresi,
      egitimYeri: egitim.egitimYeri,
      egitimPuani: egitim.egitimPuani,
      aktif: egitim.aktif,
      kurum: egitim.kurum,
      egitimTuru: egitim.egitimTuru,
      egitmen: egitim.egitmen,
      applicationUser: egitim.applicationUser,
    });

    this.kurumsSharedCollection = this.kurumService.addKurumToCollectionIfMissing(this.kurumsSharedCollection, egitim.kurum);
    this.egitimTurusSharedCollection = this.egitimTuruService.addEgitimTuruToCollectionIfMissing(
      this.egitimTurusSharedCollection,
      egitim.egitimTuru
    );
    this.egitmenSharedCollection = this.egitmenService.addEgitmenToCollectionIfMissing(this.egitmenSharedCollection, egitim.egitmen);
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      egitim.applicationUser
    );
  }

  protected loadRelationshipsOptions(): void {
    this.kurumService
      .query()
      .pipe(map((res: HttpResponse<IKurum[]>) => res.body ?? []))
      .pipe(map((kurums: IKurum[]) => this.kurumService.addKurumToCollectionIfMissing(kurums, this.editForm.get('kurum')!.value)))
      .subscribe((kurums: IKurum[]) => (this.kurumsSharedCollection = kurums));

    this.egitimTuruService
      .query()
      .pipe(map((res: HttpResponse<IEgitimTuru[]>) => res.body ?? []))
      .pipe(
        map((egitimTurus: IEgitimTuru[]) =>
          this.egitimTuruService.addEgitimTuruToCollectionIfMissing(egitimTurus, this.editForm.get('egitimTuru')!.value)
        )
      )
      .subscribe((egitimTurus: IEgitimTuru[]) => (this.egitimTurusSharedCollection = egitimTurus));

    this.egitmenService
      .query()
      .pipe(map((res: HttpResponse<IEgitmen[]>) => res.body ?? []))
      .pipe(map((egitmen: IEgitmen[]) => this.egitmenService.addEgitmenToCollectionIfMissing(egitmen, this.editForm.get('egitmen')!.value)))
      .subscribe((egitmen: IEgitmen[]) => (this.egitmenSharedCollection = egitmen));

    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing(applicationUsers, this.editForm.get('applicationUser')!.value)
        )
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));
  }

  protected createFromForm(): IEgitim {
    return {
      ...new Egitim(),
      id: this.editForm.get(['id'])!.value,
      egitimBaslik: this.editForm.get(['egitimBaslik'])!.value,
      egitimAltBaslik: this.editForm.get(['egitimAltBaslik'])!.value,
      egitimBaslamaTarihi: this.editForm.get(['egitimBaslamaTarihi'])!.value,
      egitimBitisTarihi: this.editForm.get(['egitimBitisTarihi'])!.value,
      dersSayisi: this.editForm.get(['dersSayisi'])!.value,
      egitimSuresi: this.editForm.get(['egitimSuresi'])!.value,
      egitimYeri: this.editForm.get(['egitimYeri'])!.value,
      egitimPuani: this.editForm.get(['egitimPuani'])!.value,
      aktif: this.editForm.get(['aktif'])!.value,
      kurum: this.editForm.get(['kurum'])!.value,
      egitimTuru: this.editForm.get(['egitimTuru'])!.value,
      egitmen: this.editForm.get(['egitmen'])!.value,
      applicationUser: this.editForm.get(['applicationUser'])!.value,
    };
  }
}
