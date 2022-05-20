jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEgitimTuru, EgitimTuru } from '../egitim-turu.model';
import { EgitimTuruService } from '../service/egitim-turu.service';

import { EgitimTuruRoutingResolveService } from './egitim-turu-routing-resolve.service';

describe('EgitimTuru routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: EgitimTuruRoutingResolveService;
  let service: EgitimTuruService;
  let resultEgitimTuru: IEgitimTuru | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(EgitimTuruRoutingResolveService);
    service = TestBed.inject(EgitimTuruService);
    resultEgitimTuru = undefined;
  });

  describe('resolve', () => {
    it('should return IEgitimTuru returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEgitimTuru = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEgitimTuru).toEqual({ id: 123 });
    });

    it('should return new IEgitimTuru if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEgitimTuru = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultEgitimTuru).toEqual(new EgitimTuru());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as EgitimTuru })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEgitimTuru = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEgitimTuru).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
