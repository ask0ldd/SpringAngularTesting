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

// Init mocks
const sessionInformation : SessionInformation = {
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
    return (void 0)
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
      imports: [
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
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render the register title, the fields and the an inactive submit button', () => {
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
});
