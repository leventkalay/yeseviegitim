import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IKurum, getKurumIdentifier } from '../kurum.model';

export type EntityResponseType = HttpResponse<IKurum>;
export type EntityArrayResponseType = HttpResponse<IKurum[]>;

@Injectable({ providedIn: 'root' })
export class KurumService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/kurums');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(kurum: IKurum): Observable<EntityResponseType> {
    return this.http.post<IKurum>(this.resourceUrl, kurum, { observe: 'response' });
  }

  update(kurum: IKurum): Observable<EntityResponseType> {
    return this.http.put<IKurum>(`${this.resourceUrl}/${getKurumIdentifier(kurum) as number}`, kurum, { observe: 'response' });
  }

  partialUpdate(kurum: IKurum): Observable<EntityResponseType> {
    return this.http.patch<IKurum>(`${this.resourceUrl}/${getKurumIdentifier(kurum) as number}`, kurum, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IKurum>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IKurum[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addKurumToCollectionIfMissing(kurumCollection: IKurum[], ...kurumsToCheck: (IKurum | null | undefined)[]): IKurum[] {
    const kurums: IKurum[] = kurumsToCheck.filter(isPresent);
    if (kurums.length > 0) {
      const kurumCollectionIdentifiers = kurumCollection.map(kurumItem => getKurumIdentifier(kurumItem)!);
      const kurumsToAdd = kurums.filter(kurumItem => {
        const kurumIdentifier = getKurumIdentifier(kurumItem);
        if (kurumIdentifier == null || kurumCollectionIdentifiers.includes(kurumIdentifier)) {
          return false;
        }
        kurumCollectionIdentifiers.push(kurumIdentifier);
        return true;
      });
      return [...kurumsToAdd, ...kurumCollection];
    }
    return kurumCollection;
  }
}
