import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EgitimDerslerComponent } from '../list/egitim-dersler.component';
import { EgitimDerslerDetailComponent } from '../detail/egitim-dersler-detail.component';
import { EgitimDerslerUpdateComponent } from '../update/egitim-dersler-update.component';
import { EgitimDerslerRoutingResolveService } from './egitim-dersler-routing-resolve.service';

const egitimDerslerRoute: Routes = [
  {
    path: '',
    component: EgitimDerslerComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EgitimDerslerDetailComponent,
    resolve: {
      egitimDersler: EgitimDerslerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EgitimDerslerUpdateComponent,
    resolve: {
      egitimDersler: EgitimDerslerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EgitimDerslerUpdateComponent,
    resolve: {
      egitimDersler: EgitimDerslerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(egitimDerslerRoute)],
  exports: [RouterModule],
})
export class EgitimDerslerRoutingModule {}
