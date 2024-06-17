import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';


describe('AppComponent', () => {
  let app: AppComponent;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
     app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('it should be logged', () => {
    app.$isLogged().subscribe((isLogged) => expect(isLogged).toBeTruthy());
  });

  it('it should log out', () => {
    app.logout();
    app.$isLogged().subscribe((isLogged) => expect(isLogged).toBeFalsy());

  })
});
