import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DuyuruDetailComponent } from './duyuru-detail.component';

describe('Duyuru Management Detail Component', () => {
  let comp: DuyuruDetailComponent;
  let fixture: ComponentFixture<DuyuruDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DuyuruDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ duyuru: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DuyuruDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DuyuruDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load duyuru on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.duyuru).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
