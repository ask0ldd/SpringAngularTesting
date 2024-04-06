import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed, tick } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { Session } from '../../interfaces/session.interface';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from 'src/app/services/teacher.service';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe, TitleCasePipe, UpperCasePipe } from '@angular/common';
import { Teacher } from 'src/app/interfaces/teacher.interface';

const mockSession : Session = {
  id : 1,
  name : 'name',
  description : 'description',
  date : new Date("10/10/2023"),
  teacher_id : 1,
  users : [2, 3],
  createdAt : new Date(),
  updatedAt : new Date(),
}

const teacher : Teacher = {
  id: 1,
  lastName: "lastname",
  firstName: "firstname",
  createdAt: new Date(),
  updatedAt: new Date(),
}

const titleCasePipe = new TitleCasePipe();
const datePipe = new DatePipe(`en-US`);
const upperCasePipe = new UpperCasePipe();

// !!! admin false : participate // unparticipate
// !!! admin true : delete
// session doesn't exist

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let service: SessionService;

  const mockTeacherService = {
    detail : jest.fn(() => of(teacher))
  }

  const snackBarMock = {
    open : jest.fn()
  }

  const activatedRouteMock = {
    snapshot : {
      paramMap : {
        get : (id : any) => 1
      }
    }
  }

  const routerMock = {
    navigate : jest.fn((commands : string[]) => null)
  }

  describe('As a regular user', () => {

    const userId = 1

    const mockSessionService = {
      sessionInformation: {
        admin: false,
        id: userId
      }
    }

    const mockSessionAPIService = {
      detail : jest.fn(() => of(mockSession)),
      participate : jest.fn(() => {
        // adds the current user to the mocked session participants
        if(!mockSession.users.includes(userId)) mockSession.users.push(userId)
        return of(mockSession)
      }),
      unParticipate : jest.fn(() => {
        if(mockSession.users.includes(userId)) mockSession.users.pop()
        return of(mockSession)
      }), 
      delete : jest.fn(() => of(void 0)),
    }

    beforeEach(async () => {
      await TestBed.configureTestingModule({ // added imports and providers
        imports: [
          RouterTestingModule,
          HttpClientModule,
          MatCardModule,
          MatIconModule,
          MatFormFieldModule,
          MatInputModule,
          ReactiveFormsModule, 
          MatSnackBarModule,
          MatSelectModule,
          ReactiveFormsModule
        ],
        declarations: [DetailComponent], 
        providers: [{ provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue : mockSessionAPIService},
        { provide: TeacherService, useValue : mockTeacherService},
        { provide: MatSnackBar, useValue : snackBarMock },
        { provide: ActivatedRoute, useValue : activatedRouteMock }, // used to mock the function returning the url sessionId param
      ]
      })
        .compileComponents();
      service = TestBed.inject(SessionService);
      fixture = TestBed.createComponent(DetailComponent);
      component = fixture.componentInstance;
      
      fixture.detectChanges();

      // reset the number of calls associated to all mocks
      jest.clearAllMocks()
    });

    it('should create the component', () => {
      expect(component).toBeTruthy();
    })

    it('should display all the sessions datas', () => {
      // Arrange
      const attendeesnDateContainer = fixture.debugElement.query(By.css('mat-card-content'))
      const spans = attendeesnDateContainer.queryAll(By.css('span'))
      // Assert
      expect(fixture.debugElement.query(By.css('h1')).nativeElement.textContent).toEqual(titleCasePipe.transform(mockSession.name))
      expect(fixture.debugElement.queryAll(By.css('.picture'))[0].nativeElement.src).toContain('assets/sessions.png')
      expect(fixture.debugElement.query(By.css('.description')).nativeElement.textContent).toContain(mockSession.description)
      expect(fixture.debugElement.query(By.css('.created')).nativeElement.textContent).toContain(datePipe.transform(mockSession.createdAt,'longDate'))
      expect(fixture.debugElement.query(By.css('.updated')).nativeElement.textContent).toContain(datePipe.transform(mockSession.updatedAt,'longDate'))
      expect(spans[0].nativeElement.textContent).toContain(mockSession.users.length + ' attendees')
      expect(spans[1].nativeElement.textContent).toContain(datePipe.transform(mockSession.date,'longDate'))
      expect(fixture.debugElement.query(By.css('mat-card-subtitle')).nativeElement.textContent).toContain(component.teacher?.firstName + ' ' + upperCasePipe.transform(component.teacher?.lastName))
    })

    // Unit Test : Back button
    it('should go back in history when clicking on the back button', () => {
      // Arrange
      const windowHistorySpy = jest.spyOn(window.history, 'back')
      const backButton = fixture.debugElement.query(By.css('button[mat-icon-button]'))
      // Act
      backButton.triggerEventHandler('click', null)
      // Assert
      expect(windowHistorySpy).toHaveBeenCalled()
    })

    // Unit Test : Participate button
    describe('as a non participant to the session', () => {
      it('should display a participate button which should be calling sessionAPIservice.participate()', () => {
        // Arrange
        // init to be sure the user is not already a participant
        mockSessionAPIService.unParticipate()
        const buttons = fixture.debugElement.queryAll(By.css('button'))
        const cardButtons = fixture.debugElement.queryAll(By.css('mat-card-title button'))
        const participateButton = cardButtons[1]
        // PreAssert
        expect(buttons.length).toBe(2)
        expect(mockSessionAPIService.detail).not.toHaveBeenCalled()
        expect(mockSessionAPIService.participate).not.toHaveBeenCalled()
        // Act
        participateButton.triggerEventHandler('click', null)
        // Assert
        expect(mockSessionAPIService.participate).toHaveBeenCalled()
        expect(mockSession.users.includes(userId)).toBeTruthy()
        expect(mockSessionAPIService.detail).toHaveBeenCalledTimes(1)
      })
    })

    // Unit Test : Unparticipate button
    describe('as a participant to the session', () => {
      it('should display a do not participate button  which should be calling sessionAPIservice.unparticipate()', () => {
        // Arrange
        // init to be sure the user is a participant
        mockSessionAPIService.participate()
        const buttons = fixture.debugElement.queryAll(By.css('button'))
        const cardButtons = fixture.debugElement.queryAll(By.css('mat-card-title button'))
        const unparticipateButton = cardButtons[1]
        // PreAssert
        expect(buttons.length).toBe(2)
        expect(mockSessionAPIService.detail).not.toHaveBeenCalled()
        expect(mockSessionAPIService.unParticipate).not.toHaveBeenCalled()
        // Act
        unparticipateButton.triggerEventHandler('click', null)
        // Assert
        expect(mockSessionAPIService.unParticipate).toHaveBeenCalled()
        expect(mockSession.users.includes(userId)).toBeFalsy()
        expect(mockSessionAPIService.detail).toHaveBeenCalledTimes(1)
      })
    })


  })

  describe('As an admin', () => {

    const userId = 1

    const mockSessionService = {
      sessionInformation: {
        admin: true,
        id: userId
      }
    }

    const mockSessionAPIService = {
      detail : jest.fn(() => of(mockSession)),
      participate : jest.fn(() => {
        // adds the current user to the mockSession participants
        if(!mockSession.users.includes(userId)) mockSession.users.push(userId)
        return of(mockSession)
      }),
      unParticipate : jest.fn(() => {
        // adds the current user out of the mockSession participants
        if(mockSession.users.includes(userId)) mockSession.users.pop()
        return of(mockSession)
      }), 
      delete : jest.fn(() => of(void 0)),
    }

    beforeEach(async () => {
      await TestBed.configureTestingModule({ // added imports and providers
        imports: [
          RouterTestingModule,
          HttpClientModule,
          MatCardModule,
          MatIconModule,
          MatFormFieldModule,
          MatInputModule,
          ReactiveFormsModule, 
          MatSnackBarModule,
          MatSelectModule,
          ReactiveFormsModule
        ],
        declarations: [DetailComponent], 
        providers: [{ provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue : mockSessionAPIService},
        { provide: TeacherService, useValue : mockTeacherService},
        { provide: MatSnackBar, useValue : snackBarMock },
        { provide: ActivatedRoute, useValue : activatedRouteMock },
        { provide: Router, useValue: routerMock },
      ]
      })
        .compileComponents();
      service = TestBed.inject(SessionService);
      fixture = TestBed.createComponent(DetailComponent);
      component = fixture.componentInstance;
      
      fixture.detectChanges();
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    })

    // Unit Test : Delete button
    it('should display a delete button which should be calling sessionAPIservice.delete()', () => {
      // Arrange
      const deleteButton = fixture.debugElement.queryAll(By.css('button[color="warn"]'))[0]
      // Act
      deleteButton.triggerEventHandler('click', null)
      // Assert
      expect(mockSessionAPIService.delete).toHaveBeenCalledWith(mockSession.id)
      expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']) 
    })
  })
});

