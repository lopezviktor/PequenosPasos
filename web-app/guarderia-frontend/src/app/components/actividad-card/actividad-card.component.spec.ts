import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActividadCardComponent } from './actividad-card.component';

describe('ActividadCardComponent', () => {
  let component: ActividadCardComponent;
  let fixture: ComponentFixture<ActividadCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActividadCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActividadCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
