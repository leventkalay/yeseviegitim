import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEgitmen, Egitmen } from '../egitmen.model';

import { EgitmenService } from './egitmen.service';

describe('Egitmen Service', () => {
  let service: EgitmenService;
  let httpMock: HttpTestingController;
  let elemDefault: IEgitmen;
  let expectedResult: IEgitmen | IEgitmen[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EgitmenService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      adiSoyadi: 'AAAAAAA',
      unvani: 'AAAAAAA',
      puani: 0,
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

    it('should create a Egitmen', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Egitmen()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Egitmen', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          adiSoyadi: 'BBBBBB',
          unvani: 'BBBBBB',
          puani: 1,
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

    it('should partial update a Egitmen', () => {
      const patchObject = Object.assign({}, new Egitmen());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Egitmen', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          adiSoyadi: 'BBBBBB',
          unvani: 'BBBBBB',
          puani: 1,
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

    it('should delete a Egitmen', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEgitmenToCollectionIfMissing', () => {
      it('should add a Egitmen to an empty array', () => {
        const egitmen: IEgitmen = { id: 123 };
        expectedResult = service.addEgitmenToCollectionIfMissing([], egitmen);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(egitmen);
      });

      it('should not add a Egitmen to an array that contains it', () => {
        const egitmen: IEgitmen = { id: 123 };
        const egitmenCollection: IEgitmen[] = [
          {
            ...egitmen,
          },
          { id: 456 },
        ];
        expectedResult = service.addEgitmenToCollectionIfMissing(egitmenCollection, egitmen);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Egitmen to an array that doesn't contain it", () => {
        const egitmen: IEgitmen = { id: 123 };
        const egitmenCollection: IEgitmen[] = [{ id: 456 }];
        expectedResult = service.addEgitmenToCollectionIfMissing(egitmenCollection, egitmen);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(egitmen);
      });

      it('should add only unique Egitmen to an array', () => {
        const egitmenArray: IEgitmen[] = [{ id: 123 }, { id: 456 }, { id: 12343 }];
        const egitmenCollection: IEgitmen[] = [{ id: 123 }];
        expectedResult = service.addEgitmenToCollectionIfMissing(egitmenCollection, ...egitmenArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const egitmen: IEgitmen = { id: 123 };
        const egitmen2: IEgitmen = { id: 456 };
        expectedResult = service.addEgitmenToCollectionIfMissing([], egitmen, egitmen2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(egitmen);
        expect(expectedResult).toContain(egitmen2);
      });

      it('should accept null and undefined values', () => {
        const egitmen: IEgitmen = { id: 123 };
        expectedResult = service.addEgitmenToCollectionIfMissing([], null, egitmen, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(egitmen);
      });

      it('should return initial array if no Egitmen is added', () => {
        const egitmenCollection: IEgitmen[] = [{ id: 123 }];
        expectedResult = service.addEgitmenToCollectionIfMissing(egitmenCollection, undefined, null);
        expect(expectedResult).toEqual(egitmenCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
