import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOgrenciEgitimler, getOgrenciEgitimlerIdentifier } from '../ogrenci-egitimler.model';

export type EntityResponseType = HttpResponse<IOgrenciEgitimler>;
export type EntityArrayResponseType = HttpResponse<IOgrenciEgitimler[]>;

@Injectable({ providedIn: 'root' })
export class OgrenciEgitimlerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ogrenci-egitimlers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ogrenciEgitimler: IOgrenciEgitimler): Observable<EntityResponseType> {
    return this.http.post<IOgrenciEgitimler>(this.resourceUrl, ogrenciEgitimler, { observe: 'response' });
  }

  update(ogrenciEgitimler: IOgrenciEgitimler): Observable<EntityResponseType> {
    return this.http.put<IOgrenciEgitimler>(
      `${this.resourceUrl}/${getOgrenciEgitimlerIdentifier(ogrenciEgitimler) as number}`,
      ogrenciEgitimler,
      { observe: 'response' }
    );
  }

  partialUpdate(ogrenciEgitimler: IOgrenciEgitimler): Observable<EntityResponseType> {
    return this.http.patch<IOgrenciEgitimler>(
      `${this.resourceUrl}/${getOgrenciEgitimlerIdentifier(ogrenciEgitimler) as number}`,
      ogrenciEgitimler,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOgrenciEgitimler>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOgrenciEgitimler[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOgrenciEgitimlerToCollectionIfMissing(
    ogrenciEgitimlerCollection: IOgrenciEgitimler[],
    ...ogrenciEgitimlersToCheck: (IOgrenciEgitimler | null | undefined)[]
  ): IOgrenciEgitimler[] {
    const ogrenciEgitimlers: IOgrenciEgitimler[] = ogrenciEgitimlersToCheck.filter(isPresent);
    if (ogrenciEgitimlers.length > 0) {
      const ogrenciEgitimlerCollectionIdentifiers = ogrenciEgitimlerCollection.map(
        ogrenciEgitimlerItem => getOgrenciEgitimlerIdentifier(ogrenciEgitimlerItem)!
      );
      const ogrenciEgitimlersToAdd = ogrenciEgitimlers.filter(ogrenciEgitimlerItem => {
        const ogrenciEgitimlerIdentifier = getOgrenciEgitimlerIdentifier(ogrenciEgitimlerItem);
        if (ogrenciEgitimlerIdentifier == null || ogrenciEgitimlerCollectionIdentifiers.includes(ogrenciEgitimlerIdentifier)) {
          return false;
        }
        ogrenciEgitimlerCollectionIdentifiers.push(ogrenciEgitimlerIdentifier);
        return true;
      });
      return [...ogrenciEgitimlersToAdd, ...ogrenciEgitimlerCollection];
    }
    return ogrenciEgitimlerCollection;
  }
}
