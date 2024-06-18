import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { LoginRequest } from '../../interfaces/loginRequest.interface';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceMock: jest.Mocked<AuthService>;
  let sessionServiceMock: jest.Mocked<SessionService>;
  let routerMock: jest.Mocked<Router>;

  const exampleSessionInformation: SessionInformation = {
    token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9',
    type: 'Bearer',
    id: 123,
    username: 'johndoe',
    firstName: 'John',
    lastName: 'Doe',
    admin: true
  };

  beforeEach(async () => {
    authServiceMock = {
      login: jest.fn()
    } as any;

    sessionServiceMock = {
      logIn: jest.fn()
    } as any;

    routerMock = {
      navigate: jest.fn()
    } as any;

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: Router, useValue: routerMock }
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should submit', () => {
    const loginResponse: SessionInformation = exampleSessionInformation;
    authServiceMock.login.mockReturnValue(of(loginResponse));

    component.form.setValue({
      email: 'test@example.com',
      password: 'mypassword'
    });

    component.submit();

    expect(authServiceMock.login).toHaveBeenCalledWith({
      email: 'test@example.com',
      password: 'mypassword'
    } as LoginRequest);
    expect(sessionServiceMock.logIn).toHaveBeenCalledWith(loginResponse);
    expect(routerMock.navigate).toHaveBeenCalledWith(['/sessions']);
    expect(component.onError).toBeFalsy();
  });

  it('should set onError to true on login error', () => {
    authServiceMock.login.mockReturnValue(throwError(() => new Error('error')));

    component.form.setValue({
      email: 'test@example.com',
      password: 'mypassword'
    });

    component.submit();

    expect(authServiceMock.login).toHaveBeenCalledWith({
      email: 'test@example.com',
      password: 'mypassword'
    } as LoginRequest);
    expect(component.onError).toBeTruthy();
  });
});
