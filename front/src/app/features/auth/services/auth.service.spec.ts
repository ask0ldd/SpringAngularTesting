import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import {AuthService } from './auth.service';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { of } from 'rxjs';
import { RegisterRequest } from '../interfaces/registerRequest.interface';

const sessionInformation : SessionInformation = {
  token: 
  `e85c9ffdaeff0bf290b2eebffd25ff56255d0a2c163edf229ebb83b189334962
  22724c1dd101ed52d9d88ea1c71eab235a7a4dbd380539b5e779373627460acc
  09193a12b1e67899ad9c16ebf95df5a5ba15e4ac2f546d4780283caecabc6bbf
  6d431d8ac22a9895182017951cb17af4e8ce14ee68353be337803f60999558d2
  ebf88d87131f7f8e4641d0a16ac0f81a2ee807d7b6384fe0c2023acd925e51dc
  abd55b2f56bfb5ec5ca4e44e64cb02976adc3fbeaf60ff7d6a808fe3f1b5954b
  01bbafcda59eb4c4ada6c1af90eef515c5f32b44d3bcea1f3641ea9664324e1f
  18e124861170470ba6d707bf0cb778975d0307caf2761ebcf0ec50cea8d52e56
  4203a428e662e69f4129c8dfde2a2bf5aff449bb2d6beaaf032d7778d665da5a
  789f2ed26aaed7dbe298b48d0e8c0420743bf8f880025cfdf43a3ba64b03765e`,
  type: 'type',
  id: 0,
  username: 'username',
  firstName: 'firstname',
  lastName: 'lastname',
  admin: true
}

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
      // !!! evaluations into a subscribe callback are ignored
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
      // !!! evaluations into a subscribe callback are ignored
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
