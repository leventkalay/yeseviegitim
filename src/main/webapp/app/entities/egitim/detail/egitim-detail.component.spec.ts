import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EgitimDetailComponent } from './egitim-detail.component';

describe('Egitim Management Detail Component', () => {
  let comp: EgitimDetailComponent;
  let fixture: ComponentFixture<EgitimDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EgitimDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ egitim: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EgitimDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EgitimDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load egitim on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.egitim).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
