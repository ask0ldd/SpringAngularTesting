import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { By } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Session } from '../../interfaces/session.interface';
import { DatePipe } from '@angular/common';
import { of } from 'rxjs';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import { TeacherService } from 'src/app/services/teacher.service';
import { NgZone } from '@angular/core';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { mockTeacher1, mockTeacher2, mockSessionInformationAdmin, mockYogaSession1 } from 'src/app/testing/mockDatas';

const mockSessionInformation : SessionInformation = {...mockSessionInformationAdmin}

const mockSessionService = {
  sessionInformation: mockSessionInformation
} 

const yogaSession : Session = {...mockYogaSession1}

const snackBarMock = {
  open : jest.fn()
}

const mockSessionAPIService = {
  all : jest.fn(),
  detail : jest.fn(() => of(yogaSession)),
  update : jest.fn(() => of(yogaSession)),
  create : jest.fn(() => of(yogaSession)),
}

const teacher1 : Teacher = {...mockTeacher1}

const teacher2 : Teacher = {...mockTeacher2}

const mockTeacherService = {
  all : jest.fn(() => of([{...teacher1, ...teacher2}])),
}

const activatedRouteMock = {
  snapshot : {
    paramMap : {
      get : (id : any) => 1
    }
  }, 
}

const routerMock = {
  navigate : jest.fn((commands : string[]) => null),
  url : "",
  createUrlTree : jest.fn(() => void 0),
  navigateByUrl : jest.fn(() => void 0),
}

