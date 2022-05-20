import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OgrenciEgitimlerComponent } from './list/ogrenci-egitimler.component';
import { OgrenciEgitimlerDetailComponent } from './detail/ogrenci-egitimler-detail.component';
import { OgrenciEgitimlerUpdateComponent } from './update/ogrenci-egitimler-update.component';
import { OgrenciEgitimlerDeleteDialogComponent } from './delete/ogrenci-egitimler-delete-dialog.component';
import { OgrenciEgitimlerRoutingModule } from './route/ogrenci-egitimler-routing.module';

@NgModule({
  imports: [SharedModule, OgrenciEgitimlerRoutingModule],
  declarations: [
    OgrenciEgitimlerComponent,
    OgrenciEgitimlerDetailComponent,
    OgrenciEgitimlerUpdateComponent,
    OgrenciEgitimlerDeleteDialogComponent,
  ],
  entryComponents: [OgrenciEgitimlerDeleteDialogComponent],
})
export class OgrenciEgitimlerModule {}
