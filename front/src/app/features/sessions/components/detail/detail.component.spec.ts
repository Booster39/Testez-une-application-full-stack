import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 

import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { Router } from '@angular/router';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let sessionApiService: SessionApiService;
  let matSnackBarMock: MatSnackBar;
  let routerMock: Router;


  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent], 
      providers: [{ provide: SessionApiService, useValue: sessionApiService },
        { provide: MatSnackBar, useValue: matSnackBarMock },
        { provide: Router, useValue: routerMock }
      ],
    })
      .compileComponents();
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  
  it('should back', () => {
    expect(component.back()).toEqual(window.history.back());
  })

  it('it should delete', () => {
    component.delete();

    expect(sessionApiService.delete).toHaveBeenCalledWith('1');
    expect(matSnackBarMock.open).lastCalledWith('Session deleted !', 'Close', { duration: 3000 });
    expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
  })

  it('should participate', () => {
    component.sessionId = '1'
    component.userId = '2'
    component.participate()

    expect(sessionApiService.participate).toHaveBeenCalledWith(component.sessionId, component.userId);
  })
});

