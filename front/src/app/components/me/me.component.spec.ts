import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { expect } from '@jest/globals';
import { MeComponent } from './me.component';
import { UserService } from 'src/app/services/user.service';
import { Route, Router } from '@angular/router';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let sessionServiceMock: SessionService;
  let matSnackBarMock: MatSnackBar;
  let userServiceMock: UserService;
  let routerMock: Router;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [{ provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: userServiceMock },
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: MatSnackBar, useValue: matSnackBarMock },
        { provide: Router, useValue: routerMock }
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    fixture.detectChanges();
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should back', () => {
    expect(component.back()).toEqual(window.history.back());
  })

  it('it should delete', () => {
    component.delete();

    expect(userServiceMock.delete).toHaveBeenCalledWith('1');
    expect(matSnackBarMock.open).lastCalledWith("Your account has been deleted !", 'Close', { duration: 3000 });
    expect(sessionServiceMock.logOut).toHaveBeenCalled();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/']);
  })
});
