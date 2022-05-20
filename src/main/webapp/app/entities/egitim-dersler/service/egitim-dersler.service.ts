import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEgitimDersler, getEgitimDerslerIdentifier } from '../egitim-dersler.model';

export type EntityResponseType = HttpResponse<IEgitimDersler>;
export type EntityArrayResponseType = HttpResponse<IEgitimDersler[]>;

@Injectable({ providedIn: 'root' })
export class EgitimDerslerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/egitim-derslers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(egitimDersler: IEgitimDersler): Observable<EntityResponseType> {
    return this.http.post<IEgitimDersler>(this.resourceUrl, egitimDersler, { observe: 'response' });
  }

  update(egitimDersler: IEgitimDersler): Observable<EntityResponseType> {
    return this.http.put<IEgitimDersler>(`${this.resourceUrl}/${getEgitimDerslerIdentifier(egitimDersler) as number}`, egitimDersler, {
      observe: 'response',
    });
  }

  partialUpdate(egitimDersler: IEgitimDersler): Observable<EntityResponseType> {
    return this.http.patch<IEgitimDersler>(`${this.resourceUrl}/${getEgitimDerslerIdentifier(egitimDersler) as number}`, egitimDersler, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEgitimDersler>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEgitimDersler[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEgitimDerslerToCollectionIfMissing(
    egitimDerslerCollection: IEgitimDersler[],
    ...egitimDerslersToCheck: (IEgitimDersler | null | undefined)[]
  ): IEgitimDersler[] {
    const egitimDerslers: IEgitimDersler[] = egitimDerslersToCheck.filter(isPresent);
    if (egitimDerslers.length > 0) {
      const egitimDerslerCollectionIdentifiers = egitimDerslerCollection.map(
        egitimDerslerItem => getEgitimDerslerIdentifier(egitimDerslerItem)!
      );
      const egitimDerslersToAdd = egitimDerslers.filter(egitimDerslerItem => {
        const egitimDerslerIdentifier = getEgitimDerslerIdentifier(egitimDerslerItem);
        if (egitimDerslerIdentifier == null || egitimDerslerCollectionIdentifiers.includes(egitimDerslerIdentifier)) {
          return false;
        }
        egitimDerslerCollectionIdentifiers.push(egitimDerslerIdentifier);
        return true;
      });
      return [...egitimDerslersToAdd, ...egitimDerslerCollection];
    }
    return egitimDerslerCollection;
  }
}