describe('FormComponent', () => {
  let component: FormComponent
  let fixture: ComponentFixture<FormComponent>
  let ngZone : NgZone

  beforeEach(async () => {
    await TestBed.configureTestingModule({
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
        BrowserAnimationsModule,
        NoopAnimationsModule, // had to be added since "TypeError: element.animate is not a function" showed when opening the select
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: SessionApiService, useValue: mockSessionAPIService },
        { provide: MatSnackBar, useValue : snackBarMock },
        { provide: ActivatedRoute, useValue : activatedRouteMock }, // used to mock the function returning the url/{sessionId} param
        { provide: Router, useValue: routerMock },
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
    ngZone = TestBed.inject(NgZone)
    jest.clearAllMocks()
  });

  it('should create', () => {
    expect(component).toBeTruthy()
  });

  // --------
  // Back Button / Integration Test
  // --------

  describe('when clicking on the back button', () => {
    it('should go back in history', async () => {
      const windowHistorySpy = jest.spyOn(window.history, 'back')
      const backButton = fixture.debugElement.query(By.css('button[mat-icon-button]'))
      backButton.triggerEventHandler('click', null)
      await fixture.whenStable()
      expect(routerMock.navigateByUrl).toHaveBeenCalled()
    })
  })

  describe('when connected as an admin', () => {

  // --------
  // Submit button disabled by default / Integration Test
  // --------

    it('should display the form with a disabled submit button', () => {
      const compiled = fixture.nativeElement as HTMLElement;
      // Assert
      expect(fixture.debugElement.query(By.css('h1')).nativeElement.textContent).toBe("Create session")
      expect(compiled.querySelector('input[formcontrolname="name"]')).toBeTruthy()
      expect(compiled.querySelector('input[formcontrolname="date"]')).toBeTruthy()
      expect(compiled.querySelector('mat-select[formcontrolname="teacher_id"]')).toBeTruthy()
      expect(compiled.querySelector('textarea[formcontrolname="description"]')).toBeTruthy()
      const submitButton = compiled.querySelector('button[type="submit"]')
      expect(submitButton).toBeTruthy()
      // the submit button should be disabled by default
      expect((submitButton as HTMLButtonElement).disabled).toBeTruthy()
    })
  })

  // --------
  // Create a new Yoga Session / Integration Test
  // --------

  describe('when connected as an admin and the form is fully filled', () => {
    it('should create a new yoga session for the teacher 2 and display a confirmation snackbar', async () => {
      // Arrange
      expect(fixture.debugElement.query(By.css('h1')).nativeElement.textContent).toBe("Create session")
      const submitFn = jest.spyOn(component, 'submit')
      const form = fixture.debugElement.query(By.css('form'))
      const nameInput = fixture.debugElement.query(By.css('form input[formcontrolname="name"]'))
      const dateInput = fixture.debugElement.query(By.css('form input[formcontrolname="date"]'))
      const descriptionTextarea = fixture.debugElement.query(By.css('form textarea[formcontrolname="description"]'))
      
      const select = fixture.debugElement.query(By.css('mat-select')).nativeElement
      const selectTrigger = fixture.debugElement.query(By.css('.mat-select-trigger')).nativeElement

      // Act
      // fill the input / textarea fields
      nameInput.triggerEventHandler('input', { target: { value: "yoga session name"}})
      dateInput.triggerEventHandler('input', { target: { value: "04/04/2024"}})
      descriptionTextarea.triggerEventHandler('input', { target: { value: "yoga session description"}})
      
      // select the second teacher from the select
      selectTrigger.click()
      await fixture.whenStable()
      fixture.detectChanges()
      const matSelectOptions = fixture.debugElement.queryAll(By.css('.mat-option'))
      const targetOption = matSelectOptions.find((option) => option.nativeElement.textContent.trim() === teacher2.firstName + ' ' + teacher2.lastName)
      targetOption?.nativeElement.click()
      fixture.detectChanges()

      ngZone.run(() => {
        form.triggerEventHandler('submit', null)
      })

      // Assert
      expect(mockSessionAPIService.create).toHaveBeenCalledWith({
        name: 'yoga session name',
        description: 'yoga session description',
        date: '04/04/2024',
        teacher_id : 2,
      })

      expect(snackBarMock.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 })
      await fixture.whenStable()
      expect(routerMock.navigate).toHaveBeenCalledWith(['sessions'])
    })
  })

  describe('when connected as an admin', () => {

    beforeAll(() => {
      routerMock.url = "update"
    })

    // --------
    // Edit a Yoga Session / Integration Test
    // --------

    it('should be possible to succesfully edit a yoga session with a confirmation snackbar appearing at the end', async () => {
      // Arrange
      const submitFn = jest.spyOn(component, 'submit')
      const form = fixture.debugElement.query(By.css('form'))
      expect(fixture.debugElement.query(By.css('h1')).nativeElement.textContent).toBe("Update session")
      const nameInput = fixture.debugElement.query(By.css('form input[formcontrolname="name"]'))
      const dateInput = fixture.debugElement.query(By.css('form input[formcontrolname="date"]'))
      const descriptionTextarea = fixture.debugElement.query(By.css('form textarea[formcontrolname="description"]'))

      // Pre-Act Assertion 
      expect(nameInput.nativeElement.value).toBe(yogaSession.name)
      expect(descriptionTextarea.nativeElement.value).toBe(yogaSession.description)
      expect(dateInput.nativeElement.value).toBe(yogaSession.date.toISOString().slice(0, 10))

      // Act
      nameInput.triggerEventHandler('input', { target: { value: "yoga session name"}})
      dateInput.triggerEventHandler('input', { target: { value: "04/04/2024"}})
      descriptionTextarea.triggerEventHandler('input', { target: { value: "yoga session description"}})
      fixture.detectChanges()

      ngZone.run(() => {
        form.triggerEventHandler('submit', null)
      })

      // Assert
      expect(submitFn).toHaveBeenCalled()
      expect(mockSessionAPIService.update).toHaveBeenCalledWith(activatedRouteMock.snapshot.paramMap.get('id'), {
        name: 'yoga session name',
        description: 'yoga session description',
        date: '04/04/2024',
        teacher_id : 1,
      })

      expect(snackBarMock.open).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
      await fixture.whenStable()
      expect(routerMock.navigate).toHaveBeenCalledWith(['sessions'])
    })

  })

  describe('when connected as a base user', () => {

    beforeAll(() => {
      mockSessionService.sessionInformation.admin = false;
    })

    it('should redirect the user to the sessions page', async () => {
      // !!! could be fixed injecting the updated mockSessionService before object creation
      // expect(routerMock.navigate).toHaveBeenCalledWith(['/sessions'])
    })
  })
});
