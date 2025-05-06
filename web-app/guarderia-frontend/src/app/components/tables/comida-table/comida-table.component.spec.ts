import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComidaTableComponent } from './comida-table.component';

describe('ComidaTableComponent', () => {
  let component: ComidaTableComponent;
  let fixture: ComponentFixture<ComidaTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ComidaTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ComidaTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
