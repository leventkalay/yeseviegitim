import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEgitmen, getEgitmenIdentifier } from '../egitmen.model';

export type EntityResponseType = HttpResponse<IEgitmen>;
export type EntityArrayResponseType = HttpResponse<IEgitmen[]>;

@Injectable({ providedIn: 'root' })
export class EgitmenService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/egitmen');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(egitmen: IEgitmen): Observable<EntityResponseType> {
    return this.http.post<IEgitmen>(this.resourceUrl, egitmen, { observe: 'response' });
  }

  update(egitmen: IEgitmen): Observable<EntityResponseType> {
    return this.http.put<IEgitmen>(`${this.resourceUrl}/${getEgitmenIdentifier(egitmen) as number}`, egitmen, { observe: 'response' });
  }

  partialUpdate(egitmen: IEgitmen): Observable<EntityResponseType> {
    return this.http.patch<IEgitmen>(`${this.resourceUrl}/${getEgitmenIdentifier(egitmen) as number}`, egitmen, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEgitmen>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEgitmen[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEgitmenToCollectionIfMissing(egitmenCollection: IEgitmen[], ...egitmenToCheck: (IEgitmen | null | undefined)[]): IEgitmen[] {
    const egitmen: IEgitmen[] = egitmenToCheck.filter(isPresent);
    if (egitmen.length > 0) {
      const egitmenCollectionIdentifiers = egitmenCollection.map(egitmenItem => getEgitmenIdentifier(egitmenItem)!);
      const egitmenToAdd = egitmen.filter(egitmenItem => {
        const egitmenIdentifier = getEgitmenIdentifier(egitmenItem);
        if (egitmenIdentifier == null || egitmenCollectionIdentifiers.includes(egitmenIdentifier)) {
          return false;
        }
        egitmenCollectionIdentifiers.push(egitmenIdentifier);
        return true;
      });
      return [...egitmenToAdd, ...egitmenCollection];
    }
    return egitmenCollection;
  }
}
