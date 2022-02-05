import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StatsCumulativeComponent } from './stats-cumulative.component';

describe('StatsCumulativeComponent', () => {
  let component: StatsCumulativeComponent;
  let fixture: ComponentFixture<StatsCumulativeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StatsCumulativeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StatsCumulativeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
