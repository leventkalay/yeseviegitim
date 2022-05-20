import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDers, Ders } from '../ders.model';
import { DersService } from '../service/ders.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IEgitim } from 'app/entities/egitim/egitim.model';
import { EgitimService } from 'app/entities/egitim/service/egitim.service';

@Component({
  selector: 'jhi-ders-update',
  templateUrl: './ders-update.component.html',
})
export class DersUpdateComponent implements OnInit {
  isSaving = false;

  egitimsSharedCollection: IEgitim[] = [];

  editForm = this.fb.group({
    id: [],
    adi: [],
    aciklama: [],
    dersLinki: [],
    dersVideosu: [],
    dersVideosuContentType: [],
    egitim: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected dersService: DersService,
    protected egitimService: EgitimService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ders }) => {
      this.updateForm(ders);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('egitimyeseviApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ders = this.createFromForm();
    if (ders.id !== undefined) {
      this.subscribeToSaveResponse(this.dersService.update(ders));
    } else {
      this.subscribeToSaveResponse(this.dersService.create(ders));
    }
  }

  trackEgitimById(index: number, item: IEgitim): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDers>>): void {
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

  protected updateForm(ders: IDers): void {
    this.editForm.patchValue({
      id: ders.id,
      adi: ders.adi,
      aciklama: ders.aciklama,
      dersLinki: ders.dersLinki,
      dersVideosu: ders.dersVideosu,
      dersVideosuContentType: ders.dersVideosuContentType,
      egitim: ders.egitim,
    });

    this.egitimsSharedCollection = this.egitimService.addEgitimToCollectionIfMissing(this.egitimsSharedCollection, ders.egitim);
  }

  protected loadRelationshipsOptions(): void {
    this.egitimService
      .query()
      .pipe(map((res: HttpResponse<IEgitim[]>) => res.body ?? []))
      .pipe(map((egitims: IEgitim[]) => this.egitimService.addEgitimToCollectionIfMissing(egitims, this.editForm.get('egitim')!.value)))
      .subscribe((egitims: IEgitim[]) => (this.egitimsSharedCollection = egitims));
  }

  protected createFromForm(): IDers {
    return {
      ...new Ders(),
      id: this.editForm.get(['id'])!.value,
      adi: this.editForm.get(['adi'])!.value,
      aciklama: this.editForm.get(['aciklama'])!.value,
      dersLinki: this.editForm.get(['dersLinki'])!.value,
      dersVideosuContentType: this.editForm.get(['dersVideosuContentType'])!.value,
      dersVideosu: this.editForm.get(['dersVideosu'])!.value,
      egitim: this.editForm.get(['egitim'])!.value,
    };
  }
}
