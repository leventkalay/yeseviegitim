import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EgitmenComponent } from './list/egitmen.component';
import { EgitmenDetailComponent } from './detail/egitmen-detail.component';
import { EgitmenUpdateComponent } from './update/egitmen-update.component';
import { EgitmenDeleteDialogComponent } from './delete/egitmen-delete-dialog.component';
import { EgitmenRoutingModule } from './route/egitmen-routing.module';

@NgModule({
  imports: [SharedModule, EgitmenRoutingModule],
  declarations: [EgitmenComponent, EgitmenDetailComponent, EgitmenUpdateComponent, EgitmenDeleteDialogComponent],
  entryComponents: [EgitmenDeleteDialogComponent],
})
export class EgitmenModule {}
