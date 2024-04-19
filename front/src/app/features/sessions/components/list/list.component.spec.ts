import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { ListComponent } from './list.component';
import { Session } from '../../interfaces/session.interface';
import { SessionApiService } from '../../services/session-api.service';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';
import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { NgZone } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { mockSessionInformationAdmin, mockYogaSession1, mockYogaSession2 } from 'src/app/testing/mockDatas';

const mockSessionInformation : SessionInformation = {...mockSessionInformationAdmin}

const yogaSessionMock1 : Session = {...mockYogaSession1}

const yogaSessionMock2 : Session = {...mockYogaSession2}

const leftMouseButton = 0

const datePipe = new DatePipe(`en-US`);

describe('ListComponent', () => {
  let router : Router
  let ngZone : NgZone
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  const mockSessionService = {
    sessionInformation: mockSessionInformation
  }

  const mockSessionAPIService = {
    all : jest.fn()
  }

  describe('when the component is initialized', () => {

    beforeAll(async () => {
      await TestBed.configureTestingModule({
        declarations: [ListComponent],
        imports: [HttpClientModule, MatCardModule, MatIconModule, RouterTestingModule,],
        providers: [{ provide: SessionService, useValue: mockSessionService }, { provide: SessionApiService, useValue: mockSessionAPIService }],
      })
        .compileComponents();

      fixture = TestBed.createComponent(ListComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    it('should be created', () => {
      expect(component).toBeTruthy();
    });
  })

  // --------
  // An empty array leads to no Sessions being displayed / Integration Test
  // --------

  describe('when an empty array is broadcasted by the .all() method of the sessionAPIService', () => {
    
    beforeEach(async () => {
      // mockSessionAPIService.all creates an observable that will broadcast an empty array
      mockSessionAPIService.all = jest.fn(() => of([]))
      await TestBed.configureTestingModule({
        declarations: [ListComponent],
        imports: [HttpClientModule, MatCardModule, MatIconModule],
        providers: [
          { provide: SessionService, useValue: mockSessionService }, 
          { provide: SessionApiService, useValue: mockSessionAPIService }
        ]
      }).compileComponents();

      fixture = TestBed.createComponent(ListComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    it('should display no sessions cards', async () => {
      // Assert
      expect(component).toBeTruthy()
      expect(mockSessionAPIService.all).toHaveBeenCalled()
      expect(fixture.debugElement.queryAll(By.css('.item')).length).toBe(0)
    })
  })

  // --------
  // An array of 2 Sessions leads to 2 Sessions being displayed / Integration Test
  // --------

  describe('when an array of two sessions is broadcasted by the .all() method of the sessionAPIService', () => {

    beforeEach(async () => {
      // for the following tests mockSessionAPIService.all returns an observable that will broadcast an array of two sessions
      mockSessionAPIService.all = jest.fn(() => of([{...yogaSessionMock1}, {...yogaSessionMock2}]))
      await TestBed.configureTestingModule({
        declarations: [ListComponent],
        imports: [RouterTestingModule, HttpClientModule, MatCardModule, MatIconModule],
        providers: [
          { provide: SessionService, useValue: mockSessionService }, 
          { provide: SessionApiService, useValue: mockSessionAPIService },
        ]
      }).compileComponents();

      fixture = TestBed.createComponent(ListComponent);
      router = TestBed.inject(Router) as Router
      ngZone = TestBed.inject(NgZone)
      component = fixture.componentInstance
      fixture.detectChanges()
      jest.restoreAllMocks()
    });

    it('should display two sessions cards', async () => {
      // Assert
      expect(fixture.debugElement.queryAll(By.css('mat-card.item')).length).toEqual(2)
      const card1 = fixture.debugElement.queryAll(By.css('mat-card.item'))[0] // !!!! improve with find
      const card2 = fixture.debugElement.queryAll(By.css('mat-card.item'))[1]
      expect(component).toBeTruthy()
      expect(mockSessionAPIService.all).toHaveBeenCalled()
      expect(card1.queryAll(By.css('mat-card-title'))[0].nativeElement.textContent).toEqual(yogaSessionMock1.name)
      expect(card2.queryAll(By.css('mat-card-title'))[0].nativeElement.textContent).toEqual(yogaSessionMock2.name)
      expect(card1.queryAll(By.css('.picture')).length).toBe(1);
      expect(card2.queryAll(By.css('.picture')).length).toBe(1);
      expect(card1.queryAll(By.css('.picture'))[0].nativeElement.src).toContain('assets/sessions.png')
      expect(card2.queryAll(By.css('.picture'))[0].nativeElement.src).toContain('assets/sessions.png')
      expect((card1.queryAll(By.css('mat-card-content p'))[0].nativeElement.textContent as string).trim()).toEqual(yogaSessionMock1.description)
      expect((card2.queryAll(By.css('mat-card-content p'))[0].nativeElement.textContent as string).trim()).toEqual(yogaSessionMock2.description)
      expect((card1.queryAll(By.css('mat-card-subtitle'))[0].nativeElement.textContent as string).trim()).toEqual('Session on ' + datePipe.transform(yogaSessionMock1.date, 'longDate'))
      expect((card2.queryAll(By.css('mat-card-subtitle'))[0].nativeElement.textContent as string).trim()).toEqual('Session on ' + datePipe.transform(yogaSessionMock2.date, 'longDate')) // using pipe
      expect((card1.queryAll(By.css('button span'))[0].nativeElement.textContent as string).trim()).toEqual('Detail')
      expect((card1.queryAll(By.css('button span'))[1].nativeElement.textContent as string).trim()).toEqual('Edit')
      expect((card2.queryAll(By.css('button span'))[0].nativeElement.textContent as string).trim()).toEqual('Detail')
      expect((card2.queryAll(By.css('button span'))[1].nativeElement.textContent as string).trim()).toEqual('Edit')
      const createButton = fixture.debugElement.query(By.css('button[routerlink="create"]'))
      expect(createButton).toBeTruthy()
    })
  })

  describe('when logged as an admin', () => {

    beforeEach(async () => {
      // for the following tests mockSessionAPIService.all returns an observable that will broadcast an array of two sessions
      mockSessionAPIService.all = jest.fn(() => of([{...yogaSessionMock1}, {...yogaSessionMock2}]))
      await TestBed.configureTestingModule({
        declarations: [ListComponent],
        imports: [RouterTestingModule, HttpClientModule, MatCardModule, MatIconModule],
        providers: [
          { provide: SessionService, useValue: mockSessionService }, 
          { provide: SessionApiService, useValue: mockSessionAPIService },
        ]
      }).compileComponents();

      fixture = TestBed.createComponent(ListComponent);
      router = TestBed.inject(Router) as Router
      ngZone = TestBed.inject(NgZone)
      component = fixture.componentInstance
      fixture.detectChanges()
      jest.restoreAllMocks()
    });

    // --------
    // When logged as an admin, the create session button should be displayed and functional / Integration Test
    // --------

    describe('when clicking on the create button', () => {
      it('should navigate to the new session form page', () => {
        jest.spyOn(router, 'navigateByUrl')
        const matCardHeader = fixture.debugElement.query(By.css('mat-card-header'))
        const createButton = matCardHeader.queryAll(By.css('button'))[0]
        expect(createButton).toBeTruthy()
        ngZone.run(() => {
          createButton.triggerEventHandler('click', { button: 0 })
        })
        fixture.detectChanges()
        expect(router.navigateByUrl).toHaveBeenCalledWith(router.parseUrl('create'), {"replaceUrl": false, "skipLocationChange": false, "state": undefined})
      })
    })

    // --------
    // The details button should be displayed and functional / Integration Test
    // --------

    describe('when clicking on the detail button', () => {
      it('should navigate to the detail page', () => {
        jest.spyOn(router, 'navigateByUrl')
        const card1 = fixture.debugElement.queryAll(By.css('mat-card.item'))[0]
        const detailButton = card1.queryAll(By.css('button'))[0]
        expect(detailButton).toBeTruthy()
        expect(router.navigateByUrl).not.toHaveBeenCalled()
        ngZone.run(() => {
          detailButton.triggerEventHandler('click', null)
        })
        fixture.detectChanges();
        expect(router.navigateByUrl).toHaveBeenCalledWith(router.parseUrl('detail/1'), {"replaceUrl": false, "skipLocationChange": false, "state": undefined})
      })
    })

    // --------
    // When logged as an admin, the edit button should be displayed and functional / Integration Test
    // --------

    describe('when clicking on the edit button', () => {
      it('should navigate to the edit form page', () => {
        jest.spyOn(router, 'navigateByUrl')
        const card1 = fixture.debugElement.queryAll(By.css('mat-card.item'))[0]
        const editButton = card1.queryAll(By.css('button'))[1]
        expect(editButton).toBeTruthy()
        expect(router.navigateByUrl).not.toHaveBeenCalled()
        ngZone.run(() => {
          editButton.triggerEventHandler('click', null)
        })
        fixture.detectChanges();
        expect(router.navigateByUrl).toHaveBeenCalledWith(router.parseUrl('update/1'), {"replaceUrl": false, "skipLocationChange": false, "state": undefined})
      })
    })

    // --------
    // Component.user should return the users session informations / Unit Test
    // --------

    describe('the component user getter', () => {
      it('should return the sessionInformations', () => {
        expect(JSON.stringify(component.user)).toBe(JSON.stringify(mockSessionInformation))
      })
    })

    // --------
    // After init, $session should broadcast 2 sessions / Unit Test
    // --------

    describe('once the component initialized', () => {
      beforeAll(() => {
        mockSessionAPIService.all = jest.fn(() => of([{...yogaSessionMock1}, {...yogaSessionMock2}]))
      })
      it('should broadcast the expected sessions through its sessions$ attribute', fakeAsync(() => {
        let subSessions : Session[] = []
        component.sessions$.subscribe(sessions => {
          subSessions = sessions
        })

        tick(); // Advance the fake time to trigger the subscription

        expect(JSON.stringify(subSessions[0])).toBe(JSON.stringify(yogaSessionMock1))
        expect(JSON.stringify(subSessions[1])).toBe(JSON.stringify(yogaSessionMock2))
      }))
    })

  })

});
