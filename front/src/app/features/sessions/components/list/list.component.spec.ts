import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { ListComponent } from './list.component';
import { Session } from '../../interfaces/session.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { SessionApiService } from '../../services/session-api.service';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';
import { DatePipe } from '@angular/common';

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

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let datePipe: DatePipe;

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
        providers: [{ provide: SessionService, useValue: mockSessionService }, { provide: SessionApiService, useValue: mockSessionAPIService }]
      })
        .compileComponents();

      fixture = TestBed.createComponent(ListComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
      datePipe = new DatePipe(`en-US`);
    });

    it('should be created', () => {
      expect(component).toBeTruthy();
    });
  })

  // Unit Test
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
      datePipe = new DatePipe(`en-US`);
    });

    it('should display no sessions cards', async () => {
      expect(component).toBeTruthy()
      expect(mockSessionAPIService.all).toHaveBeenCalled()
      expect(fixture.debugElement.queryAll(By.css('.item')).length).toBe(0)
    })
  })

  // Unit Test
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
      datePipe = new DatePipe(`en-US`);
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

  
});
