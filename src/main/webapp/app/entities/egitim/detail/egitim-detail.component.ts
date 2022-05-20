import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEgitim } from '../egitim.model';

@Component({
  selector: 'jhi-egitim-detail',
  templateUrl: './egitim-detail.component.html',
})
export class EgitimDetailComponent implements OnInit {
  egitim: IEgitim | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ egitim }) => {
      this.egitim = egitim;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
