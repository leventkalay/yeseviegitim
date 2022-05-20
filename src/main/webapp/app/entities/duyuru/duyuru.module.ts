import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DuyuruComponent } from './list/duyuru.component';
import { DuyuruDetailComponent } from './detail/duyuru-detail.component';
import { DuyuruUpdateComponent } from './update/duyuru-update.component';
import { DuyuruDeleteDialogComponent } from './delete/duyuru-delete-dialog.component';
import { DuyuruRoutingModule } from './route/duyuru-routing.module';

@NgModule({
  imports: [SharedModule, DuyuruRoutingModule],
  declarations: [DuyuruComponent, DuyuruDetailComponent, DuyuruUpdateComponent, DuyuruDeleteDialogComponent],
  entryComponents: [DuyuruDeleteDialogComponent],
})
export class DuyuruModule {}
