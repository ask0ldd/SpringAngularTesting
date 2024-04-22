import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';
import { mockSessionInformationAdmin } from '../testing/mockDatas';

const mockSessionInformation : SessionInformation = {...mockSessionInformationAdmin}

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
    service.sessionInformation = undefined
    service.isLogged = false
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // --------
  // $logged broadcasting False by default / Unit Test
  // --------

  describe('when isLogged = false', () => {
    test('$logged() should broadcast false', () => {
      let subValue
      service.$isLogged().subscribe(value => {
        subValue = value
      })
      expect(subValue).toBeFalsy()
    });
  })

  // --------
  // Login / Unit Test
  // --------

  describe('when the logIn method is called', () => {
    it('should update the session informations with the expect values & isLogged$ should be broadcasting true', () => {
      service.logIn(mockSessionInformation)
      expect(service.isLogged).toBeTruthy()
      expect(JSON.stringify(service.sessionInformation)).toBe(JSON.stringify(mockSessionInformation))
      // [!] evaluations into a subscribe callback are ignored
      // $logged() should broadcast true since islogged = true and next()
      let subValue
      service.$isLogged().subscribe(value => {
        subValue = value
      })
      expect(subValue).toBeTruthy()
    });
  })

  // --------
  // Logout / Unit Test
  // --------

  describe('when the logOut is called', () => {
    it('should update the session informations to undefined & isLogged$ should be broadcasting false', () => {
      service.logOut()
      expect(service.isLogged).toBeFalsy()
      expect(service.sessionInformation).toBeUndefined()
      // [!] evaluations into a subscribe callback are ignored
      // $logged() should broadcast false since islogged = false and next()
      let subValue
      service.$isLogged().subscribe(value => {
        subValue = value
      })
      expect(subValue).toBeFalsy()
    });
  })
});
