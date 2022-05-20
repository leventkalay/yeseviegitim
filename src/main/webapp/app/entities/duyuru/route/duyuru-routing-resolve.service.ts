import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDuyuru, Duyuru } from '../duyuru.model';
import { DuyuruService } from '../service/duyuru.service';

@Injectable({ providedIn: 'root' })
export class DuyuruRoutingResolveService implements Resolve<IDuyuru> {
  constructor(protected service: DuyuruService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDuyuru> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((duyuru: HttpResponse<Duyuru>) => {
          if (duyuru.body) {
            return of(duyuru.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Duyuru());
  }
}
