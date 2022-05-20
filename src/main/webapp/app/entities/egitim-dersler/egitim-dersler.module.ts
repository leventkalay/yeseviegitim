import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EgitimDerslerComponent } from './list/egitim-dersler.component';
import { EgitimDerslerDetailComponent } from './detail/egitim-dersler-detail.component';
import { EgitimDerslerUpdateComponent } from './update/egitim-dersler-update.component';
import { EgitimDerslerDeleteDialogComponent } from './delete/egitim-dersler-delete-dialog.component';
import { EgitimDerslerRoutingModule } from './route/egitim-dersler-routing.module';

@NgModule({
  imports: [SharedModule, EgitimDerslerRoutingModule],
  declarations: [EgitimDerslerComponent, EgitimDerslerDetailComponent, EgitimDerslerUpdateComponent, EgitimDerslerDeleteDialogComponent],
  entryComponents: [EgitimDerslerDeleteDialogComponent],
})
export class EgitimDerslerModule {}
