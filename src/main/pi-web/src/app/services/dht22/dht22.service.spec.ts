import {inject, TestBed} from '@angular/core/testing';

import {Dht22Service} from './dht22.service';

describe('Dht22Service', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [Dht22Service]
    });
  });

  it('should be created', inject([Dht22Service], (service: Dht22Service) => {
    expect(service).toBeTruthy();
  }));
});
