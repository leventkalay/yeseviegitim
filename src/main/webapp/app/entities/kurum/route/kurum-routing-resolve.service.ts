import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IKurum, Kurum } from '../kurum.model';
import { KurumService } from '../service/kurum.service';

@Injectable({ providedIn: 'root' })
export class KurumRoutingResolveService implements Resolve<IKurum> {
  constructor(protected service: KurumService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IKurum> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((kurum: HttpResponse<Kurum>) => {
          if (kurum.body) {
            return of(kurum.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Kurum());
  }
}
