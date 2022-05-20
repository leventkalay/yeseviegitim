import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DuyuruComponent } from '../list/duyuru.component';
import { DuyuruDetailComponent } from '../detail/duyuru-detail.component';
import { DuyuruUpdateComponent } from '../update/duyuru-update.component';
import { DuyuruRoutingResolveService } from './duyuru-routing-resolve.service';

const duyuruRoute: Routes = [
  {
    path: '',
    component: DuyuruComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DuyuruDetailComponent,
    resolve: {
      duyuru: DuyuruRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DuyuruUpdateComponent,
    resolve: {
      duyuru: DuyuruRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DuyuruUpdateComponent,
    resolve: {
      duyuru: DuyuruRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(duyuruRoute)],
  exports: [RouterModule],
})
export class DuyuruRoutingModule {}
