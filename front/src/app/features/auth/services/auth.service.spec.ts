import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import {AuthService } from './auth.service';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { of } from 'rxjs';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { mockSessionInformationAdmin } from 'src/app/testing/mockDatas';

const sessionInformation : SessionInformation = {...mockSessionInformationAdmin}

describe('AuthService', () => {
  let service: AuthService;
  let httpClient: HttpClient;
  const pathService = 'api/auth'

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ],
      providers: [AuthService],
    });
    service = TestBed.inject(AuthService);
    httpClient = TestBed.inject(HttpClient);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // --------
  // Register A New User / Unit Test
  // --------

  describe('when the register method receives a registerRequest obj', () => {
    it('should send a request to the expected endpoint & return an observable broadcasting void', () => {
      // Arrange
      const registerRequest: RegisterRequest = {
        email: "email@email.com",
        firstName: 'firstname',
        lastName: 'lastname',
        password: "password"
      }
      const targetEndpoint = `${pathService}/register`
      jest.spyOn(httpClient, 'post').mockReturnValue(of(void 0))
      // Act
      // [!] evaluations into a subscribe callback are ignored
      let subResponse
      service.register(registerRequest).subscribe((response) => {
        subResponse = response
      })
      // Assert
      expect(httpClient.post).toHaveBeenCalledWith(targetEndpoint, registerRequest)
      expect(subResponse).toEqual(void 0)
    });
  })

  // --------
  // Login A New User / Unit Test
  // --------

  describe('when login method receives a loginRequest obj', () => {
    it('should send a request to the expected endpoint & return an observable broadcasting a session information', () => {
      // Arrange
      const loginRequest: LoginRequest = {
        email: "email@email.com",
        password: "password"
      }
      const targetEndpoint = `${pathService}/login`
      jest.spyOn(httpClient, 'post').mockReturnValue(of(sessionInformation))
      // Act
      // [!] evaluations into a subscribe callback are ignored
      let subResponse
      service.login(loginRequest).subscribe((response) => {
        subResponse = response
      })
      // Assert
      expect(httpClient.post).toHaveBeenCalledWith(targetEndpoint, loginRequest)
      expect(subResponse).toEqual(sessionInformation)
    });
  })

});
