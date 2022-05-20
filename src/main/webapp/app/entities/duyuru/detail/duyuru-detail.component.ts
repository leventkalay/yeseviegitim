import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDuyuru } from '../duyuru.model';

@Component({
  selector: 'jhi-duyuru-detail',
  templateUrl: './duyuru-detail.component.html',
})
export class DuyuruDetailComponent implements OnInit {
  duyuru: IDuyuru | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ duyuru }) => {
      this.duyuru = duyuru;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
