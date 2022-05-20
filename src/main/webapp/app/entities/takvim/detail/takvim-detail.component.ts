import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITakvim } from '../takvim.model';

@Component({
  selector: 'jhi-takvim-detail',
  templateUrl: './takvim-detail.component.html',
})
export class TakvimDetailComponent implements OnInit {
  takvim: ITakvim | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ takvim }) => {
      this.takvim = takvim;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
