import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TakvimComponent } from '../list/takvim.component';
import { TakvimDetailComponent } from '../detail/takvim-detail.component';
import { TakvimUpdateComponent } from '../update/takvim-update.component';
import { TakvimRoutingResolveService } from './takvim-routing-resolve.service';

const takvimRoute: Routes = [
  {
    path: '',
    component: TakvimComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TakvimDetailComponent,
    resolve: {
      takvim: TakvimRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TakvimUpdateComponent,
    resolve: {
      takvim: TakvimRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TakvimUpdateComponent,
    resolve: {
      takvim: TakvimRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(takvimRoute)],
  exports: [RouterModule],
})
export class TakvimRoutingModule {}
