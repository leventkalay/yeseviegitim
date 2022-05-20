import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { KurumComponent } from './list/kurum.component';
import { KurumDetailComponent } from './detail/kurum-detail.component';
import { KurumUpdateComponent } from './update/kurum-update.component';
import { KurumDeleteDialogComponent } from './delete/kurum-delete-dialog.component';
import { KurumRoutingModule } from './route/kurum-routing.module';

@NgModule({
  imports: [SharedModule, KurumRoutingModule],
  declarations: [KurumComponent, KurumDetailComponent, KurumUpdateComponent, KurumDeleteDialogComponent],
  entryComponents: [KurumDeleteDialogComponent],
})
export class KurumModule {}
