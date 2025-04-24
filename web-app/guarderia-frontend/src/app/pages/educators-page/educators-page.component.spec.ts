import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EducatorsPageComponent } from './educators-page.component';

describe('EducatorsPageComponent', () => {
  let component: EducatorsPageComponent;
  let fixture: ComponentFixture<EducatorsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EducatorsPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EducatorsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
