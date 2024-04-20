import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { By } from '@angular/platform-browser';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { of, throwError } from 'rxjs';
import { LoginRequest } from '../../interfaces/loginRequest.interface';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { mockSessionInformationAdmin } from 'src/app/testing/mockDatas';

// Init mocks
const sessionInformation : SessionInformation = {...mockSessionInformationAdmin}

const validRegistration = {
  firstname : "firstname",
  lastname : "lastname",
  email : "validemail@validemail.com",
  password : "validPassword"
}

const registrationWithInvalidEmail = {...validRegistration, email : "invalidemail"}

const registrationWithShortPassword = {...validRegistration, password : "pswd"}

// if credentials are email@email.com / validpassword : broadcast sessionInformation
// if anything else : throwError
const authServiceMock = {
  login: jest.fn((loginRequest : LoginRequest) => {
    if(loginRequest.email == 'email@email.com' && loginRequest.password == 'validPassword') return of(sessionInformation)
    const err = new Error('test')
    return throwError(() => err)
  }),
  register: jest.fn((registerRequest : RegisterRequest) => {
    const err = new Error('test')
    if(registerRequest.email.length>50) return throwError(() => err)
    if(registerRequest.password.length<5) return throwError(() => err)
    return of(void 0)
  })
}

const sessionServiceMock = {
  logIn : jest.fn((response : SessionInformation) => null)
}

const routerMock = {
  navigate : jest.fn((commands : string[]) => null)
}
// end init Mocks

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock }, // added to mock authservice
        { provide: Router, useValue: routerMock },
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    jest.clearAllMocks()
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // ------
  // All the expected elements are display by default / Integration Test
  // ------

  it('should render the register title, the fields and the an inactive submit button', () => {
    // Assert
    expect(fixture.debugElement.query(By.css('mat-card-title')).nativeElement.textContent).toBe("Register")
    /*
      compiled ?
      When you use fixture.debugElement.queryAll(By.css('form input')) in your Jest test, you're selecting the raw HTML input elements, 
      not the Angular Material components. This means that the placeholder attribute, which is set on the Material component, 
      is not being reflected in the selected input elements.
    */
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('input[data-placeholder="First name"]')).toBeTruthy()
    expect(compiled.querySelector('input[data-placeholder="Last name"]')).toBeTruthy()
    expect(compiled.querySelector('input[data-placeholder="Email"]')).toBeTruthy()
    expect(compiled.querySelector('input[data-placeholder="Password"]')).toBeTruthy()
    const submitButton = compiled.querySelector('button[type="submit"]')
    expect(submitButton).toBeTruthy()
    // the submit button should be disabled by default
    expect((submitButton as HTMLButtonElement).disabled).toBeTruthy()
  })

  // ------
  // Invalid email -> Submit button is inactive / Integration Test
  // ------

  describe('when the form contains three valid fields & an invalid email',() => {
    it('should display an inactive submit button', () => {
      // Arrange
      const compiled = fixture.nativeElement as HTMLElement;
      const inputs = fixture.debugElement.queryAll(By.css('form input'))
      const submitButton = compiled.querySelector('button[type="submit"]')
      // Act
      inputs[0].triggerEventHandler('input', { target: { value: registrationWithInvalidEmail.firstname}})
      inputs[1].triggerEventHandler('input', { target: { value: registrationWithInvalidEmail.lastname}})
      inputs[2].triggerEventHandler('input', { target: { value: registrationWithInvalidEmail.email}})
      inputs[3].triggerEventHandler('input', { target: { value: registrationWithInvalidEmail.password}})
      fixture.detectChanges()
      // Assert
      // the submit button should be disabled if one field is invalid
      expect((submitButton as HTMLButtonElement).disabled).toBeTruthy()
    })
  })

  // ------
  // Valid datas within the form -> Submit button is active / Integration Test
  // ------

  describe('when the form contains only valid fields',() => {
    it('should display an active submit button', () => {
      // Arrange
      const compiled = fixture.nativeElement as HTMLElement;
      const inputs = fixture.debugElement.queryAll(By.css('form input'))
      const submitButton = compiled.querySelector('button[type="submit"]')
      // Act
      inputs[0].triggerEventHandler('input', { target: { value: validRegistration.firstname}})
      inputs[1].triggerEventHandler('input', { target: { value: validRegistration.lastname}})
      inputs[2].triggerEventHandler('input', { target: { value: validRegistration.email}})
      inputs[3].triggerEventHandler('input', { target: { value: validRegistration.password}})
      fixture.detectChanges()
      // Assert
      // the submit button should be disabled if one field is invalid
      expect((submitButton as HTMLButtonElement).disabled).toBeFalsy()
    })
  })

  // ------
  // Invalid registration datas submitted / Integration Test
  // ------

  describe('when invalid datas are submitted',() => {
    it('should display an error message', () => {
      // Arrange
      const inputs = fixture.debugElement.queryAll(By.css('form input'))
      const submitFn = jest.spyOn(component, 'submit')
      const form = fixture.debugElement.query(By.css('.register-form'))
      // Act
      inputs[0].triggerEventHandler('input', { target: { value: registrationWithShortPassword.firstname}})
      inputs[1].triggerEventHandler('input', { target: { value: registrationWithShortPassword.lastname}})
      inputs[2].triggerEventHandler('input', { target: { value: registrationWithShortPassword.email}})
      inputs[3].triggerEventHandler('input', { target: { value: registrationWithShortPassword.password}})
      fixture.detectChanges()
      form.triggerEventHandler('submit', null)
      // Assert
      expect(submitFn).toHaveBeenCalled()
      // wrong credentials : the authservice mock broadcasts an error 
      expect(authServiceMock.register).toHaveBeenCalled()
      expect(routerMock.navigate).not.toHaveBeenCalled()
      fixture.detectChanges()
      expect(fixture.debugElement.queryAll(By.css('.error'))).toBeTruthy()
    })
  })

  // ------
  // Valid registration datas submitted / Integration Test
  // ------

  describe('when valid datas are submitted',() => {
    it('should navigate to the login page', () => {
      // Arrange
      const inputs = fixture.debugElement.queryAll(By.css('form input'))
      const submitFn = jest.spyOn(component, 'submit')
      const form = fixture.debugElement.query(By.css('.register-form'))
      // Act
      inputs[0].triggerEventHandler('input', { target: { value: validRegistration.firstname}})
      inputs[1].triggerEventHandler('input', { target: { value: validRegistration.lastname}})
      inputs[2].triggerEventHandler('input', { target: { value: validRegistration.email}})
      inputs[3].triggerEventHandler('input', { target: { value: validRegistration.password}})
      fixture.detectChanges()
      form.triggerEventHandler('submit', null)
      // Assert
      expect(submitFn).toHaveBeenCalled()
      // wrong credentials : the authservice mock broadcasts an error 
      expect(authServiceMock.register).toHaveBeenCalled()
      expect(routerMock.navigate).toHaveBeenCalledWith(['/login'])
      fixture.detectChanges()
      expect(fixture.debugElement.query(By.css('.error'))).toBeNull()
    })
  })

});
