import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EntradaChartComponent } from './entrada-chart.component';

describe('EntradaChartComponent', () => {
  let component: EntradaChartComponent;
  let fixture: ComponentFixture<EntradaChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EntradaChartComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EntradaChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
