import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HigienesPageComponent } from './higienes-page.component';

describe('HigienesPageComponent', () => {
  let component: HigienesPageComponent;
  let fixture: ComponentFixture<HigienesPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HigienesPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HigienesPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
