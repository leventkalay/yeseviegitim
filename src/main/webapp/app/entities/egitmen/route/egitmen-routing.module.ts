import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EgitmenComponent } from '../list/egitmen.component';
import { EgitmenDetailComponent } from '../detail/egitmen-detail.component';
import { EgitmenUpdateComponent } from '../update/egitmen-update.component';
import { EgitmenRoutingResolveService } from './egitmen-routing-resolve.service';

const egitmenRoute: Routes = [
  {
    path: '',
    component: EgitmenComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EgitmenDetailComponent,
    resolve: {
      egitmen: EgitmenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EgitmenUpdateComponent,
    resolve: {
      egitmen: EgitmenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EgitmenUpdateComponent,
    resolve: {
      egitmen: EgitmenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(egitmenRoute)],
  exports: [RouterModule],
})
export class EgitmenRoutingModule {}
