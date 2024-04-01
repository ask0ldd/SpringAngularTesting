import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
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
import { NO_ERRORS_SCHEMA } from '@angular/compiler';

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

const datePipe = new DatePipe(`en-US`);

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }

  const mockSessionAPIService = {
    all : jest.fn()
  }

  // Unit Test
  describe('when the component is initialized', () => {

    beforeAll(async () => {
      await TestBed.configureTestingModule({
        declarations: [ListComponent],
        imports: [HttpClientModule, MatCardModule, MatIconModule],
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
    
    beforeAll(async () => {
      // mockSessionAPIService.all creates an observable that will broadcast an empty array
      mockSessionAPIService.all = jest.fn(() => of([]))
      await TestBed.configureTestingModule({
        declarations: [ListComponent],
        imports: [HttpClientModule, MatCardModule, MatIconModule],
        providers: [{ provide: SessionService, useValue: mockSessionService }, { provide: SessionApiService, useValue: mockSessionAPIService }]
      })
        .compileComponents();

      fixture = TestBed.createComponent(ListComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    it('should display no sessions cards', async () => {
      expect(component).toBeTruthy()
      expect(mockSessionAPIService.all).toHaveBeenCalled()
      expect(fixture.debugElement.queryAll(By.css('.item')).length).toBe(0)
    })
  })

  // Integration Test
  describe('when an array of two sessions is broadcasted by the .all() method of the sessionAPIService', () => {
    beforeAll(async () => {
      // mockSessionAPIService.all creates an observable that will broadcast an array of two sessions
      mockSessionAPIService.all = jest.fn(() => of([{...session1}, {...session2}]))
      await TestBed.configureTestingModule({
        declarations: [ListComponent],
        imports: [HttpClientModule, MatCardModule, MatIconModule],
        providers: [{ provide: SessionService, useValue: mockSessionService }, { provide: SessionApiService, useValue: mockSessionAPIService }]
      })
        .compileComponents();

      fixture = TestBed.createComponent(ListComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    it('should display two sessions cards', async () => {
      expect(component).toBeTruthy()
      expect(mockSessionAPIService.all).toHaveBeenCalled()
      expect(fixture.debugElement.queryAll(By.css('.item')).length).toEqual(2)
      const item1 = fixture.debugElement.queryAll(By.css('.item'))[0] // !!!! improve with find
      const item2 = fixture.debugElement.queryAll(By.css('.item'))[1]
      expect(item1.queryAll(By.css('mat-card-title'))[0].nativeElement.textContent).toEqual(session1.name)
      expect(item2.queryAll(By.css('mat-card-title'))[0].nativeElement.textContent).toEqual(session2.name)
      expect(item1.queryAll(By.css('.picture')).length).toBe(1);
      expect(item2.queryAll(By.css('.picture')).length).toBe(1);
      expect(item1.queryAll(By.css('.picture'))[0].nativeElement.src).toContain('assets/sessions.png')
      expect(item2.queryAll(By.css('.picture'))[0].nativeElement.src).toContain('assets/sessions.png')
      expect((item1.queryAll(By.css('mat-card-content p'))[0].nativeElement.textContent as string).trim()).toEqual(session1.description)
      expect((item2.queryAll(By.css('mat-card-content p'))[0].nativeElement.textContent as string).trim()).toEqual(session2.description)
      expect((item1.queryAll(By.css('mat-card-subtitle'))[0].nativeElement.textContent as string).trim()).toEqual('Session on ' + datePipe.transform(session1.date, 'longDate'))
      expect((item2.queryAll(By.css('mat-card-subtitle'))[0].nativeElement.textContent as string).trim()).toEqual('Session on ' + datePipe.transform(session2.date, 'longDate')) // using pipe
      expect((item1.queryAll(By.css('button span'))[0].nativeElement.textContent as string).trim()).toEqual('Detail')
      expect((item1.queryAll(By.css('button span'))[1].nativeElement.textContent as string).trim()).toEqual('Edit')
      expect((item2.queryAll(By.css('button span'))[0].nativeElement.textContent as string).trim()).toEqual('Detail')
      expect((item2.queryAll(By.css('button span'))[1].nativeElement.textContent as string).trim()).toEqual('Edit')
    })
  })

  // Unit Test : Clicking the detail button should trigger action

  // Unit Test : Clicking the edit button should trigger action

  // clicking on the create button

  // Unit Test
  describe('the component user getter', () => {
    it('should return the sessionInformations', () => {
      expect(JSON.stringify(component.user)).toBe(JSON.stringify({ admin: true }))
    })
  })

  // Unit Test
  describe('once the component initialized', () => {
    beforeAll(() => {
      mockSessionAPIService.all = jest.fn(() => of([{...session1}, {...session2}]))
    })
    it('should broadcast the expected sessions through its sessions$ attribute', () => {
      component.sessions$.subscribe(sessions => {
        expect(JSON.stringify(sessions[0])).toBe(JSON.stringify(session1))
        expect(JSON.stringify(sessions[1])).toBe(JSON.stringify(session2))
      })
    })
  })

});
