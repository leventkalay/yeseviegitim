import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EgitimTuruComponent } from './list/egitim-turu.component';
import { EgitimTuruDetailComponent } from './detail/egitim-turu-detail.component';
import { EgitimTuruUpdateComponent } from './update/egitim-turu-update.component';
import { EgitimTuruDeleteDialogComponent } from './delete/egitim-turu-delete-dialog.component';
import { EgitimTuruRoutingModule } from './route/egitim-turu-routing.module';

@NgModule({
  imports: [SharedModule, EgitimTuruRoutingModule],
  declarations: [EgitimTuruComponent, EgitimTuruDetailComponent, EgitimTuruUpdateComponent, EgitimTuruDeleteDialogComponent],
  entryComponents: [EgitimTuruDeleteDialogComponent],
})
export class EgitimTuruModule {}
