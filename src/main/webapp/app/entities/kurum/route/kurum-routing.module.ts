import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { KurumComponent } from '../list/kurum.component';
import { KurumDetailComponent } from '../detail/kurum-detail.component';
import { KurumUpdateComponent } from '../update/kurum-update.component';
import { KurumRoutingResolveService } from './kurum-routing-resolve.service';

const kurumRoute: Routes = [
  {
    path: '',
    component: KurumComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: KurumDetailComponent,
    resolve: {
      kurum: KurumRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: KurumUpdateComponent,
    resolve: {
      kurum: KurumRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: KurumUpdateComponent,
    resolve: {
      kurum: KurumRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(kurumRoute)],
  exports: [RouterModule],
})
export class KurumRoutingModule {}
