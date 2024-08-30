import { HttpClientModule } from '@angular/common/http';
import {HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;
  let path : string = 'api/session';
  let id : string = '1';
  let userId : string = '2';
  const session1: Session = {
    id: 1,
    name: 'Mathematics 101',
    description: 'Introduction to basic mathematics concepts.',
    date: new Date('2024-09-01T10:00:00Z'),
    teacher_id: 1001,
    users: [2001, 2002, 2003],
    createdAt: new Date('2024-01-01T00:00:00Z'),
    updatedAt: new Date('2024-06-01T00:00:00Z')
  };
  
  const session2: Session = {
    name: 'Physics 101',
    description: 'Introduction to basic physics principles.',
    date: new Date('2024-09-02T10:00:00Z'),
    teacher_id: 1002,
    users: [2004, 2005, 2006]
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule, HttpClientTestingModule
      ]
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('it should get all sessions', () => {
    const mockSessions: Session[] = [
      session1,
      session2
    ]
    service.all().subscribe((sessions) => {
      expect(sessions).toEqual(mockSessions);
    });

    const req = httpMock.expectOne(path);

    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);
  })

  it('it should get a session', () => {
    service.detail(id).subscribe((session) => {
      expect(session).toEqual(session1);});

    const req = httpMock.expectOne(`${path}/${id}`)

    expect(req.request.method).toBe("GET");
    req.flush(session1);
  })

  it('it should delete session by id', () => {
    service.delete(id).subscribe((session) => {
      expect(session).toBeUndefined();});

    const req = httpMock.expectOne(`${path}/${id}`);

    expect(req.request.method).toBe('DELETE');
    req.flush({})
  })

  it('it should create session', () => {
    service.create(session1).subscribe((createdSession) => {
      expect(createdSession).toEqual(session1)});

    const req = httpMock.expectOne(path);

    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(session1);
    req.flush(session1)
  })

  it('it should update session', () => {
    service.update(id, session1).subscribe((updatedSession) => {
      expect(updatedSession).toEqual(session1)});

    const req = httpMock.expectOne(`${path}/${id}`);

    expect(req.request.method).toBe('PUT');
    req.flush(session1)
  })

  it('it should participe to the session', () => {
    service.participate(id, userId).subscribe((response) => {
      expect(response).toBeUndefined()});

    const req = httpMock.expectOne(`${path}/${id}/participate/${userId}`);

    expect(req.request.method).toBe('POST');
    req.flush({})
  })

  it('it should unparticipe to the session', () => {
    service.unParticipate(id, userId).subscribe((response) => {
      expect(response).toBeUndefined()});

    const req = httpMock.expectOne(`${path}/${id}/participate/${userId}`);

    expect(req.request.method).toBe('DELETE');
    req.flush({})
  })
});
