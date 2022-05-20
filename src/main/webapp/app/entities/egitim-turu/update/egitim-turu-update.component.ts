import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IEgitimTuru, EgitimTuru } from '../egitim-turu.model';
import { EgitimTuruService } from '../service/egitim-turu.service';

@Component({
  selector: 'jhi-egitim-turu-update',
  templateUrl: './egitim-turu-update.component.html',
})
export class EgitimTuruUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    adi: [],
    aciklama: [],
    aktif: [],
  });

  constructor(protected egitimTuruService: EgitimTuruService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ egitimTuru }) => {
      this.updateForm(egitimTuru);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const egitimTuru = this.createFromForm();
    if (egitimTuru.id !== undefined) {
      this.subscribeToSaveResponse(this.egitimTuruService.update(egitimTuru));
    } else {
      this.subscribeToSaveResponse(this.egitimTuruService.create(egitimTuru));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEgitimTuru>>): void {
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

  protected updateForm(egitimTuru: IEgitimTuru): void {
    this.editForm.patchValue({
      id: egitimTuru.id,
      adi: egitimTuru.adi,
      aciklama: egitimTuru.aciklama,
      aktif: egitimTuru.aktif,
    });
  }

  protected createFromForm(): IEgitimTuru {
    return {
      ...new EgitimTuru(),
      id: this.editForm.get(['id'])!.value,
      adi: this.editForm.get(['adi'])!.value,
      aciklama: this.editForm.get(['aciklama'])!.value,
      aktif: this.editForm.get(['aktif'])!.value,
    };
  }
}
