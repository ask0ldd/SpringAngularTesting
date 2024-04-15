import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';
import { of } from 'rxjs';

const session1 : Session = {
  id : 1,
  name : 'name1',
  description : 'description1',
  date : new Date("11/10/2023"),
  teacher_id : 1,
  users : [2, 3],
  createdAt : new Date(),
  updatedAt : new Date(),
}

const session2 : Session = {
  id : 2,
  name : 'name2',
  description : 'description2',
  date : new Date("12/10/2023"),
  teacher_id : 2,
  users : [4, 5],
  createdAt : new Date(),
  updatedAt : new Date(),
}

const pathService = 'api/session';

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpClient : HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ]
    });
    service = TestBed.inject(SessionApiService);
    httpClient = TestBed.inject(HttpClient);
    jest.resetAllMocks()
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // --------
  // Requesting All Yoga Sessions
  // --------

  describe('when the all method is called', () => {
    it('should send a request to the expected endpoint & an observable broadcasting an array of sessions should be returned', () => {
      const targetEndpoint = `${pathService}`

      jest.spyOn(httpClient, 'get').mockReturnValue(of([{...session1}, {...session2}]))

      expect(httpClient.get).not.toHaveBeenCalled()

      // [!] evaluations into a subscribe callback are ignored
      let subResponse
      service.all().subscribe((response) => {
        subResponse = response
      })
      expect(httpClient.get).toHaveBeenCalledWith(targetEndpoint)
      expect(JSON.stringify(subResponse)).toEqual(JSON.stringify([session1, session2]))
    });
  })

  // --------
  // Unit Test : Requestion the Yoga Session Details
  // --------

  describe('when the detail method is called', () => {
    it('should send a request to the expected endpoint & an observable broadcasting the expecte session should be returned', () => {
      const id = '1'
      const targetEndpoint = `${pathService}/${id}`

      jest.spyOn(httpClient, 'get').mockReturnValue(of({...session1}))

      expect(httpClient.get).not.toHaveBeenCalled()

      // [!] evaluations into a subscribe callback are ignored
      let subResponse
      service.detail(id).subscribe((response) => {
        subResponse = response
      })
      expect(httpClient.get).toHaveBeenCalledWith(targetEndpoint)
      expect(JSON.stringify(subResponse)).toEqual(JSON.stringify(session1))
    });
  })

  // --------
  // Unit Test : Delete a Yoga Session
  // --------

  describe('when the delete method is called', () => {
    it('should send a request to the expected endpoint & an observable broadcasting an empty object should be returned', () => {
      const id = '1'
      const targetEndpoint = `${pathService}/${id}`

      jest.spyOn(httpClient, 'delete').mockReturnValue(of({}))

      expect(httpClient.delete).not.toHaveBeenCalled()

      // [!] evaluations into a subscribe callback are ignored
      let subResponse
      service.delete(id).subscribe((response) => {
        subResponse = response
      })
      expect(httpClient.delete).toHaveBeenCalledWith(targetEndpoint)
      expect(subResponse).toEqual({})
    });
  })

  // --------
  // Unit Test : Create a Yoga Session
  // --------

  describe('when the create method is called with a session passed', () => {
    it('should send a request to the expected endpoint & an observable broadcasting this session should be returned', () => {
      const targetEndpoint = `${pathService}`

      jest.spyOn(httpClient, 'post').mockReturnValue(of(session1))

      expect(httpClient.post).not.toHaveBeenCalled()

      // [!] evaluations into a subscribe callback are ignored
      let subResponse
      service.create(session1).subscribe((response) => {
        subResponse = response
      })
      expect(httpClient.post).toHaveBeenCalledWith(targetEndpoint, session1)
      expect(subResponse).toEqual(session1)
    });
  })

  // --------
  // Unit Test : Update a Yoga Session
  // --------

  describe('when the update method is called', () => {
    it('should send a request to the expected endpoint & an observable broadcasting the new session should be returned', () => {
      const id = '1'
      const targetEndpoint = `${pathService}/${id}`

      jest.spyOn(httpClient, 'put').mockReturnValue(of(session1))

      expect(httpClient.put).not.toHaveBeenCalled()

      // [!] evaluations into a subscribe callback are ignored
      let subResponse
      service.update(id, session1).subscribe((response) => {
        subResponse = response
      })
      expect(httpClient.put).toHaveBeenCalledWith(targetEndpoint, session1)
      expect(subResponse).toEqual(session1)
    });
  })

  // --------
  // Unit Test : Participate to a Yoga Session
  // --------

  describe('when the participate method is called', () => {
    it('should send a request to the expected endpoint & void broadcasting observable should be returned', () => {
      const id = '1'
      const userId = '1'
      const targetEndpoint = `${pathService}/${id}/participate/${userId}`

      jest.spyOn(httpClient, 'post').mockReturnValue(of(void 0))

      expect(httpClient.post).not.toHaveBeenCalled()

      // [!] evaluations into a subscribe callback are ignored
      let subResponse
      service.participate(id, userId).subscribe((response) => {
        subResponse = response
      })
      expect(httpClient.post).toHaveBeenCalledWith(targetEndpoint, null)
      expect(subResponse).toEqual(void 0)
    });
  })

  // --------
  // Unit Test : Unsub from a Yoga Session
  // --------

  describe('when the unparticipate method is called', () => {
    it('should send a request to the expected endpoint & a void broadcasting observable should be returned', () => {
      const id = '1'
      const userId = '1'
      const targetEndpoint = `${pathService}/${id}/participate/${userId}`

      jest.spyOn(httpClient, 'delete').mockReturnValue(of(void 0))

      expect(httpClient.delete).not.toHaveBeenCalled()

      // [!] evaluations into a subscribe callback are ignored
      let subResponse
      service.unParticipate(id, userId).subscribe((response) => {
        subResponse = response
      })
      expect(httpClient.delete).toHaveBeenCalledWith(targetEndpoint)
      expect(subResponse).toEqual(void 0)
    });
  })

});
