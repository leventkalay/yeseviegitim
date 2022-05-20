import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITakvim, Takvim } from '../takvim.model';
import { TakvimService } from '../service/takvim.service';
import { IEgitim } from 'app/entities/egitim/egitim.model';
import { EgitimService } from 'app/entities/egitim/service/egitim.service';

@Component({
  selector: 'jhi-takvim-update',
  templateUrl: './takvim-update.component.html',
})
export class TakvimUpdateComponent implements OnInit {
  isSaving = false;

  egitimsSharedCollection: IEgitim[] = [];

  editForm = this.fb.group({
    id: [],
    adi: [],
    egitim: [],
  });

  constructor(
    protected takvimService: TakvimService,
    protected egitimService: EgitimService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ takvim }) => {
      this.updateForm(takvim);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const takvim = this.createFromForm();
    if (takvim.id !== undefined) {
      this.subscribeToSaveResponse(this.takvimService.update(takvim));
    } else {
      this.subscribeToSaveResponse(this.takvimService.create(takvim));
    }
  }

  trackEgitimById(index: number, item: IEgitim): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITakvim>>): void {
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

  protected updateForm(takvim: ITakvim): void {
    this.editForm.patchValue({
      id: takvim.id,
      adi: takvim.adi,
      egitim: takvim.egitim,
    });

    this.egitimsSharedCollection = this.egitimService.addEgitimToCollectionIfMissing(this.egitimsSharedCollection, takvim.egitim);
  }

  protected loadRelationshipsOptions(): void {
    this.egitimService
      .query()
      .pipe(map((res: HttpResponse<IEgitim[]>) => res.body ?? []))
      .pipe(map((egitims: IEgitim[]) => this.egitimService.addEgitimToCollectionIfMissing(egitims, this.editForm.get('egitim')!.value)))
      .subscribe((egitims: IEgitim[]) => (this.egitimsSharedCollection = egitims));
  }

  protected createFromForm(): ITakvim {
    return {
      ...new Takvim(),
      id: this.editForm.get(['id'])!.value,
      adi: this.editForm.get(['adi'])!.value,
      egitim: this.editForm.get(['egitim'])!.value,
    };
  }
}
