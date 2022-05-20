import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEgitmen } from '../egitmen.model';

@Component({
  selector: 'jhi-egitmen-detail',
  templateUrl: './egitmen-detail.component.html',
})
export class EgitmenDetailComponent implements OnInit {
  egitmen: IEgitmen | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ egitmen }) => {
      this.egitmen = egitmen;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
