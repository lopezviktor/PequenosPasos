import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SiestaTableComponent } from './siesta-table.component';

describe('SiestaTableComponent', () => {
  let component: SiestaTableComponent;
  let fixture: ComponentFixture<SiestaTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SiestaTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SiestaTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
