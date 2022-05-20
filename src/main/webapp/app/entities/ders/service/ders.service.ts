import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDers, getDersIdentifier } from '../ders.model';

export type EntityResponseType = HttpResponse<IDers>;
export type EntityArrayResponseType = HttpResponse<IDers[]>;

@Injectable({ providedIn: 'root' })
export class DersService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ders');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ders: IDers): Observable<EntityResponseType> {
    return this.http.post<IDers>(this.resourceUrl, ders, { observe: 'response' });
  }

  update(ders: IDers): Observable<EntityResponseType> {
    return this.http.put<IDers>(`${this.resourceUrl}/${getDersIdentifier(ders) as number}`, ders, { observe: 'response' });
  }

  partialUpdate(ders: IDers): Observable<EntityResponseType> {
    return this.http.patch<IDers>(`${this.resourceUrl}/${getDersIdentifier(ders) as number}`, ders, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDers>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDers[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDersToCollectionIfMissing(dersCollection: IDers[], ...dersToCheck: (IDers | null | undefined)[]): IDers[] {
    const ders: IDers[] = dersToCheck.filter(isPresent);
    if (ders.length > 0) {
      const dersCollectionIdentifiers = dersCollection.map(dersItem => getDersIdentifier(dersItem)!);
      const dersToAdd = ders.filter(dersItem => {
        const dersIdentifier = getDersIdentifier(dersItem);
        if (dersIdentifier == null || dersCollectionIdentifiers.includes(dersIdentifier)) {
          return false;
        }
        dersCollectionIdentifiers.push(dersIdentifier);
        return true;
      });
      return [...dersToAdd, ...dersCollection];
    }
    return dersCollection;
  }
}
