import { HttpClientModule} from '@angular/common/http';
import {HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { User } from '../interfaces/user.interface';

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
    //Given
    const mockUser: User = {
      id: 1,
      email: 'example@example.com',
      lastName: 'Doe',
      firstName: 'John',
      admin: true,
      password: 'password123',
      createdAt: new Date(),
      updatedAt: new Date()
    };
    service.getById(id).subscribe(user => expect(user).toEqual(mockUser));

    //When
    const req = httpMock.expectOne(`${path}/${id}`);

    //Then
    expect(req.request.method).toBe('GET');
    req.flush(mockUser);
  })

  it('it should delete user by id', () => {
    //Given
    service.delete(id).subscribe(response => {
      expect(response).toBeUndefined()
    });
  
    //When
    const req = httpMock.expectOne(`${path}/${id}`);
  
    //Then
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  })
});
