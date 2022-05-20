import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEgitimTuru, EgitimTuru } from '../egitim-turu.model';
import { EgitimTuruService } from '../service/egitim-turu.service';

@Injectable({ providedIn: 'root' })
export class EgitimTuruRoutingResolveService implements Resolve<IEgitimTuru> {
  constructor(protected service: EgitimTuruService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEgitimTuru> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((egitimTuru: HttpResponse<EgitimTuru>) => {
          if (egitimTuru.body) {
            return of(egitimTuru.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EgitimTuru());
  }
}
