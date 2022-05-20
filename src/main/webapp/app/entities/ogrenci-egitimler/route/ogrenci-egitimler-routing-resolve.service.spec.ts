jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IOgrenciEgitimler, OgrenciEgitimler } from '../ogrenci-egitimler.model';
import { OgrenciEgitimlerService } from '../service/ogrenci-egitimler.service';

import { OgrenciEgitimlerRoutingResolveService } from './ogrenci-egitimler-routing-resolve.service';

describe('OgrenciEgitimler routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: OgrenciEgitimlerRoutingResolveService;
  let service: OgrenciEgitimlerService;
  let resultOgrenciEgitimler: IOgrenciEgitimler | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(OgrenciEgitimlerRoutingResolveService);
    service = TestBed.inject(OgrenciEgitimlerService);
    resultOgrenciEgitimler = undefined;
  });

  describe('resolve', () => {
    it('should return IOgrenciEgitimler returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOgrenciEgitimler = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultOgrenciEgitimler).toEqual({ id: 123 });
    });

    it('should return new IOgrenciEgitimler if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOgrenciEgitimler = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultOgrenciEgitimler).toEqual(new OgrenciEgitimler());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as OgrenciEgitimler })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOgrenciEgitimler = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultOgrenciEgitimler).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
