import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChildrenPageComponent } from './children-page.component';

describe('ChildrenPageComponent', () => {
  let component: ChildrenPageComponent;
  let fixture: ComponentFixture<ChildrenPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChildrenPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChildrenPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
