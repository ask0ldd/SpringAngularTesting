import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';
import { of } from 'rxjs';
import { mockYogaSession1, mockYogaSession2 } from 'src/app/testing/mockDatas';

const yogaSessionMock1 : Session = {...mockYogaSession1}

const yogaSessionMock2 : Session = {...mockYogaSession2}

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
  // Requesting All Yoga Sessions / Unit Test
  // --------

  describe('when the all method is called', () => {
    it('should send a request to the expected endpoint & an observable broadcasting an array of sessions should be returned', () => {
      const targetEndpoint = `${pathService}`

      jest.spyOn(httpClient, 'get').mockReturnValue(of([{...yogaSessionMock1}, {...yogaSessionMock2}]))

      expect(httpClient.get).not.toHaveBeenCalled()

      // [!] evaluations into a subscribe callback are ignored
      let subResponse
      service.all().subscribe((response) => {
        subResponse = response
      })
      expect(httpClient.get).toHaveBeenCalledWith(targetEndpoint)
      expect(JSON.stringify(subResponse)).toEqual(JSON.stringify([yogaSessionMock1, yogaSessionMock2]))
    });
  })

  // --------
  // Requestion the Yoga Session Details / Unit Test
  // --------

  describe('when the detail method is called', () => {
    it('should send a request to the expected endpoint & an observable broadcasting the expecte session should be returned', () => {
      const id = '1'
      const targetEndpoint = `${pathService}/${id}`

      jest.spyOn(httpClient, 'get').mockReturnValue(of({...yogaSessionMock1}))

      expect(httpClient.get).not.toHaveBeenCalled()

      // [!] evaluations into a subscribe callback are ignored
      let subResponse
      service.detail(id).subscribe((response) => {
        subResponse = response
      })
      expect(httpClient.get).toHaveBeenCalledWith(targetEndpoint)
      expect(JSON.stringify(subResponse)).toEqual(JSON.stringify(yogaSessionMock1))
    });
  })

  // --------
  // Delete a Yoga Session / Unit Test
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
  // Create a Yoga Session / Unit Test
  // --------

  describe('when the create method is called with a session passed', () => {
    it('should send a request to the expected endpoint & an observable broadcasting this session should be returned', () => {
      const targetEndpoint = `${pathService}`

      jest.spyOn(httpClient, 'post').mockReturnValue(of(yogaSessionMock1))

      expect(httpClient.post).not.toHaveBeenCalled()

      // [!] evaluations into a subscribe callback are ignored
      let subResponse
      service.create(yogaSessionMock1).subscribe((response) => {
        subResponse = response
      })
      expect(httpClient.post).toHaveBeenCalledWith(targetEndpoint, yogaSessionMock1)
      expect(subResponse).toEqual(yogaSessionMock1)
    });
  })

  // --------
  // Update a Yoga Session / Unit Test
  // --------

  describe('when the update method is called', () => {
    it('should send a request to the expected endpoint & an observable broadcasting the new session should be returned', () => {
      const id = '1'
      const targetEndpoint = `${pathService}/${id}`

      jest.spyOn(httpClient, 'put').mockReturnValue(of(yogaSessionMock1))

      expect(httpClient.put).not.toHaveBeenCalled()

      // [!] evaluations into a subscribe callback are ignored
      let subResponse
      service.update(id, yogaSessionMock1).subscribe((response) => {
        subResponse = response
      })
      expect(httpClient.put).toHaveBeenCalledWith(targetEndpoint, yogaSessionMock1)
      expect(subResponse).toEqual(yogaSessionMock1)
    });
  })

  // --------
  // Participate to a Yoga Session / Unit Test
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
  // Unsub from a Yoga Session / Unit Test
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
