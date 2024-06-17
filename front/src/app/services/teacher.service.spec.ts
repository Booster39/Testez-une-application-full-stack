import { HttpClientModule } from '@angular/common/http';
import {HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import { Teacher } from '../interfaces/teacher.interface';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpMock: HttpTestingController;
  let date: Date;
  let id : string = '1';
  let path : string = 'api/teacher';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule, HttpClientTestingModule
      ]
    });
    service = TestBed.inject(TeacherService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('it should get all teachers', () => {
    const mockTeachers: Teacher[] = [
      { id: 1, lastName: 'John Doe', firstName: 'Jane Doe', createdAt: date, updatedAt: date },
      { id: 2, firstName: 'Jane Doe', lastName: 'John Doe', createdAt: date, updatedAt: date }
    ];
    service.all().subscribe((teachers) => {
      expect(teachers).toEqual(mockTeachers);
    });

    const req = httpMock.expectOne(path);

    expect(req.request.method).toBe('GET');
    req.flush(mockTeachers);
  })

  it('it should get a teacher', () => {
    service.detail(id).subscribe();

    const req = httpMock.expectOne(`${path}/${id}`)

    expect(req.request.method).toBe("GET");
    req.flush({});
  })

});
