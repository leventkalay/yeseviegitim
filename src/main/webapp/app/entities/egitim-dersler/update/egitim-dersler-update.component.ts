import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEgitimDersler, EgitimDersler } from '../egitim-dersler.model';
import { EgitimDerslerService } from '../service/egitim-dersler.service';
import { IEgitim } from 'app/entities/egitim/egitim.model';
import { EgitimService } from 'app/entities/egitim/service/egitim.service';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';

@Component({
  selector: 'jhi-egitim-dersler-update',
  templateUrl: './egitim-dersler-update.component.html',
})
export class EgitimDerslerUpdateComponent implements OnInit {
  isSaving = false;

  egitimsSharedCollection: IEgitim[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];

  editForm = this.fb.group({
    id: [],
    egitim: [],
    kullanici: [],
  });

  constructor(
    protected egitimDerslerService: EgitimDerslerService,
    protected egitimService: EgitimService,
    protected applicationUserService: ApplicationUserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ egitimDersler }) => {
      this.updateForm(egitimDersler);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const egitimDersler = this.createFromForm();
    if (egitimDersler.id !== undefined) {
      this.subscribeToSaveResponse(this.egitimDerslerService.update(egitimDersler));
    } else {
      this.subscribeToSaveResponse(this.egitimDerslerService.create(egitimDersler));
    }
  }

  trackEgitimById(index: number, item: IEgitim): number {
    return item.id!;
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEgitimDersler>>): void {
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

  protected updateForm(egitimDersler: IEgitimDersler): void {
    this.editForm.patchValue({
      id: egitimDersler.id,
      egitim: egitimDersler.egitim,
      kullanici: egitimDersler.kullanici,
    });

    this.egitimsSharedCollection = this.egitimService.addEgitimToCollectionIfMissing(this.egitimsSharedCollection, egitimDersler.egitim);
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      egitimDersler.kullanici
    );
  }

  protected loadRelationshipsOptions(): void {
    this.egitimService
      .query()
      .pipe(map((res: HttpResponse<IEgitim[]>) => res.body ?? []))
      .pipe(map((egitims: IEgitim[]) => this.egitimService.addEgitimToCollectionIfMissing(egitims, this.editForm.get('egitim')!.value)))
      .subscribe((egitims: IEgitim[]) => (this.egitimsSharedCollection = egitims));

    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing(applicationUsers, this.editForm.get('kullanici')!.value)
        )
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));
  }

  protected createFromForm(): IEgitimDersler {
    return {
      ...new EgitimDersler(),
      id: this.editForm.get(['id'])!.value,
      egitim: this.editForm.get(['egitim'])!.value,
      kullanici: this.editForm.get(['kullanici'])!.value,
    };
  }
}
