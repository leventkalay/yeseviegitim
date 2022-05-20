import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEgitimDersler, EgitimDersler } from '../egitim-dersler.model';

import { EgitimDerslerService } from './egitim-dersler.service';

describe('EgitimDersler Service', () => {
  let service: EgitimDerslerService;
  let httpMock: HttpTestingController;
  let elemDefault: IEgitimDersler;
  let expectedResult: IEgitimDersler | IEgitimDersler[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EgitimDerslerService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
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

    it('should create a EgitimDersler', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new EgitimDersler()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EgitimDersler', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EgitimDersler', () => {
      const patchObject = Object.assign({}, new EgitimDersler());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EgitimDersler', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
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

    it('should delete a EgitimDersler', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEgitimDerslerToCollectionIfMissing', () => {
      it('should add a EgitimDersler to an empty array', () => {
        const egitimDersler: IEgitimDersler = { id: 123 };
        expectedResult = service.addEgitimDerslerToCollectionIfMissing([], egitimDersler);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(egitimDersler);
      });

      it('should not add a EgitimDersler to an array that contains it', () => {
        const egitimDersler: IEgitimDersler = { id: 123 };
        const egitimDerslerCollection: IEgitimDersler[] = [
          {
            ...egitimDersler,
          },
          { id: 456 },
        ];
        expectedResult = service.addEgitimDerslerToCollectionIfMissing(egitimDerslerCollection, egitimDersler);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EgitimDersler to an array that doesn't contain it", () => {
        const egitimDersler: IEgitimDersler = { id: 123 };
        const egitimDerslerCollection: IEgitimDersler[] = [{ id: 456 }];
        expectedResult = service.addEgitimDerslerToCollectionIfMissing(egitimDerslerCollection, egitimDersler);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(egitimDersler);
      });

      it('should add only unique EgitimDersler to an array', () => {
        const egitimDerslerArray: IEgitimDersler[] = [{ id: 123 }, { id: 456 }, { id: 87867 }];
        const egitimDerslerCollection: IEgitimDersler[] = [{ id: 123 }];
        expectedResult = service.addEgitimDerslerToCollectionIfMissing(egitimDerslerCollection, ...egitimDerslerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const egitimDersler: IEgitimDersler = { id: 123 };
        const egitimDersler2: IEgitimDersler = { id: 456 };
        expectedResult = service.addEgitimDerslerToCollectionIfMissing([], egitimDersler, egitimDersler2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(egitimDersler);
        expect(expectedResult).toContain(egitimDersler2);
      });

      it('should accept null and undefined values', () => {
        const egitimDersler: IEgitimDersler = { id: 123 };
        expectedResult = service.addEgitimDerslerToCollectionIfMissing([], null, egitimDersler, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(egitimDersler);
      });

      it('should return initial array if no EgitimDersler is added', () => {
        const egitimDerslerCollection: IEgitimDersler[] = [{ id: 123 }];
        expectedResult = service.addEgitimDerslerToCollectionIfMissing(egitimDerslerCollection, undefined, null);
        expect(expectedResult).toEqual(egitimDerslerCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
