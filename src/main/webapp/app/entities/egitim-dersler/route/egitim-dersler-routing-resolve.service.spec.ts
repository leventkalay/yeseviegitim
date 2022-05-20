jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEgitimDersler, EgitimDersler } from '../egitim-dersler.model';
import { EgitimDerslerService } from '../service/egitim-dersler.service';

import { EgitimDerslerRoutingResolveService } from './egitim-dersler-routing-resolve.service';

describe('EgitimDersler routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: EgitimDerslerRoutingResolveService;
  let service: EgitimDerslerService;
  let resultEgitimDersler: IEgitimDersler | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(EgitimDerslerRoutingResolveService);
    service = TestBed.inject(EgitimDerslerService);
    resultEgitimDersler = undefined;
  });

  describe('resolve', () => {
    it('should return IEgitimDersler returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEgitimDersler = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEgitimDersler).toEqual({ id: 123 });
    });

    it('should return new IEgitimDersler if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEgitimDersler = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultEgitimDersler).toEqual(new EgitimDersler());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as EgitimDersler })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEgitimDersler = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEgitimDersler).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
