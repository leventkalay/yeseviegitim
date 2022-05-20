import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEgitmen, Egitmen } from '../egitmen.model';
import { EgitmenService } from '../service/egitmen.service';

@Injectable({ providedIn: 'root' })
export class EgitmenRoutingResolveService implements Resolve<IEgitmen> {
  constructor(protected service: EgitmenService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEgitmen> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((egitmen: HttpResponse<Egitmen>) => {
          if (egitmen.body) {
            return of(egitmen.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Egitmen());
  }
}
