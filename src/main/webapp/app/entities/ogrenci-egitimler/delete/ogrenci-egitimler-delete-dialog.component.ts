import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOgrenciEgitimler } from '../ogrenci-egitimler.model';
import { OgrenciEgitimlerService } from '../service/ogrenci-egitimler.service';

@Component({
  templateUrl: './ogrenci-egitimler-delete-dialog.component.html',
})
export class OgrenciEgitimlerDeleteDialogComponent {
  ogrenciEgitimler?: IOgrenciEgitimler;

  constructor(protected ogrenciEgitimlerService: OgrenciEgitimlerService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ogrenciEgitimlerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
