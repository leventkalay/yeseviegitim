import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOgrenciEgitimler } from '../ogrenci-egitimler.model';

@Component({
  selector: 'jhi-ogrenci-egitimler-detail',
  templateUrl: './ogrenci-egitimler-detail.component.html',
})
export class OgrenciEgitimlerDetailComponent implements OnInit {
  ogrenciEgitimler: IOgrenciEgitimler | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ogrenciEgitimler }) => {
      this.ogrenciEgitimler = ogrenciEgitimler;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
