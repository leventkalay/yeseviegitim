import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEgitim, getEgitimIdentifier } from '../egitim.model';

export type EntityResponseType = HttpResponse<IEgitim>;
export type EntityArrayResponseType = HttpResponse<IEgitim[]>;

@Injectable({ providedIn: 'root' })
export class EgitimService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/egitims');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(egitim: IEgitim): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(egitim);
    return this.http
      .post<IEgitim>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(egitim: IEgitim): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(egitim);
    return this.http
      .put<IEgitim>(`${this.resourceUrl}/${getEgitimIdentifier(egitim) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(egitim: IEgitim): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(egitim);
    return this.http
      .patch<IEgitim>(`${this.resourceUrl}/${getEgitimIdentifier(egitim) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEgitim>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEgitim[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEgitimToCollectionIfMissing(egitimCollection: IEgitim[], ...egitimsToCheck: (IEgitim | null | undefined)[]): IEgitim[] {
    const egitims: IEgitim[] = egitimsToCheck.filter(isPresent);
    if (egitims.length > 0) {
      const egitimCollectionIdentifiers = egitimCollection.map(egitimItem => getEgitimIdentifier(egitimItem)!);
      const egitimsToAdd = egitims.filter(egitimItem => {
        const egitimIdentifier = getEgitimIdentifier(egitimItem);
        if (egitimIdentifier == null || egitimCollectionIdentifiers.includes(egitimIdentifier)) {
          return false;
        }
        egitimCollectionIdentifiers.push(egitimIdentifier);
        return true;
      });
      return [...egitimsToAdd, ...egitimCollection];
    }
    return egitimCollection;
  }

  protected convertDateFromClient(egitim: IEgitim): IEgitim {
    return Object.assign({}, egitim, {
      egitimBaslamaTarihi: egitim.egitimBaslamaTarihi?.isValid() ? egitim.egitimBaslamaTarihi.format(DATE_FORMAT) : undefined,
      egitimBitisTarihi: egitim.egitimBitisTarihi?.isValid() ? egitim.egitimBitisTarihi.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.egitimBaslamaTarihi = res.body.egitimBaslamaTarihi ? dayjs(res.body.egitimBaslamaTarihi) : undefined;
      res.body.egitimBitisTarihi = res.body.egitimBitisTarihi ? dayjs(res.body.egitimBitisTarihi) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((egitim: IEgitim) => {
        egitim.egitimBaslamaTarihi = egitim.egitimBaslamaTarihi ? dayjs(egitim.egitimBaslamaTarihi) : undefined;
        egitim.egitimBitisTarihi = egitim.egitimBitisTarihi ? dayjs(egitim.egitimBitisTarihi) : undefined;
      });
    }
    return res;
  }
}
