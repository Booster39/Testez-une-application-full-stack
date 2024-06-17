import { HttpClientModule} from '@angular/common/http';
import {HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;
  let id : string = '1';
  let path : string = 'api/user';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule, HttpClientTestingModule
      ]
    });
    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('it should get user by id', () => {
    service.getById(id).subscribe();

    const req = httpMock.expectOne(`${path}/${id}`);

    expect(req.request.method).toBe('GET');
    req.flush({});
  })

  it('it should delete user by id', () => {
    service.delete(id).subscribe();

    const req = httpMock.expectOne(`${path}/${id}`);

    expect(req.request.method).toBe('DELETE');
    req.flush({})
  })
});
