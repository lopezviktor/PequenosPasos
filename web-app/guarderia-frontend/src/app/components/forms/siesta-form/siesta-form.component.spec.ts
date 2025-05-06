import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SiestaFormComponent } from './siesta-form.component';

describe('SiestaFormComponent', () => {
  let component: SiestaFormComponent;
  let fixture: ComponentFixture<SiestaFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SiestaFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SiestaFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
