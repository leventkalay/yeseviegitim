import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDuyuru } from '../duyuru.model';
import { DuyuruService } from '../service/duyuru.service';

@Component({
  templateUrl: './duyuru-delete-dialog.component.html',
})
export class DuyuruDeleteDialogComponent {
  duyuru?: IDuyuru;

  constructor(protected duyuruService: DuyuruService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.duyuruService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
