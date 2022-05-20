import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEgitimDersler } from '../egitim-dersler.model';

@Component({
  selector: 'jhi-egitim-dersler-detail',
  templateUrl: './egitim-dersler-detail.component.html',
})
export class EgitimDerslerDetailComponent implements OnInit {
  egitimDersler: IEgitimDersler | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ egitimDersler }) => {
      this.egitimDersler = egitimDersler;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
