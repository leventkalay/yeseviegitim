import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IDuyuru, Duyuru } from '../duyuru.model';

import { DuyuruService } from './duyuru.service';

describe('Duyuru Service', () => {
  let service: DuyuruService;
  let httpMock: HttpTestingController;
  let elemDefault: IDuyuru;
  let expectedResult: IDuyuru | IDuyuru[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DuyuruService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      duyuruBaslik: 'AAAAAAA',
      duyuruIcerik: 'AAAAAAA',
      duyuruBaslamaTarihi: currentDate,
      duyuruBitisTarihi: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          duyuruBaslamaTarihi: currentDate.format(DATE_FORMAT),
          duyuruBitisTarihi: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Duyuru', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          duyuruBaslamaTarihi: currentDate.format(DATE_FORMAT),
          duyuruBitisTarihi: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          duyuruBaslamaTarihi: currentDate,
          duyuruBitisTarihi: currentDate,
        },
        returnedFromService
      );

      service.create(new Duyuru()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Duyuru', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          duyuruBaslik: 'BBBBBB',
          duyuruIcerik: 'BBBBBB',
          duyuruBaslamaTarihi: currentDate.format(DATE_FORMAT),
          duyuruBitisTarihi: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          duyuruBaslamaTarihi: currentDate,
          duyuruBitisTarihi: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Duyuru', () => {
      const patchObject = Object.assign(
        {
          duyuruIcerik: 'BBBBBB',
          duyuruBaslamaTarihi: currentDate.format(DATE_FORMAT),
          duyuruBitisTarihi: currentDate.format(DATE_FORMAT),
        },
        new Duyuru()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          duyuruBaslamaTarihi: currentDate,
          duyuruBitisTarihi: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Duyuru', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          duyuruBaslik: 'BBBBBB',
          duyuruIcerik: 'BBBBBB',
          duyuruBaslamaTarihi: currentDate.format(DATE_FORMAT),
          duyuruBitisTarihi: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          duyuruBaslamaTarihi: currentDate,
          duyuruBitisTarihi: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Duyuru', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDuyuruToCollectionIfMissing', () => {
      it('should add a Duyuru to an empty array', () => {
        const duyuru: IDuyuru = { id: 123 };
        expectedResult = service.addDuyuruToCollectionIfMissing([], duyuru);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(duyuru);
      });

      it('should not add a Duyuru to an array that contains it', () => {
        const duyuru: IDuyuru = { id: 123 };
        const duyuruCollection: IDuyuru[] = [
          {
            ...duyuru,
          },
          { id: 456 },
        ];
        expectedResult = service.addDuyuruToCollectionIfMissing(duyuruCollection, duyuru);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Duyuru to an array that doesn't contain it", () => {
        const duyuru: IDuyuru = { id: 123 };
        const duyuruCollection: IDuyuru[] = [{ id: 456 }];
        expectedResult = service.addDuyuruToCollectionIfMissing(duyuruCollection, duyuru);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(duyuru);
      });

      it('should add only unique Duyuru to an array', () => {
        const duyuruArray: IDuyuru[] = [{ id: 123 }, { id: 456 }, { id: 15448 }];
        const duyuruCollection: IDuyuru[] = [{ id: 123 }];
        expectedResult = service.addDuyuruToCollectionIfMissing(duyuruCollection, ...duyuruArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const duyuru: IDuyuru = { id: 123 };
        const duyuru2: IDuyuru = { id: 456 };
        expectedResult = service.addDuyuruToCollectionIfMissing([], duyuru, duyuru2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(duyuru);
        expect(expectedResult).toContain(duyuru2);
      });

      it('should accept null and undefined values', () => {
        const duyuru: IDuyuru = { id: 123 };
        expectedResult = service.addDuyuruToCollectionIfMissing([], null, duyuru, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(duyuru);
      });

      it('should return initial array if no Duyuru is added', () => {
        const duyuruCollection: IDuyuru[] = [{ id: 123 }];
        expectedResult = service.addDuyuruToCollectionIfMissing(duyuruCollection, undefined, null);
        expect(expectedResult).toEqual(duyuruCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
