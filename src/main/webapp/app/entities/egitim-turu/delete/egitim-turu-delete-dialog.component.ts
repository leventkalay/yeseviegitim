import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEgitimTuru } from '../egitim-turu.model';
import { EgitimTuruService } from '../service/egitim-turu.service';

@Component({
  templateUrl: './egitim-turu-delete-dialog.component.html',
})
export class EgitimTuruDeleteDialogComponent {
  egitimTuru?: IEgitimTuru;

  constructor(protected egitimTuruService: EgitimTuruService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.egitimTuruService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
