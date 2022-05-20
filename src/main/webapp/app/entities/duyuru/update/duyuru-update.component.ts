import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDuyuru, Duyuru } from '../duyuru.model';
import { DuyuruService } from '../service/duyuru.service';
import { IEgitim } from 'app/entities/egitim/egitim.model';
import { EgitimService } from 'app/entities/egitim/service/egitim.service';

@Component({
  selector: 'jhi-duyuru-update',
  templateUrl: './duyuru-update.component.html',
})
export class DuyuruUpdateComponent implements OnInit {
  isSaving = false;

  egitimsSharedCollection: IEgitim[] = [];

  editForm = this.fb.group({
    id: [],
    duyuruBaslik: [null, [Validators.required]],
    duyuruIcerik: [null, [Validators.required]],
    duyuruBaslamaTarihi: [null, [Validators.required]],
    duyuruBitisTarihi: [null, [Validators.required]],
    egitim: [],
  });

  constructor(
    protected duyuruService: DuyuruService,
    protected egitimService: EgitimService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ duyuru }) => {
      this.updateForm(duyuru);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const duyuru = this.createFromForm();
    if (duyuru.id !== undefined) {
      this.subscribeToSaveResponse(this.duyuruService.update(duyuru));
    } else {
      this.subscribeToSaveResponse(this.duyuruService.create(duyuru));
    }
  }

  trackEgitimById(index: number, item: IEgitim): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDuyuru>>): void {
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

  protected updateForm(duyuru: IDuyuru): void {
    this.editForm.patchValue({
      id: duyuru.id,
      duyuruBaslik: duyuru.duyuruBaslik,
      duyuruIcerik: duyuru.duyuruIcerik,
      duyuruBaslamaTarihi: duyuru.duyuruBaslamaTarihi,
      duyuruBitisTarihi: duyuru.duyuruBitisTarihi,
      egitim: duyuru.egitim,
    });

    this.egitimsSharedCollection = this.egitimService.addEgitimToCollectionIfMissing(this.egitimsSharedCollection, duyuru.egitim);
  }

  protected loadRelationshipsOptions(): void {
    this.egitimService
      .query()
      .pipe(map((res: HttpResponse<IEgitim[]>) => res.body ?? []))
      .pipe(map((egitims: IEgitim[]) => this.egitimService.addEgitimToCollectionIfMissing(egitims, this.editForm.get('egitim')!.value)))
      .subscribe((egitims: IEgitim[]) => (this.egitimsSharedCollection = egitims));
  }

  protected createFromForm(): IDuyuru {
    return {
      ...new Duyuru(),
      id: this.editForm.get(['id'])!.value,
      duyuruBaslik: this.editForm.get(['duyuruBaslik'])!.value,
      duyuruIcerik: this.editForm.get(['duyuruIcerik'])!.value,
      duyuruBaslamaTarihi: this.editForm.get(['duyuruBaslamaTarihi'])!.value,
      duyuruBitisTarihi: this.editForm.get(['duyuruBitisTarihi'])!.value,
      egitim: this.editForm.get(['egitim'])!.value,
    };
  }
}
