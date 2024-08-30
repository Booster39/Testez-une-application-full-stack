import { HttpClientModule, HttpResponse } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { Router, ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SessionService } from '../../../../services/session.service';
import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import { TeacherService } from 'src/app/services/teacher.service';
import { Session } from '../../interfaces/session.interface';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let routerSpy: any;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  };

  const mockActivatedRoute = {
    snapshot: {
      paramMap: {
        get: jest.fn().mockReturnValue('123')  // Mock the sessionId from the route params
      }
    }
  };

  beforeEach(async () => {
    routerSpy = { navigate: jest.fn() };

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: routerSpy },
        { provide: ActivatedRoute, useValue: mockActivatedRoute }  // Provide the mock ActivatedRoute
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate back', () => {
    //Given
    jest.spyOn(window.history, 'back').mockImplementation(() => {
      routerSpy.navigate(['/sessions']);
    });

    //When
    component.back();

    //Then
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should call delete and navigate to sessions', () => {
    //Given
    jest.spyOn(TestBed.inject(SessionApiService), 'delete').mockReturnValue(of(new HttpResponse<any>()));
    jest.spyOn(TestBed.inject(MatSnackBar), 'open');

    //When
    component.delete();

    //Then
    expect(TestBed.inject(SessionApiService).delete).toHaveBeenCalledWith(component.sessionId);
    expect(TestBed.inject(MatSnackBar).open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });

    // Wait for the router navigation to complete
    fixture.whenStable().then(() => {
      expect(routerSpy.navigate).toHaveBeenCalledWith(['sessions']);
    });
  });

  it('should call participate and fetch session', () => {
    jest.spyOn(TestBed.inject(SessionApiService), 'participate').mockReturnValue(of((void 0)));
  
    component.participate();
  
    expect(TestBed.inject(SessionApiService).participate).toHaveBeenCalledWith(
      component.sessionId,
      component.userId
    );
  
    // Wait for the fetchSession method to be called
    fixture.whenStable().then(() => {
      expect(TestBed.inject(SessionApiService).detail).toHaveBeenCalledWith(
        component.sessionId
      );
    });
  });

  it('should call unParticipate and fetch session', () => {
    jest.spyOn(TestBed.inject(SessionApiService), 'unParticipate').mockReturnValue(of(void 0));
  
    component.unParticipate();
  
    expect(TestBed.inject(SessionApiService).unParticipate).toHaveBeenCalledWith(
      component.sessionId,
      component.userId
    );
  
    // Wait for the fetchSession method to be called
    fixture.whenStable().then(() => {
      expect(TestBed.inject(SessionApiService).detail).toHaveBeenCalledWith(
        component.sessionId
      );
    });
  });

  it('should fetch session and set session', () => {
    const mockSession: Session = {
      id: 1,
      teacher_id: 2,
      users: [1, 3],
      name: '',
      description: '',
      date: new Date(),
      createdAt: new Date(),
      updatedAt: new Date()
    };
  
    jest.spyOn(TestBed.inject(SessionApiService), 'detail').mockReturnValue(of(mockSession));

    (component as any).fetchSession();
  
    expect(TestBed.inject(SessionApiService).detail).toHaveBeenCalledWith(component.sessionId);
  
    // Wait for the session to be set
    fixture.whenStable().then(() => {
      expect(component.session).toEqual(mockSession);
    });
  });
});