import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IKurum } from '../kurum.model';
import { KurumService } from '../service/kurum.service';

@Component({
  templateUrl: './kurum-delete-dialog.component.html',
})
export class KurumDeleteDialogComponent {
  kurum?: IKurum;

  constructor(protected kurumService: KurumService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.kurumService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
