import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOgrenciEgitimler, OgrenciEgitimler } from '../ogrenci-egitimler.model';
import { OgrenciEgitimlerService } from '../service/ogrenci-egitimler.service';

@Injectable({ providedIn: 'root' })
export class OgrenciEgitimlerRoutingResolveService implements Resolve<IOgrenciEgitimler> {
  constructor(protected service: OgrenciEgitimlerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOgrenciEgitimler> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ogrenciEgitimler: HttpResponse<OgrenciEgitimler>) => {
          if (ogrenciEgitimler.body) {
            return of(ogrenciEgitimler.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OgrenciEgitimler());
  }
}
