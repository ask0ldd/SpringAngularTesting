import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { User } from '../interfaces/user.interface';
import { of } from 'rxjs';

const pathService = 'api/user';

const mockUser : User = {
  id: 1,
  email: 'email@email.com',
  lastName: 'lastname',
  firstName: 'firstname',
  admin: false,
  password: 'password',
  createdAt: new Date(),
  updatedAt: new Date(),
}

describe('UserService', () => {
  let service: UserService;
  let httpClient : HttpClient

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ]
    });
    service = TestBed.inject(UserService);
    httpClient = TestBed.inject(HttpClient)
    jest.resetAllMocks()
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('when the getById is called with a user id passed to it', () => {
    it('should send a request to the expected endpoint & return an observable broadcasting the infos of target user', () => {
      const userId = "1"
      const targetEndpoint = `${pathService}/${userId}`

      jest.spyOn(httpClient, 'get').mockReturnValue(of(mockUser))

      // !!! evaluations into a subscribe callback are ignored
      let subResponse
      service.getById(userId).subscribe((response) => {
        subResponse = response
      })
      expect(httpClient.get).toHaveBeenCalledWith(targetEndpoint)
      expect(JSON.stringify(subResponse)).toEqual(JSON.stringify(mockUser))
    })
  })

  describe('when the delete method is called with an id passed to it', () => {
    it('should send a request to the expected endpoint & return an observer broadcasting any value', () => {
      const teacherId = '1'
      const targetEndpoint = `${pathService}/${teacherId}`

      jest.spyOn(httpClient, 'delete').mockReturnValue(of(void 0))

      // !!! evaluations into a subscribe callback are ignored
      let subResponse
      service.delete(teacherId).subscribe((response) => {
        subResponse = response
      })
      expect(httpClient.delete).toHaveBeenCalledWith(targetEndpoint)
      expect(subResponse).toEqual(void 0)
    })
  })
});
