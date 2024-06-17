import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { SessionInformation } from '../interfaces/sessionInformation.interface';


import { SessionService } from './session.service';

describe('SessionService', () => {
  let service: SessionService;
  const user: SessionInformation = {
    token: 'abc123',
    type: 'Bearer',
    id: 1,
    username: 'johndoe',
    firstName: 'John',
    lastName: 'Doe',
    admin: true,
  };
  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('it should be logged', () => {
    service.$isLogged().subscribe((isLogged) => expect(isLogged).toBeTruthy());
  });

  it('it should log in', () => {
    service.logIn(user);

    service.$isLogged().subscribe((isLogged) => expect(isLogged).toBeTruthy());
  })

  it('it should log out', () => {
    service.logOut();
    service.$isLogged().subscribe((isLogged) => expect(isLogged).toBeFalsy());

  })
});
