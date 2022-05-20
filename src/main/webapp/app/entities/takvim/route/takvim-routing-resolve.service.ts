import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITakvim, Takvim } from '../takvim.model';
import { TakvimService } from '../service/takvim.service';

@Injectable({ providedIn: 'root' })
export class TakvimRoutingResolveService implements Resolve<ITakvim> {
  constructor(protected service: TakvimService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITakvim> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((takvim: HttpResponse<Takvim>) => {
          if (takvim.body) {
            return of(takvim.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Takvim());
  }
}
