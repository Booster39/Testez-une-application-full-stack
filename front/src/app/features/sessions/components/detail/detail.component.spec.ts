import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { Router, ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SessionService } from '../../../../services/session.service';
import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';

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
    // Mock window.history.back to call router.navigate
    jest.spyOn(window.history, 'back').mockImplementation(() => {
      routerSpy.navigate(['/sessions']);
    });

    component.back();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should call delete and navigate to sessions', () => {
    jest.spyOn(TestBed.inject(SessionApiService), 'delete').mockReturnValue(of({}));
    jest.spyOn(TestBed.inject(MatSnackBar), 'open');

    component.delete();

    expect(TestBed.inject(SessionApiService).delete).toHaveBeenCalledWith(component.sessionId);
    expect(TestBed.inject(MatSnackBar).open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });

    // Wait for the router navigation to complete
    fixture.whenStable().then(() => {
      expect(routerSpy.navigate).toHaveBeenCalledWith(['sessions']);
    });
  });
});
