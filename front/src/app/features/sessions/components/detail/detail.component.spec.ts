import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
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
import { DebugElement } from '@angular/core';

const session : Session = {
  id : 1,
  name : 'name',
  description : 'description',
  date : new Date("10/10/2023"),
  teacher_id : 1,
  users : [2, 3],
  createdAt : new Date(),
  updatedAt : new Date(),
}

const teacher = {
  id: 1,
  lastName: "lastname",
  firstName: "firstname",
  createdAt: new Date(),
  updatedAt: new Date(),
}

// !!! admin false : particpate
// !!! admin true : delete
// session doesn't exist

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let service: SessionService;

  describe('As a non admin user', () => {

    const userId = 1

    const mockSessionService = {
      sessionInformation: {
        admin: false,
        id: userId
      }
    }

    const mockSessionAPIService = {
      detail : jest.fn(() => of(session)),
      participate : jest.fn(() => {
        // adds the current user to the session participants
        if(!session.users.includes(userId)) session.users.push(userId)
        return of(session)
      }),
      unParticipate : jest.fn(() => {
        if(session.users.includes(userId)) session.users.pop()
        return of(session)
      }), 
      delete : jest.fn(() => of(void 0)),
    }

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

    // Unit Test
    describe('as a non participant to the session', () => {
      it('should display a participate button which should be calling the participate method of the sessionAPIservice', () => {
        mockSessionAPIService.unParticipate()
        const buttons = fixture.debugElement.queryAll(By.css('button'))
        expect(buttons.length).toBe(2)
        const cardButtons = fixture.debugElement.queryAll(By.css('mat-card-title button'))
        const participateButton = cardButtons[1]
        participateButton.triggerEventHandler('click', null)
        expect(mockSessionAPIService.participate).toHaveBeenCalled()
        expect(session.users.includes(userId)).toBeTruthy()    
      })
    })

    // Unit Test
    describe('as a participant to the session', () => {
      it('should display a do not participate button  which should be calling the unparticpate method of the sessionAPIservice', () => {
        mockSessionAPIService.participate()
        const buttons = fixture.debugElement.queryAll(By.css('button'))
        expect(buttons.length).toBe(2)
        const cardButtons = fixture.debugElement.queryAll(By.css('mat-card-title button'))
        const unparticipateButton = cardButtons[1]
        unparticipateButton.triggerEventHandler('click', null)
        expect(mockSessionAPIService.unParticipate).toHaveBeenCalled()
        expect(session.users.includes(userId)).toBeFalsy()    
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
      detail : jest.fn(() => of(session)),
      participate : jest.fn(() => {
        // adds the current user to the session participants
        if(!session.users.includes(userId)) session.users.push(userId)
        return of(session)
      }),
      unParticipate : jest.fn(() => {
        if(session.users.includes(userId)) session.users.pop()
        return of(session)
      }), 
      delete : jest.fn(() => of(void 0)),
    }

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

    // Unit Test
    it('should display a delete button which should be calling the delete method of the sessionAPIservice', () => {
      const router = TestBed.inject(Router)
      router.navigate = jest.fn()
      const deleteButton = fixture.debugElement.query(By.css('button[color="warn"]'))
      deleteButton.triggerEventHandler('click', null)
      expect(mockSessionAPIService.delete).toHaveBeenCalledWith(session.id)
      expect(router.navigate).toHaveBeenCalledWith(['sessions']) 
    })

    
  })

});

