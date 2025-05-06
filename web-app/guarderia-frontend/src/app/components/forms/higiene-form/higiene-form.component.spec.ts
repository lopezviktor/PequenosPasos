import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HigieneFormComponent } from './higiene-form.component';

describe('HigieneFormComponent', () => {
  let component: HigieneFormComponent;
  let fixture: ComponentFixture<HigieneFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HigieneFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HigieneFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
