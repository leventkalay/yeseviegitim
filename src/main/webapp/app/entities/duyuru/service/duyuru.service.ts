import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDuyuru, getDuyuruIdentifier } from '../duyuru.model';

export type EntityResponseType = HttpResponse<IDuyuru>;
export type EntityArrayResponseType = HttpResponse<IDuyuru[]>;

@Injectable({ providedIn: 'root' })
export class DuyuruService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/duyurus');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(duyuru: IDuyuru): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(duyuru);
    return this.http
      .post<IDuyuru>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(duyuru: IDuyuru): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(duyuru);
    return this.http
      .put<IDuyuru>(`${this.resourceUrl}/${getDuyuruIdentifier(duyuru) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(duyuru: IDuyuru): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(duyuru);
    return this.http
      .patch<IDuyuru>(`${this.resourceUrl}/${getDuyuruIdentifier(duyuru) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDuyuru>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDuyuru[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDuyuruToCollectionIfMissing(duyuruCollection: IDuyuru[], ...duyurusToCheck: (IDuyuru | null | undefined)[]): IDuyuru[] {
    const duyurus: IDuyuru[] = duyurusToCheck.filter(isPresent);
    if (duyurus.length > 0) {
      const duyuruCollectionIdentifiers = duyuruCollection.map(duyuruItem => getDuyuruIdentifier(duyuruItem)!);
      const duyurusToAdd = duyurus.filter(duyuruItem => {
        const duyuruIdentifier = getDuyuruIdentifier(duyuruItem);
        if (duyuruIdentifier == null || duyuruCollectionIdentifiers.includes(duyuruIdentifier)) {
          return false;
        }
        duyuruCollectionIdentifiers.push(duyuruIdentifier);
        return true;
      });
      return [...duyurusToAdd, ...duyuruCollection];
    }
    return duyuruCollection;
  }

  protected convertDateFromClient(duyuru: IDuyuru): IDuyuru {
    return Object.assign({}, duyuru, {
      duyuruBaslamaTarihi: duyuru.duyuruBaslamaTarihi?.isValid() ? duyuru.duyuruBaslamaTarihi.format(DATE_FORMAT) : undefined,
      duyuruBitisTarihi: duyuru.duyuruBitisTarihi?.isValid() ? duyuru.duyuruBitisTarihi.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.duyuruBaslamaTarihi = res.body.duyuruBaslamaTarihi ? dayjs(res.body.duyuruBaslamaTarihi) : undefined;
      res.body.duyuruBitisTarihi = res.body.duyuruBitisTarihi ? dayjs(res.body.duyuruBitisTarihi) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((duyuru: IDuyuru) => {
        duyuru.duyuruBaslamaTarihi = duyuru.duyuruBaslamaTarihi ? dayjs(duyuru.duyuruBaslamaTarihi) : undefined;
        duyuru.duyuruBitisTarihi = duyuru.duyuruBitisTarihi ? dayjs(duyuru.duyuruBitisTarihi) : undefined;
      });
    }
    return res;
  }
}
