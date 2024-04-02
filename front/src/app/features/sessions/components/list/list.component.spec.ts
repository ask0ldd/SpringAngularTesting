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

  // Unit Test
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

  // Integration Test
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
      })
        .compileComponents();

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

  // Integration Test
  describe('when an array of two sessions is broadcasted by the .all() method of the sessionAPIService', () => {
    beforeEach(async () => {
      // for the following tests mockSessionAPIService.all returns an observable that will broadcast an array of two sessions
      mockSessionAPIService.all = jest.fn(() => of([{...session1}, {...session2}]))
      await TestBed.configureTestingModule({
        declarations: [ListComponent],
        imports: [RouterTestingModule, HttpClientModule, MatCardModule, MatIconModule],
        providers: [
          { provide: SessionService, useValue: mockSessionService }, 
          { provide: SessionApiService, useValue: mockSessionAPIService },
        ]
      })
        .compileComponents();

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
      expect(card1.queryAll(By.css('mat-card-title'))[0].nativeElement.textContent).toEqual(session1.name)
      expect(card2.queryAll(By.css('mat-card-title'))[0].nativeElement.textContent).toEqual(session2.name)
      expect(card1.queryAll(By.css('.picture')).length).toBe(1);
      expect(card2.queryAll(By.css('.picture')).length).toBe(1);
      expect(card1.queryAll(By.css('.picture'))[0].nativeElement.src).toContain('assets/sessions.png')
      expect(card2.queryAll(By.css('.picture'))[0].nativeElement.src).toContain('assets/sessions.png')
      expect((card1.queryAll(By.css('mat-card-content p'))[0].nativeElement.textContent as string).trim()).toEqual(session1.description)
      expect((card2.queryAll(By.css('mat-card-content p'))[0].nativeElement.textContent as string).trim()).toEqual(session2.description)
      expect((card1.queryAll(By.css('mat-card-subtitle'))[0].nativeElement.textContent as string).trim()).toEqual('Session on ' + datePipe.transform(session1.date, 'longDate'))
      expect((card2.queryAll(By.css('mat-card-subtitle'))[0].nativeElement.textContent as string).trim()).toEqual('Session on ' + datePipe.transform(session2.date, 'longDate')) // using pipe
      expect((card1.queryAll(By.css('button span'))[0].nativeElement.textContent as string).trim()).toEqual('Detail')
      expect((card1.queryAll(By.css('button span'))[1].nativeElement.textContent as string).trim()).toEqual('Edit')
      expect((card2.queryAll(By.css('button span'))[0].nativeElement.textContent as string).trim()).toEqual('Detail')
      expect((card2.queryAll(By.css('button span'))[1].nativeElement.textContent as string).trim()).toEqual('Edit')
      const createButton = fixture.debugElement.query(By.css('button[routerlink="create"]'))
      expect(createButton).toBeTruthy()
    })

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

    // Unit Test
    describe('the component user getter', () => {
      it('should return the sessionInformations', () => {
        expect(JSON.stringify(component.user)).toBe(JSON.stringify(mockSessionInformation))
      })
    })

    // Unit Test
    describe('once the component initialized', () => {
      beforeAll(() => {
        mockSessionAPIService.all = jest.fn(() => of([{...session1}, {...session2}]))
      })
      it('should broadcast the expected sessions through its sessions$ attribute', fakeAsync(() => {
        let subSessions : Session[] = []
        component.sessions$.subscribe(sessions => {
          subSessions = sessions
        })

        tick(); // Advance the fake time to trigger the subscription

        expect(JSON.stringify(subSessions[0])).toBe(JSON.stringify(session1))
        expect(JSON.stringify(subSessions[1])).toBe(JSON.stringify(session2))
      }))
    })

  })

});
