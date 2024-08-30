import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {  FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { Session } from '../../interfaces/session.interface';
import { of } from 'rxjs';
import { Router } from '@angular/router';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  } 

  beforeEach(async () => {
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
        NoopAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        SessionApiService
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form when component is created', () => {
    //Given
    const session: Session = {
      name: 'Test Session',
      date: new Date('2024-08-29T22:44:27.485Z'),
      teacher_id: 123,
      description: 'Test description',
      users: []
    };

    const formGroup = TestBed.inject(FormBuilder).group({
      name: [session.name, [Validators.required]],
      date: [session.date.toISOString().split('T')[0], [Validators.required]], //  problem
      teacher_id: [session.teacher_id, [Validators.required]],
      description: [session.description, [Validators.required, Validators.max(2000)]]
    });

    const formBuilderSpy = jest.spyOn(TestBed.inject(FormBuilder), 'group').mockReturnValue(formGroup);

    //When
    component['initForm'](session);

    //Then
    expect(formBuilderSpy).toHaveBeenCalledWith(expect.objectContaining({
      name: [session.name, [Validators.required]]
    }));
    expect(formBuilderSpy).toHaveBeenCalledWith(expect.objectContaining({
      teacher_id: [session.teacher_id, [Validators.required]]
    }));
  });
  it('should create a new session when submit button is clicked and not updating', () => {
    // Given
    const session: Session = {
      name: 'Test Session',
      date: new Date(),
      teacher_id: 123,
      description: 'Test description',
      users: []
    };

    jest.spyOn(TestBed.inject(SessionApiService), 'create').mockReturnValue(of(session));
    jest.spyOn(TestBed.inject(MatSnackBar), 'open');
    jest.spyOn(TestBed.inject(Router), 'navigate');

    component.onUpdate = false;
    component.sessionForm = new FormGroup({
      name: new FormControl(session.name),
      date: new FormControl(new Date(session.date)),
      teacher_id: new FormControl(session.teacher_id),
      description: new FormControl(session.description),
      users: new FormControl(session.users)
    });
    // When
    component.submit();

    // Then
    expect(TestBed.inject(SessionApiService).create).toHaveBeenCalledWith(session);
    expect(TestBed.inject(MatSnackBar).open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
    expect(TestBed.inject(Router).navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should update a session when submit button is clicked and updating', () => {
    // Given
    const session: Session = {
      name: 'Test Session',
      date: new Date(),
      teacher_id: 1,
      description: 'Test description',
      users: []
    };

    jest.spyOn(TestBed.inject(SessionApiService), 'update').mockReturnValue(of(session));
    jest.spyOn(TestBed.inject(MatSnackBar), 'open');
    jest.spyOn(TestBed.inject(Router), 'navigate');

    component.onUpdate = true;
    component.sessionForm = new FormGroup({
      name: new FormControl(session.name),
      date: new FormControl(session.date),
      teacher_id: new FormControl(session.teacher_id),
      description: new FormControl(session.description),
      users: new FormControl(session.users)
    });
    // When
    component.submit();

    // Then
    expect(TestBed.inject(SessionApiService).update).toHaveBeenCalledWith(session.id, session);
    expect(TestBed.inject(MatSnackBar).open).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
    expect(TestBed.inject(Router).navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should exit page when calling exitPage method', () => {
    // Given
    const message = 'Test message';

    jest.spyOn(TestBed.inject(MatSnackBar), 'open');
    jest.spyOn(TestBed.inject(Router), 'navigate');

    // When
   (component as any)['exitPage'](message);
    // Then
    expect(TestBed.inject(MatSnackBar).open).toHaveBeenCalledWith(message, 'Close', { duration: 3000 });
    expect(TestBed.inject(Router).navigate).toHaveBeenCalledWith(['sessions']);
  });
});
