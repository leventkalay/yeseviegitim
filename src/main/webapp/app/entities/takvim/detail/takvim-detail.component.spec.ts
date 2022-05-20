import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TakvimDetailComponent } from './takvim-detail.component';

describe('Takvim Management Detail Component', () => {
  let comp: TakvimDetailComponent;
  let fixture: ComponentFixture<TakvimDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TakvimDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ takvim: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TakvimDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TakvimDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load takvim on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.takvim).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
