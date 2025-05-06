import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HigieneTableComponent } from './higiene-table.component';

describe('HigieneTableComponent', () => {
  let component: HigieneTableComponent;
  let fixture: ComponentFixture<HigieneTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HigieneTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HigieneTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
