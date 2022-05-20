import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEgitim } from '../egitim.model';
import { EgitimService } from '../service/egitim.service';

@Component({
  templateUrl: './egitim-delete-dialog.component.html',
})
export class EgitimDeleteDialogComponent {
  egitim?: IEgitim;

  constructor(protected egitimService: EgitimService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.egitimService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
