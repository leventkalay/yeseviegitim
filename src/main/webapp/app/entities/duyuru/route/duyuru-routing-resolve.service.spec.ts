jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDuyuru, Duyuru } from '../duyuru.model';
import { DuyuruService } from '../service/duyuru.service';

import { DuyuruRoutingResolveService } from './duyuru-routing-resolve.service';

describe('Duyuru routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DuyuruRoutingResolveService;
  let service: DuyuruService;
  let resultDuyuru: IDuyuru | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(DuyuruRoutingResolveService);
    service = TestBed.inject(DuyuruService);
    resultDuyuru = undefined;
  });

  describe('resolve', () => {
    it('should return IDuyuru returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDuyuru = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDuyuru).toEqual({ id: 123 });
    });

    it('should return new IDuyuru if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDuyuru = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDuyuru).toEqual(new Duyuru());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Duyuru })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDuyuru = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDuyuru).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
