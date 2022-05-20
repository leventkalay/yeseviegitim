jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITakvim, Takvim } from '../takvim.model';
import { TakvimService } from '../service/takvim.service';

import { TakvimRoutingResolveService } from './takvim-routing-resolve.service';

describe('Takvim routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TakvimRoutingResolveService;
  let service: TakvimService;
  let resultTakvim: ITakvim | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(TakvimRoutingResolveService);
    service = TestBed.inject(TakvimService);
    resultTakvim = undefined;
  });

  describe('resolve', () => {
    it('should return ITakvim returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTakvim = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTakvim).toEqual({ id: 123 });
    });

    it('should return new ITakvim if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTakvim = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTakvim).toEqual(new Takvim());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Takvim })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTakvim = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTakvim).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
