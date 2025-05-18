import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActividadChartComponent } from './actividad-chart.component';

describe('ActividadChartComponent', () => {
  let component: ActividadChartComponent;
  let fixture: ComponentFixture<ActividadChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActividadChartComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActividadChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
