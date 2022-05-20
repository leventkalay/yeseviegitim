import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EgitimTuruComponent } from '../list/egitim-turu.component';
import { EgitimTuruDetailComponent } from '../detail/egitim-turu-detail.component';
import { EgitimTuruUpdateComponent } from '../update/egitim-turu-update.component';
import { EgitimTuruRoutingResolveService } from './egitim-turu-routing-resolve.service';

const egitimTuruRoute: Routes = [
  {
    path: '',
    component: EgitimTuruComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EgitimTuruDetailComponent,
    resolve: {
      egitimTuru: EgitimTuruRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EgitimTuruUpdateComponent,
    resolve: {
      egitimTuru: EgitimTuruRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EgitimTuruUpdateComponent,
    resolve: {
      egitimTuru: EgitimTuruRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(egitimTuruRoute)],
  exports: [RouterModule],
})
export class EgitimTuruRoutingModule {}
