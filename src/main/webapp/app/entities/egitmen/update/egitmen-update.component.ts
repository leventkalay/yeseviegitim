import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IEgitmen, Egitmen } from '../egitmen.model';
import { EgitmenService } from '../service/egitmen.service';

@Component({
  selector: 'jhi-egitmen-update',
  templateUrl: './egitmen-update.component.html',
})
export class EgitmenUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    adiSoyadi: [],
    unvani: [],
    puani: [],
    aktif: [],
  });

  constructor(protected egitmenService: EgitmenService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ egitmen }) => {
      this.updateForm(egitmen);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const egitmen = this.createFromForm();
    if (egitmen.id !== undefined) {
      this.subscribeToSaveResponse(this.egitmenService.update(egitmen));
    } else {
      this.subscribeToSaveResponse(this.egitmenService.create(egitmen));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEgitmen>>): void {
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

  protected updateForm(egitmen: IEgitmen): void {
    this.editForm.patchValue({
      id: egitmen.id,
      adiSoyadi: egitmen.adiSoyadi,
      unvani: egitmen.unvani,
      puani: egitmen.puani,
      aktif: egitmen.aktif,
    });
  }

  protected createFromForm(): IEgitmen {
    return {
      ...new Egitmen(),
      id: this.editForm.get(['id'])!.value,
      adiSoyadi: this.editForm.get(['adiSoyadi'])!.value,
      unvani: this.editForm.get(['unvani'])!.value,
      puani: this.editForm.get(['puani'])!.value,
      aktif: this.editForm.get(['aktif'])!.value,
    };
  }
}
