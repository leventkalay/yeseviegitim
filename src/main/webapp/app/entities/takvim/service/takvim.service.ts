import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITakvim, getTakvimIdentifier } from '../takvim.model';

export type EntityResponseType = HttpResponse<ITakvim>;
export type EntityArrayResponseType = HttpResponse<ITakvim[]>;

@Injectable({ providedIn: 'root' })
export class TakvimService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/takvims');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(takvim: ITakvim): Observable<EntityResponseType> {
    return this.http.post<ITakvim>(this.resourceUrl, takvim, { observe: 'response' });
  }

  update(takvim: ITakvim): Observable<EntityResponseType> {
    return this.http.put<ITakvim>(`${this.resourceUrl}/${getTakvimIdentifier(takvim) as number}`, takvim, { observe: 'response' });
  }

  partialUpdate(takvim: ITakvim): Observable<EntityResponseType> {
    return this.http.patch<ITakvim>(`${this.resourceUrl}/${getTakvimIdentifier(takvim) as number}`, takvim, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITakvim>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITakvim[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTakvimToCollectionIfMissing(takvimCollection: ITakvim[], ...takvimsToCheck: (ITakvim | null | undefined)[]): ITakvim[] {
    const takvims: ITakvim[] = takvimsToCheck.filter(isPresent);
    if (takvims.length > 0) {
      const takvimCollectionIdentifiers = takvimCollection.map(takvimItem => getTakvimIdentifier(takvimItem)!);
      const takvimsToAdd = takvims.filter(takvimItem => {
        const takvimIdentifier = getTakvimIdentifier(takvimItem);
        if (takvimIdentifier == null || takvimCollectionIdentifiers.includes(takvimIdentifier)) {
          return false;
        }
        takvimCollectionIdentifiers.push(takvimIdentifier);
        return true;
      });
      return [...takvimsToAdd, ...takvimCollection];
    }
    return takvimCollection;
  }
}
