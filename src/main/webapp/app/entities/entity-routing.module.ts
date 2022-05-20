import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'egitim',
        data: { pageTitle: 'egitimyeseviApp.egitim.home.title' },
        loadChildren: () => import('./egitim/egitim.module').then(m => m.EgitimModule),
      },
      {
        path: 'egitim-turu',
        data: { pageTitle: 'egitimyeseviApp.egitimTuru.home.title' },
        loadChildren: () => import('./egitim-turu/egitim-turu.module').then(m => m.EgitimTuruModule),
      },
      {
        path: 'kurum',
        data: { pageTitle: 'egitimyeseviApp.kurum.home.title' },
        loadChildren: () => import('./kurum/kurum.module').then(m => m.KurumModule),
      },
      {
        path: 'egitmen',
        data: { pageTitle: 'egitimyeseviApp.egitmen.home.title' },
        loadChildren: () => import('./egitmen/egitmen.module').then(m => m.EgitmenModule),
      },
      {
        path: 'ogrenci-egitimler',
        data: { pageTitle: 'egitimyeseviApp.ogrenciEgitimler.home.title' },
        loadChildren: () => import('./ogrenci-egitimler/ogrenci-egitimler.module').then(m => m.OgrenciEgitimlerModule),
      },
      {
        path: 'duyuru',
        data: { pageTitle: 'egitimyeseviApp.duyuru.home.title' },
        loadChildren: () => import('./duyuru/duyuru.module').then(m => m.DuyuruModule),
      },
      {
        path: 'ders',
        data: { pageTitle: 'egitimyeseviApp.ders.home.title' },
        loadChildren: () => import('./ders/ders.module').then(m => m.DersModule),
      },
      {
        path: 'egitim-dersler',
        data: { pageTitle: 'egitimyeseviApp.egitimDersler.home.title' },
        loadChildren: () => import('./egitim-dersler/egitim-dersler.module').then(m => m.EgitimDerslerModule),
      },
      {
        path: 'takvim',
        data: { pageTitle: 'egitimyeseviApp.takvim.home.title' },
        loadChildren: () => import('./takvim/takvim.module').then(m => m.TakvimModule),
      },
      {
        path: 'application-user',
        data: { pageTitle: 'egitimyeseviApp.applicationUser.home.title' },
        loadChildren: () => import('./application-user/application-user.module').then(m => m.ApplicationUserModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
