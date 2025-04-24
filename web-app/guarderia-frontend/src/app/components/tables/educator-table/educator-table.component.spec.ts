import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EducatorTableComponent } from './educator-table.component';

describe('EducatorTableComponent', () => {
  let component: EducatorTableComponent;
  let fixture: ComponentFixture<EducatorTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EducatorTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EducatorTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
