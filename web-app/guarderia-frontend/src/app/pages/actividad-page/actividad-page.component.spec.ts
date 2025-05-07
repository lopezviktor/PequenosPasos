import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActividadPageComponent } from './actividad-page.component';

describe('ActividadPageComponent', () => {
  let component: ActividadPageComponent;
  let fixture: ComponentFixture<ActividadPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActividadPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActividadPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
