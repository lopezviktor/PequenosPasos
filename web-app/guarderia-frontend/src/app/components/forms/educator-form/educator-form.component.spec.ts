import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EducatorFormComponent } from './educator-form.component';

describe('EducatorFormComponent', () => {
  let component: EducatorFormComponent;
  let fixture: ComponentFixture<EducatorFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EducatorFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EducatorFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
