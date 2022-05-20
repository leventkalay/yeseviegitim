import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEgitimTuru, EgitimTuru } from '../egitim-turu.model';

import { EgitimTuruService } from './egitim-turu.service';

describe('EgitimTuru Service', () => {
  let service: EgitimTuruService;
  let httpMock: HttpTestingController;
  let elemDefault: IEgitimTuru;
  let expectedResult: IEgitimTuru | IEgitimTuru[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EgitimTuruService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      adi: 'AAAAAAA',
      aciklama: 'AAAAAAA',
      aktif: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a EgitimTuru', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new EgitimTuru()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EgitimTuru', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          adi: 'BBBBBB',
          aciklama: 'BBBBBB',
          aktif: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EgitimTuru', () => {
      const patchObject = Object.assign({}, new EgitimTuru());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EgitimTuru', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          adi: 'BBBBBB',
          aciklama: 'BBBBBB',
          aktif: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a EgitimTuru', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEgitimTuruToCollectionIfMissing', () => {
      it('should add a EgitimTuru to an empty array', () => {
        const egitimTuru: IEgitimTuru = { id: 123 };
        expectedResult = service.addEgitimTuruToCollectionIfMissing([], egitimTuru);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(egitimTuru);
      });

      it('should not add a EgitimTuru to an array that contains it', () => {
        const egitimTuru: IEgitimTuru = { id: 123 };
        const egitimTuruCollection: IEgitimTuru[] = [
          {
            ...egitimTuru,
          },
          { id: 456 },
        ];
        expectedResult = service.addEgitimTuruToCollectionIfMissing(egitimTuruCollection, egitimTuru);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EgitimTuru to an array that doesn't contain it", () => {
        const egitimTuru: IEgitimTuru = { id: 123 };
        const egitimTuruCollection: IEgitimTuru[] = [{ id: 456 }];
        expectedResult = service.addEgitimTuruToCollectionIfMissing(egitimTuruCollection, egitimTuru);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(egitimTuru);
      });

      it('should add only unique EgitimTuru to an array', () => {
        const egitimTuruArray: IEgitimTuru[] = [{ id: 123 }, { id: 456 }, { id: 41673 }];
        const egitimTuruCollection: IEgitimTuru[] = [{ id: 123 }];
        expectedResult = service.addEgitimTuruToCollectionIfMissing(egitimTuruCollection, ...egitimTuruArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const egitimTuru: IEgitimTuru = { id: 123 };
        const egitimTuru2: IEgitimTuru = { id: 456 };
        expectedResult = service.addEgitimTuruToCollectionIfMissing([], egitimTuru, egitimTuru2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(egitimTuru);
        expect(expectedResult).toContain(egitimTuru2);
      });

      it('should accept null and undefined values', () => {
        const egitimTuru: IEgitimTuru = { id: 123 };
        expectedResult = service.addEgitimTuruToCollectionIfMissing([], null, egitimTuru, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(egitimTuru);
      });

      it('should return initial array if no EgitimTuru is added', () => {
        const egitimTuruCollection: IEgitimTuru[] = [{ id: 123 }];
        expectedResult = service.addEgitimTuruToCollectionIfMissing(egitimTuruCollection, undefined, null);
        expect(expectedResult).toEqual(egitimTuruCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
