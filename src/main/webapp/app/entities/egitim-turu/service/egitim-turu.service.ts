import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEgitimTuru, getEgitimTuruIdentifier } from '../egitim-turu.model';

export type EntityResponseType = HttpResponse<IEgitimTuru>;
export type EntityArrayResponseType = HttpResponse<IEgitimTuru[]>;

@Injectable({ providedIn: 'root' })
export class EgitimTuruService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/egitim-turus');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(egitimTuru: IEgitimTuru): Observable<EntityResponseType> {
    return this.http.post<IEgitimTuru>(this.resourceUrl, egitimTuru, { observe: 'response' });
  }

  update(egitimTuru: IEgitimTuru): Observable<EntityResponseType> {
    return this.http.put<IEgitimTuru>(`${this.resourceUrl}/${getEgitimTuruIdentifier(egitimTuru) as number}`, egitimTuru, {
      observe: 'response',
    });
  }

  partialUpdate(egitimTuru: IEgitimTuru): Observable<EntityResponseType> {
    return this.http.patch<IEgitimTuru>(`${this.resourceUrl}/${getEgitimTuruIdentifier(egitimTuru) as number}`, egitimTuru, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEgitimTuru>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEgitimTuru[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEgitimTuruToCollectionIfMissing(
    egitimTuruCollection: IEgitimTuru[],
    ...egitimTurusToCheck: (IEgitimTuru | null | undefined)[]
  ): IEgitimTuru[] {
    const egitimTurus: IEgitimTuru[] = egitimTurusToCheck.filter(isPresent);
    if (egitimTurus.length > 0) {
      const egitimTuruCollectionIdentifiers = egitimTuruCollection.map(egitimTuruItem => getEgitimTuruIdentifier(egitimTuruItem)!);
      const egitimTurusToAdd = egitimTurus.filter(egitimTuruItem => {
        const egitimTuruIdentifier = getEgitimTuruIdentifier(egitimTuruItem);
        if (egitimTuruIdentifier == null || egitimTuruCollectionIdentifiers.includes(egitimTuruIdentifier)) {
          return false;
        }
        egitimTuruCollectionIdentifiers.push(egitimTuruIdentifier);
        return true;
      });
      return [...egitimTurusToAdd, ...egitimTuruCollection];
    }
    return egitimTuruCollection;
  }
}
