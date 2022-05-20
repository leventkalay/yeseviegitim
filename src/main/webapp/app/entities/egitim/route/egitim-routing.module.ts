import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EgitimComponent } from '../list/egitim.component';
import { EgitimDetailComponent } from '../detail/egitim-detail.component';
import { EgitimUpdateComponent } from '../update/egitim-update.component';
import { EgitimRoutingResolveService } from './egitim-routing-resolve.service';

const egitimRoute: Routes = [
  {
    path: '',
    component: EgitimComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EgitimDetailComponent,
    resolve: {
      egitim: EgitimRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EgitimUpdateComponent,
    resolve: {
      egitim: EgitimRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EgitimUpdateComponent,
    resolve: {
      egitim: EgitimRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(egitimRoute)],
  exports: [RouterModule],
})
export class EgitimRoutingModule {}
