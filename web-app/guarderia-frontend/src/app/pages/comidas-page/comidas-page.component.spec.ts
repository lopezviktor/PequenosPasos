import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComidasPageComponent } from './comidas-page.component';

describe('ComidasPageComponent', () => {
  let component: ComidasPageComponent;
  let fixture: ComponentFixture<ComidasPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ComidasPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ComidasPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
