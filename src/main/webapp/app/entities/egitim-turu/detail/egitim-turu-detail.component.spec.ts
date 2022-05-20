import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EgitimTuruDetailComponent } from './egitim-turu-detail.component';

describe('EgitimTuru Management Detail Component', () => {
  let comp: EgitimTuruDetailComponent;
  let fixture: ComponentFixture<EgitimTuruDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EgitimTuruDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ egitimTuru: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EgitimTuruDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EgitimTuruDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load egitimTuru on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.egitimTuru).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
