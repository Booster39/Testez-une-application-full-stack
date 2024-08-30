import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { AuthService } from './auth.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should register a new user', () => {
    //Given
    const mockRegisterRequest: RegisterRequest = {
      email: 'example@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123'
    };

    service.register(mockRegisterRequest).subscribe();

    //When
    const req = httpMock.expectOne('api/auth/register');

    //Then
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockRegisterRequest);
    req.flush({});
  });

  it('should login a user', () => {
    //Given
    const mockLoginRequest: LoginRequest = {
      email: 'example@example.com',
      password: 'password123'
    };

    const mockSessionInformation: SessionInformation = {
      token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9',
      type: 'Bearer',
      id: 1,
      username: 'johndoe',
      firstName: 'John',
      lastName: 'Doe',
      admin: false
    };

    service.login(mockLoginRequest).subscribe((sessionInfo) => {
      expect(sessionInfo).toEqual(mockSessionInformation);
    });

    //When
    const req = httpMock.expectOne('api/auth/login');

    //Then
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockLoginRequest);

    req.flush(mockSessionInformation);
  });
});