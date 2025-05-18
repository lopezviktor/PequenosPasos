import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AsistenciaChartComponent } from './asistencia-chart.component';

describe('AsistenciaChartComponent', () => {
  let component: AsistenciaChartComponent;
  let fixture: ComponentFixture<AsistenciaChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AsistenciaChartComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AsistenciaChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
