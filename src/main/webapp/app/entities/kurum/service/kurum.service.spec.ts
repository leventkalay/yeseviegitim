import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IKurum, Kurum } from '../kurum.model';

import { KurumService } from './kurum.service';

describe('Kurum Service', () => {
  let service: KurumService;
  let httpMock: HttpTestingController;
  let elemDefault: IKurum;
  let expectedResult: IKurum | IKurum[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(KurumService);
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

    it('should create a Kurum', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Kurum()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Kurum', () => {
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

    it('should partial update a Kurum', () => {
      const patchObject = Object.assign(
        {
          adi: 'BBBBBB',
          aktif: true,
        },
        new Kurum()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Kurum', () => {
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

    it('should delete a Kurum', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addKurumToCollectionIfMissing', () => {
      it('should add a Kurum to an empty array', () => {
        const kurum: IKurum = { id: 123 };
        expectedResult = service.addKurumToCollectionIfMissing([], kurum);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(kurum);
      });

      it('should not add a Kurum to an array that contains it', () => {
        const kurum: IKurum = { id: 123 };
        const kurumCollection: IKurum[] = [
          {
            ...kurum,
          },
          { id: 456 },
        ];
        expectedResult = service.addKurumToCollectionIfMissing(kurumCollection, kurum);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Kurum to an array that doesn't contain it", () => {
        const kurum: IKurum = { id: 123 };
        const kurumCollection: IKurum[] = [{ id: 456 }];
        expectedResult = service.addKurumToCollectionIfMissing(kurumCollection, kurum);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(kurum);
      });

      it('should add only unique Kurum to an array', () => {
        const kurumArray: IKurum[] = [{ id: 123 }, { id: 456 }, { id: 37581 }];
        const kurumCollection: IKurum[] = [{ id: 123 }];
        expectedResult = service.addKurumToCollectionIfMissing(kurumCollection, ...kurumArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const kurum: IKurum = { id: 123 };
        const kurum2: IKurum = { id: 456 };
        expectedResult = service.addKurumToCollectionIfMissing([], kurum, kurum2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(kurum);
        expect(expectedResult).toContain(kurum2);
      });

      it('should accept null and undefined values', () => {
        const kurum: IKurum = { id: 123 };
        expectedResult = service.addKurumToCollectionIfMissing([], null, kurum, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(kurum);
      });

      it('should return initial array if no Kurum is added', () => {
        const kurumCollection: IKurum[] = [{ id: 123 }];
        expectedResult = service.addKurumToCollectionIfMissing(kurumCollection, undefined, null);
        expect(expectedResult).toEqual(kurumCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
