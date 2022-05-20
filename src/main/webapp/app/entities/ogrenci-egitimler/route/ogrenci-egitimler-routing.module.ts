import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OgrenciEgitimlerComponent } from '../list/ogrenci-egitimler.component';
import { OgrenciEgitimlerDetailComponent } from '../detail/ogrenci-egitimler-detail.component';
import { OgrenciEgitimlerUpdateComponent } from '../update/ogrenci-egitimler-update.component';
import { OgrenciEgitimlerRoutingResolveService } from './ogrenci-egitimler-routing-resolve.service';

const ogrenciEgitimlerRoute: Routes = [
  {
    path: '',
    component: OgrenciEgitimlerComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OgrenciEgitimlerDetailComponent,
    resolve: {
      ogrenciEgitimler: OgrenciEgitimlerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OgrenciEgitimlerUpdateComponent,
    resolve: {
      ogrenciEgitimler: OgrenciEgitimlerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OgrenciEgitimlerUpdateComponent,
    resolve: {
      ogrenciEgitimler: OgrenciEgitimlerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ogrenciEgitimlerRoute)],
  exports: [RouterModule],
})
export class OgrenciEgitimlerRoutingModule {}
