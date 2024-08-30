import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { MeComponent } from './me.component';
import { of, Observable } from 'rxjs';
import { UserService } from '../../services/user.service';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const mockSessionService = {
    logOut: jest.fn(),
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
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
  
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should back', () => {
    expect(component.back()).toEqual(window.history.back());
  })

  it('should delete user account', () => {
    //Given
    jest.spyOn(TestBed.inject(UserService), 'delete').mockReturnValue(of(null));
    jest.spyOn(TestBed.inject(MatSnackBar), 'open');

    //When
    component.delete();
    
    //Then
    expect(TestBed.inject(UserService).delete).toHaveBeenCalledWith(component['sessionService'].sessionInformation!.id.toString());
    expect(TestBed.inject(MatSnackBar).open).toHaveBeenCalledWith("Your account has been deleted !", 'Close', { duration: 3000 });
  });
});
