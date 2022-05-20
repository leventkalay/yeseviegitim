import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITakvim } from '../takvim.model';
import { TakvimService } from '../service/takvim.service';

@Component({
  templateUrl: './takvim-delete-dialog.component.html',
})
export class TakvimDeleteDialogComponent {
  takvim?: ITakvim;

  constructor(protected takvimService: TakvimService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.takvimService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
