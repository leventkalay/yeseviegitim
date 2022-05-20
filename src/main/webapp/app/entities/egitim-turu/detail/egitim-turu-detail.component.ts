import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEgitimTuru } from '../egitim-turu.model';

@Component({
  selector: 'jhi-egitim-turu-detail',
  templateUrl: './egitim-turu-detail.component.html',
})
export class EgitimTuruDetailComponent implements OnInit {
  egitimTuru: IEgitimTuru | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ egitimTuru }) => {
      this.egitimTuru = egitimTuru;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
