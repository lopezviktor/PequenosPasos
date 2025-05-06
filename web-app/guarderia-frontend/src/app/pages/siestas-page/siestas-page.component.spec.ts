import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SiestasPageComponent } from './siestas-page.component';

describe('SiestasPageComponent', () => {
  let component: SiestasPageComponent;
  let fixture: ComponentFixture<SiestasPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SiestasPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SiestasPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
