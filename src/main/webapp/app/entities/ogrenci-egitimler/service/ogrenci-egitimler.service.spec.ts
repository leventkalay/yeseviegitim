import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOgrenciEgitimler, OgrenciEgitimler } from '../ogrenci-egitimler.model';

import { OgrenciEgitimlerService } from './ogrenci-egitimler.service';

describe('OgrenciEgitimler Service', () => {
  let service: OgrenciEgitimlerService;
  let httpMock: HttpTestingController;
  let elemDefault: IOgrenciEgitimler;
  let expectedResult: IOgrenciEgitimler | IOgrenciEgitimler[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OgrenciEgitimlerService);
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

    it('should create a OgrenciEgitimler', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new OgrenciEgitimler()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OgrenciEgitimler', () => {
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

    it('should partial update a OgrenciEgitimler', () => {
      const patchObject = Object.assign({}, new OgrenciEgitimler());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OgrenciEgitimler', () => {
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

    it('should delete a OgrenciEgitimler', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addOgrenciEgitimlerToCollectionIfMissing', () => {
      it('should add a OgrenciEgitimler to an empty array', () => {
        const ogrenciEgitimler: IOgrenciEgitimler = { id: 123 };
        expectedResult = service.addOgrenciEgitimlerToCollectionIfMissing([], ogrenciEgitimler);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ogrenciEgitimler);
      });

      it('should not add a OgrenciEgitimler to an array that contains it', () => {
        const ogrenciEgitimler: IOgrenciEgitimler = { id: 123 };
        const ogrenciEgitimlerCollection: IOgrenciEgitimler[] = [
          {
            ...ogrenciEgitimler,
          },
          { id: 456 },
        ];
        expectedResult = service.addOgrenciEgitimlerToCollectionIfMissing(ogrenciEgitimlerCollection, ogrenciEgitimler);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OgrenciEgitimler to an array that doesn't contain it", () => {
        const ogrenciEgitimler: IOgrenciEgitimler = { id: 123 };
        const ogrenciEgitimlerCollection: IOgrenciEgitimler[] = [{ id: 456 }];
        expectedResult = service.addOgrenciEgitimlerToCollectionIfMissing(ogrenciEgitimlerCollection, ogrenciEgitimler);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ogrenciEgitimler);
      });

      it('should add only unique OgrenciEgitimler to an array', () => {
        const ogrenciEgitimlerArray: IOgrenciEgitimler[] = [{ id: 123 }, { id: 456 }, { id: 94023 }];
        const ogrenciEgitimlerCollection: IOgrenciEgitimler[] = [{ id: 123 }];
        expectedResult = service.addOgrenciEgitimlerToCollectionIfMissing(ogrenciEgitimlerCollection, ...ogrenciEgitimlerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ogrenciEgitimler: IOgrenciEgitimler = { id: 123 };
        const ogrenciEgitimler2: IOgrenciEgitimler = { id: 456 };
        expectedResult = service.addOgrenciEgitimlerToCollectionIfMissing([], ogrenciEgitimler, ogrenciEgitimler2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ogrenciEgitimler);
        expect(expectedResult).toContain(ogrenciEgitimler2);
      });

      it('should accept null and undefined values', () => {
        const ogrenciEgitimler: IOgrenciEgitimler = { id: 123 };
        expectedResult = service.addOgrenciEgitimlerToCollectionIfMissing([], null, ogrenciEgitimler, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ogrenciEgitimler);
      });

      it('should return initial array if no OgrenciEgitimler is added', () => {
        const ogrenciEgitimlerCollection: IOgrenciEgitimler[] = [{ id: 123 }];
        expectedResult = service.addOgrenciEgitimlerToCollectionIfMissing(ogrenciEgitimlerCollection, undefined, null);
        expect(expectedResult).toEqual(ogrenciEgitimlerCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
