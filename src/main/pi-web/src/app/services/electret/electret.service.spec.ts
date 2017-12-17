import {inject, TestBed} from '@angular/core/testing';

import {ElectretService} from './electret.service';

describe('ElectretService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ElectretService]
    });
  });

  it('should be created', inject([ElectretService], (service: ElectretService) => {
    expect(service).toBeTruthy();
  }));
});
