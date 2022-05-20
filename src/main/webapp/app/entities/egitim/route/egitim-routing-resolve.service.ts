import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEgitim, Egitim } from '../egitim.model';
import { EgitimService } from '../service/egitim.service';

@Injectable({ providedIn: 'root' })
export class EgitimRoutingResolveService implements Resolve<IEgitim> {
  constructor(protected service: EgitimService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEgitim> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((egitim: HttpResponse<Egitim>) => {
          if (egitim.body) {
            return of(egitim.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Egitim());
  }
}
