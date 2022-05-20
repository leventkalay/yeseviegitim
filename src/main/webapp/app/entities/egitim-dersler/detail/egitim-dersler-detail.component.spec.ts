import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EgitimDerslerDetailComponent } from './egitim-dersler-detail.component';

describe('EgitimDersler Management Detail Component', () => {
  let comp: EgitimDerslerDetailComponent;
  let fixture: ComponentFixture<EgitimDerslerDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EgitimDerslerDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ egitimDersler: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EgitimDerslerDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EgitimDerslerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load egitimDersler on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.egitimDersler).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
