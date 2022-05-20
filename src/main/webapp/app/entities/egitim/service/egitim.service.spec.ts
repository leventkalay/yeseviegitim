import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IEgitim, Egitim } from '../egitim.model';

import { EgitimService } from './egitim.service';

describe('Egitim Service', () => {
  let service: EgitimService;
  let httpMock: HttpTestingController;
  let elemDefault: IEgitim;
  let expectedResult: IEgitim | IEgitim[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EgitimService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      egitimBaslik: 'AAAAAAA',
      egitimAltBaslik: 'AAAAAAA',
      egitimBaslamaTarihi: currentDate,
      egitimBitisTarihi: currentDate,
      dersSayisi: 0,
      egitimSuresi: 0,
      egitimYeri: 'AAAAAAA',
      egitimPuani: 0,
      aktif: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          egitimBaslamaTarihi: currentDate.format(DATE_FORMAT),
          egitimBitisTarihi: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Egitim', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          egitimBaslamaTarihi: currentDate.format(DATE_FORMAT),
          egitimBitisTarihi: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          egitimBaslamaTarihi: currentDate,
          egitimBitisTarihi: currentDate,
        },
        returnedFromService
      );

      service.create(new Egitim()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Egitim', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          egitimBaslik: 'BBBBBB',
          egitimAltBaslik: 'BBBBBB',
          egitimBaslamaTarihi: currentDate.format(DATE_FORMAT),
          egitimBitisTarihi: currentDate.format(DATE_FORMAT),
          dersSayisi: 1,
          egitimSuresi: 1,
          egitimYeri: 'BBBBBB',
          egitimPuani: 1,
          aktif: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          egitimBaslamaTarihi: currentDate,
          egitimBitisTarihi: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Egitim', () => {
      const patchObject = Object.assign(
        {
          egitimBaslik: 'BBBBBB',
          egitimAltBaslik: 'BBBBBB',
          egitimBaslamaTarihi: currentDate.format(DATE_FORMAT),
          egitimYeri: 'BBBBBB',
          egitimPuani: 1,
          aktif: true,
        },
        new Egitim()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          egitimBaslamaTarihi: currentDate,
          egitimBitisTarihi: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Egitim', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          egitimBaslik: 'BBBBBB',
          egitimAltBaslik: 'BBBBBB',
          egitimBaslamaTarihi: currentDate.format(DATE_FORMAT),
          egitimBitisTarihi: currentDate.format(DATE_FORMAT),
          dersSayisi: 1,
          egitimSuresi: 1,
          egitimYeri: 'BBBBBB',
          egitimPuani: 1,
          aktif: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          egitimBaslamaTarihi: currentDate,
          egitimBitisTarihi: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Egitim', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEgitimToCollectionIfMissing', () => {
      it('should add a Egitim to an empty array', () => {
        const egitim: IEgitim = { id: 123 };
        expectedResult = service.addEgitimToCollectionIfMissing([], egitim);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(egitim);
      });

      it('should not add a Egitim to an array that contains it', () => {
        const egitim: IEgitim = { id: 123 };
        const egitimCollection: IEgitim[] = [
          {
            ...egitim,
          },
          { id: 456 },
        ];
        expectedResult = service.addEgitimToCollectionIfMissing(egitimCollection, egitim);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Egitim to an array that doesn't contain it", () => {
        const egitim: IEgitim = { id: 123 };
        const egitimCollection: IEgitim[] = [{ id: 456 }];
        expectedResult = service.addEgitimToCollectionIfMissing(egitimCollection, egitim);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(egitim);
      });

      it('should add only unique Egitim to an array', () => {
        const egitimArray: IEgitim[] = [{ id: 123 }, { id: 456 }, { id: 73393 }];
        const egitimCollection: IEgitim[] = [{ id: 123 }];
        expectedResult = service.addEgitimToCollectionIfMissing(egitimCollection, ...egitimArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const egitim: IEgitim = { id: 123 };
        const egitim2: IEgitim = { id: 456 };
        expectedResult = service.addEgitimToCollectionIfMissing([], egitim, egitim2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(egitim);
        expect(expectedResult).toContain(egitim2);
      });

      it('should accept null and undefined values', () => {
        const egitim: IEgitim = { id: 123 };
        expectedResult = service.addEgitimToCollectionIfMissing([], null, egitim, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(egitim);
      });

      it('should return initial array if no Egitim is added', () => {
        const egitimCollection: IEgitim[] = [{ id: 123 }];
        expectedResult = service.addEgitimToCollectionIfMissing(egitimCollection, undefined, null);
        expect(expectedResult).toEqual(egitimCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
