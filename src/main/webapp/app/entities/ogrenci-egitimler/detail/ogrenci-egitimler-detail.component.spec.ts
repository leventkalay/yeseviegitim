import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OgrenciEgitimlerDetailComponent } from './ogrenci-egitimler-detail.component';

describe('OgrenciEgitimler Management Detail Component', () => {
  let comp: OgrenciEgitimlerDetailComponent;
  let fixture: ComponentFixture<OgrenciEgitimlerDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OgrenciEgitimlerDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ ogrenciEgitimler: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OgrenciEgitimlerDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OgrenciEgitimlerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load ogrenciEgitimler on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.ogrenciEgitimler).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
