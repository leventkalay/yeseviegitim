import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EgitmenDetailComponent } from './egitmen-detail.component';

describe('Egitmen Management Detail Component', () => {
  let comp: EgitmenDetailComponent;
  let fixture: ComponentFixture<EgitmenDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EgitmenDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ egitmen: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EgitmenDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EgitmenDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load egitmen on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.egitmen).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
