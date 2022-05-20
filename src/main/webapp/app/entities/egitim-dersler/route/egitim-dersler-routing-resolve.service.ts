import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEgitimDersler, EgitimDersler } from '../egitim-dersler.model';
import { EgitimDerslerService } from '../service/egitim-dersler.service';

@Injectable({ providedIn: 'root' })
export class EgitimDerslerRoutingResolveService implements Resolve<IEgitimDersler> {
  constructor(protected service: EgitimDerslerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEgitimDersler> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((egitimDersler: HttpResponse<EgitimDersler>) => {
          if (egitimDersler.body) {
            return of(egitimDersler.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EgitimDersler());
  }
}
