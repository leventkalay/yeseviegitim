import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IKurum } from '../kurum.model';

@Component({
  selector: 'jhi-kurum-detail',
  templateUrl: './kurum-detail.component.html',
})
export class KurumDetailComponent implements OnInit {
  kurum: IKurum | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ kurum }) => {
      this.kurum = kurum;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
