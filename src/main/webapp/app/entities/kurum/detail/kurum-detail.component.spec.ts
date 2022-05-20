import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KurumDetailComponent } from './kurum-detail.component';

describe('Kurum Management Detail Component', () => {
  let comp: KurumDetailComponent;
  let fixture: ComponentFixture<KurumDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [KurumDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ kurum: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(KurumDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(KurumDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load kurum on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.kurum).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
