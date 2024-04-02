import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { SessionInformation } from './interfaces/sessionInformation.interface';
import { of } from 'rxjs';
import { SessionService } from './services/session.service';
import { By } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { NgZone } from '@angular/core';

const mockSessionInformation : SessionInformation = {
  token: `e85c9ffdaeff0bf290b2eebffd25ff56255d0a2c163edf229ebb83b189334962
  22724c1dd101ed52d9d88ea1c71eab235a7a4dbd380539b5e779373627460acc
  09193a12b1e67899ad9c16ebf95df5a5ba15e4ac2f546d4780283caecabc6bbf
  6d431d8ac22a9895182017951cb17af4e8ce14ee68353be337803f60999558d2
  ebf88d87131f7f8e4641d0a16ac0f81a2ee807d7b6384fe0c2023acd925e51dc
  abd55b2f56bfb5ec5ca4e44e64cb02976adc3fbeaf60ff7d6a808fe3f1b5954b
  01bbafcda59eb4c4ada6c1af90eef515c5f32b44d3bcea1f3641ea9664324e1f
  18e124861170470ba6d707bf0cb778975d0307caf2761ebcf0ec50cea8d52e56
  4203a428e662e69f4129c8dfde2a2bf5aff449bb2d6beaaf032d7778d665da5a
  789f2ed26aaed7dbe298b48d0e8c0420743bf8f880025cfdf43a3ba64b03765e`,
  type: 'string',
  id: 1,
  username: 'string',
  firstName: 'string',
  lastName: 'string',
  admin: true,
}

const sessionServiceMock = {
  sessionInformation: mockSessionInformation,
  isLogged : false,
  $isLogged : jest.fn(() => of(false)),
  logIn : jest.fn((sessionInformation : SessionInformation) => (void 0)),
  logOut : jest.fn(() => (void 0)),
}

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let router : Router
  let ngZone : NgZone

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
      providers: [
        { provide: SessionService, useValue: sessionServiceMock },
      ],
    }).compileComponents();
    
    fixture = TestBed.createComponent(AppComponent);
    router = TestBed.inject(Router)
    ngZone = TestBed.inject(NgZone)
    component = fixture.componentInstance;
    fixture.detectChanges();
    jest.clearAllMocks()
  });

  it('should create the app', () => {
    expect(fixture).toBeTruthy();
  });

  describe('if the user is not logged', () => {
    it('should display a login and a register link', () => {
      expect(fixture.debugElement.queryAll(By.css('span.link')).length).toBe(2)
      expect(fixture.debugElement.query(By.css('span[routerlink="session"]'))).toBeFalsy()
      expect(fixture.debugElement.query(By.css('span[routerlink="me"]'))).toBeFalsy()
      expect(fixture.debugElement.query(By.css('span[routerlink="login"]'))).toBeTruthy()
      expect(fixture.debugElement.query(By.css('span[routerlink="register"]'))).toBeTruthy()
    });

    describe('and the login button is clicked', () => {
      it('should redirect the user to the login page', () => {
        jest.spyOn(router, 'navigateByUrl')
        const loginButton = fixture.debugElement.query(By.css('span[routerlink="login"]'))
        expect(router.navigateByUrl).not.toHaveBeenCalled()
        ngZone.run(() => {
          loginButton.triggerEventHandler('click', null)
        })
        fixture.detectChanges();
        expect(router.navigateByUrl).toHaveBeenCalledWith(router.parseUrl('login'), {"replaceUrl": false, "skipLocationChange": false, "state": undefined})
      })
    })

    describe('and the register button is clicked', () => {
      it('should redirect the user to the register page', () => {
        jest.spyOn(router, 'navigateByUrl')
        const registerButton = fixture.debugElement.query(By.css('span[routerlink="register"]'))
        expect(router.navigateByUrl).not.toHaveBeenCalled()
        ngZone.run(() => {
          registerButton.triggerEventHandler('click', null)
        })
        fixture.detectChanges();
        expect(router.navigateByUrl).toHaveBeenCalledWith(router.parseUrl('register'), {"replaceUrl": false, "skipLocationChange": false, "state": undefined})
      })
    })
  })

  describe('if the user is logged', () => {
    beforeAll(() => {
      sessionServiceMock.$isLogged = jest.fn(() => of(true))
    })

    beforeEach(() => jest.clearAllMocks())

    it('should display a sessions and a account link', () => {
      expect(fixture.debugElement.queryAll(By.css('span.link')).length).toBe(3)
      expect(fixture.debugElement.query(By.css('span[routerlink="login"]'))).toBeFalsy()
      expect(fixture.debugElement.query(By.css('span[routerlink="register"]'))).toBeFalsy()
      expect(fixture.debugElement.query(By.css('span[routerlink="sessions"]'))).toBeTruthy()
      expect(fixture.debugElement.query(By.css('span[routerlink="me"]'))).toBeTruthy()
    });

    describe('and the logout button is clicked', () => {
      it('should log you out and redirect you to the homepage', () => {
        jest.spyOn(router, 'navigate');
        const logoutButton = fixture.debugElement.queryAll(By.css('span.link'))[2]
        expect(sessionServiceMock.logOut).not.toHaveBeenCalled()
        expect(router.navigate).not.toHaveBeenCalled()
        ngZone.run(() => { /// !!!! why ngZone
          logoutButton.triggerEventHandler('click', null)
        })
        expect(sessionServiceMock.logOut).toHaveBeenCalled()
        expect(router.navigate).toHaveBeenCalledWith(['']);
      })
    })

    describe('and the session button is clicked', () => {
      it('should redirect the user to the sessions page', () => {
        jest.spyOn(router, 'navigateByUrl')
        const sessionButton = fixture.debugElement.query(By.css('span[routerlink="sessions"]'))
        expect(router.navigateByUrl).not.toHaveBeenCalled()
        ngZone.run(() => {
          sessionButton.triggerEventHandler('click', null)
        })
        fixture.detectChanges();
        expect(router.navigateByUrl).toHaveBeenCalledWith(router.parseUrl('sessions'), {"replaceUrl": false, "skipLocationChange": false, "state": undefined})
      })
    })

    describe('and the account button is clicked', () => {
      it('should redirect the user to the me page', () => {
        jest.spyOn(router, 'navigateByUrl')
        const accountButton = fixture.debugElement.query(By.css('span[routerlink="me"]'))
        expect(router.navigateByUrl).not.toHaveBeenCalled()
        ngZone.run(() => {
          accountButton.triggerEventHandler('click', null)
        })
        fixture.detectChanges();
        expect(router.navigateByUrl).toHaveBeenCalledWith(router.parseUrl('me'), {"replaceUrl": false, "skipLocationChange": false, "state": undefined})
      })
    })

  })
});

