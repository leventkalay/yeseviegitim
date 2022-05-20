import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITakvim, Takvim } from '../takvim.model';

import { TakvimService } from './takvim.service';

describe('Takvim Service', () => {
  let service: TakvimService;
  let httpMock: HttpTestingController;
  let elemDefault: ITakvim;
  let expectedResult: ITakvim | ITakvim[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TakvimService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      adi: 'AAAAAAA',
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

    it('should create a Takvim', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Takvim()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Takvim', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          adi: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Takvim', () => {
      const patchObject = Object.assign({}, new Takvim());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Takvim', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          adi: 'BBBBBB',
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

    it('should delete a Takvim', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTakvimToCollectionIfMissing', () => {
      it('should add a Takvim to an empty array', () => {
        const takvim: ITakvim = { id: 123 };
        expectedResult = service.addTakvimToCollectionIfMissing([], takvim);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(takvim);
      });

      it('should not add a Takvim to an array that contains it', () => {
        const takvim: ITakvim = { id: 123 };
        const takvimCollection: ITakvim[] = [
          {
            ...takvim,
          },
          { id: 456 },
        ];
        expectedResult = service.addTakvimToCollectionIfMissing(takvimCollection, takvim);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Takvim to an array that doesn't contain it", () => {
        const takvim: ITakvim = { id: 123 };
        const takvimCollection: ITakvim[] = [{ id: 456 }];
        expectedResult = service.addTakvimToCollectionIfMissing(takvimCollection, takvim);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(takvim);
      });

      it('should add only unique Takvim to an array', () => {
        const takvimArray: ITakvim[] = [{ id: 123 }, { id: 456 }, { id: 63427 }];
        const takvimCollection: ITakvim[] = [{ id: 123 }];
        expectedResult = service.addTakvimToCollectionIfMissing(takvimCollection, ...takvimArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const takvim: ITakvim = { id: 123 };
        const takvim2: ITakvim = { id: 456 };
        expectedResult = service.addTakvimToCollectionIfMissing([], takvim, takvim2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(takvim);
        expect(expectedResult).toContain(takvim2);
      });

      it('should accept null and undefined values', () => {
        const takvim: ITakvim = { id: 123 };
        expectedResult = service.addTakvimToCollectionIfMissing([], null, takvim, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(takvim);
      });

      it('should return initial array if no Takvim is added', () => {
        const takvimCollection: ITakvim[] = [{ id: 123 }];
        expectedResult = service.addTakvimToCollectionIfMissing(takvimCollection, undefined, null);
        expect(expectedResult).toEqual(takvimCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
