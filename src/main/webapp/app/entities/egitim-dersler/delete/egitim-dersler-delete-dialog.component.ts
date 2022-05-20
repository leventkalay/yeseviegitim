import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEgitimDersler } from '../egitim-dersler.model';
import { EgitimDerslerService } from '../service/egitim-dersler.service';

@Component({
  templateUrl: './egitim-dersler-delete-dialog.component.html',
})
export class EgitimDerslerDeleteDialogComponent {
  egitimDersler?: IEgitimDersler;

  constructor(protected egitimDerslerService: EgitimDerslerService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.egitimDerslerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
