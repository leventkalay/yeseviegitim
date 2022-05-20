import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEgitmen } from '../egitmen.model';
import { EgitmenService } from '../service/egitmen.service';

@Component({
  templateUrl: './egitmen-delete-dialog.component.html',
})
export class EgitmenDeleteDialogComponent {
  egitmen?: IEgitmen;

  constructor(protected egitmenService: EgitmenService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.egitmenService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
