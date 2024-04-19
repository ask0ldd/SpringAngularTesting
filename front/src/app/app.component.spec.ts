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
import { mockSessionInformationAdmin } from './testing/mockDatas';

const mockSessionInformation : SessionInformation = {...mockSessionInformationAdmin}

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

  // --------
  // User not Logged -> Login & Register links in Header / Integration Test
  // --------

  describe('when the user is not logged', () => {
    it('should display a login and a register link', () => {
      expect(fixture.debugElement.queryAll(By.css('span.link')).length).toBe(2)
      expect(fixture.debugElement.query(By.css('span[routerlink="session"]'))).toBeFalsy()
      expect(fixture.debugElement.query(By.css('span[routerlink="me"]'))).toBeFalsy()
      expect(fixture.debugElement.query(By.css('span[routerlink="login"]'))).toBeTruthy()
      expect(fixture.debugElement.query(By.css('span[routerlink="register"]'))).toBeTruthy()
    });

    // --------
    // Clicking on Login / Integration Test
    // --------

    describe('when the login button is clicked', () => {
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

    // --------
    // Clicking on Register / Integration Test
    // --------  

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

  describe('when the user is logged', () => {
    beforeAll(() => {
      sessionServiceMock.$isLogged = jest.fn(() => of(true))
    })

    beforeEach(() => jest.clearAllMocks())

    // --------
    // Logged -> Account | Me & Sessions Links in Header / Integration Test
    // --------

    it('should display a sessions link and a account link', () => {
      expect(fixture.debugElement.queryAll(By.css('span.link')).length).toBe(3)
      expect(fixture.debugElement.query(By.css('span[routerlink="login"]'))).toBeFalsy()
      expect(fixture.debugElement.query(By.css('span[routerlink="register"]'))).toBeFalsy()
      expect(fixture.debugElement.query(By.css('span[routerlink="sessions"]'))).toBeTruthy()
      expect(fixture.debugElement.query(By.css('span[routerlink="me"]'))).toBeTruthy()
    });

    // --------
    // Clicking on Logout / Integration Test
    // --------

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

    // --------
    // Clicking on the Session Link / Integration Test
    // --------

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

    // --------
    // Clicking on Account | Me / Integration Test
    // --------

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

