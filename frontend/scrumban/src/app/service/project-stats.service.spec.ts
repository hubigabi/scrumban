import { TestBed } from '@angular/core/testing';

import { ProjectStatsService } from './project-stats.service';

describe('ProjectStatsService', () => {
  let service: ProjectStatsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProjectStatsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
