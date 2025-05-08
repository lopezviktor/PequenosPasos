import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActividadDetailsComponent } from './actividad-details.component';

describe('ActividadDetailsComponent', () => {
  let component: ActividadDetailsComponent;
  let fixture: ComponentFixture<ActividadDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActividadDetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActividadDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
