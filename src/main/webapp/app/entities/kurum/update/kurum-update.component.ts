import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IKurum, Kurum } from '../kurum.model';
import { KurumService } from '../service/kurum.service';

@Component({
  selector: 'jhi-kurum-update',
  templateUrl: './kurum-update.component.html',
})
export class KurumUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    adi: [],
    aciklama: [],
    aktif: [],
  });

  constructor(protected kurumService: KurumService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ kurum }) => {
      this.updateForm(kurum);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const kurum = this.createFromForm();
    if (kurum.id !== undefined) {
      this.subscribeToSaveResponse(this.kurumService.update(kurum));
    } else {
      this.subscribeToSaveResponse(this.kurumService.create(kurum));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKurum>>): void {
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

  protected updateForm(kurum: IKurum): void {
    this.editForm.patchValue({
      id: kurum.id,
      adi: kurum.adi,
      aciklama: kurum.aciklama,
      aktif: kurum.aktif,
    });
  }

  protected createFromForm(): IKurum {
    return {
      ...new Kurum(),
      id: this.editForm.get(['id'])!.value,
      adi: this.editForm.get(['adi'])!.value,
      aciklama: this.editForm.get(['aciklama'])!.value,
      aktif: this.editForm.get(['aktif'])!.value,
    };
  }
}
