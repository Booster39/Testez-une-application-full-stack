import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authServiceMock: jest.Mocked<AuthService>;
  let routerMock: jest.Mocked<Router>;

  const exampleRegisterRequest: RegisterRequest = {
    email: 'test@example.com',
    firstName: 'John',
    lastName: 'Doe',
    password: 'mypassword'
  };

  beforeEach(async () => {
    authServiceMock = {
      register: jest.fn()
    } as any;

    routerMock = {
      navigate: jest.fn()
    } as any;

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock }
      ],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should submit', () => {
    authServiceMock.register.mockReturnValue(of(void 0));

    component.form.setValue(exampleRegisterRequest);

    component.submit();

    expect(authServiceMock.register).toHaveBeenCalledWith(exampleRegisterRequest);
    expect(routerMock.navigate).toHaveBeenCalledWith(['/login']);
    expect(component.onError).toBeFalsy();
  });

  it('should set onError to true on register error', () => {
    authServiceMock.register.mockReturnValue(throwError(() => new Error('error')));

    component.form.setValue(exampleRegisterRequest);

    component.submit();

    expect(authServiceMock.register).toHaveBeenCalledWith(exampleRegisterRequest);
    expect(component.onError).toBeTruthy();
  });
});
