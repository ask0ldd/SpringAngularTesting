import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInput, MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { of } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { By } from '@angular/platform-browser';

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

const authServiceMock = {
  login: jest.fn(() => of(sessionInformation)),
}

const sessionServiceMock = {
  logIn : jest.fn((response : SessionInformation) => null)
}

const routerMock = {
  navigate : jest.fn((commands : string[]) => null)
}
// end init Mocks

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: AuthService, useValue: authServiceMock }, // added to mock authservice
        { provide: Router, useValue: routerMock },
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  })

  it('should create', () => {
    expect(component).toBeTruthy();
  })

  it('should render the login title, the fields and the an inactive submit button', () => {
    expect(fixture.debugElement.query(By.css('mat-card-title')).nativeElement.textContent).toBe("Login")
    /*
      When you use fixture.debugElement.queryAll(By.css('form input')) in your Jest test, you're selecting the raw HTML input elements, 
      not the Angular Material components. This means that the placeholder attribute, which is set on the Material component, 
      is not being reflected in the selected input elements.
    */
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('input[data-placeholder="Email"]')).toBeTruthy()
    expect(compiled.querySelector('input[data-placeholder="Password"]')).toBeTruthy()
    const submitButton = compiled.querySelector('button[type="submit"]')
    expect(submitButton).toBeTruthy()
    expect((submitButton as HTMLButtonElement).disabled).toBeTruthy()
  })

  describe('if the form contains a valid password but with an invalid email',() => {
    it('should still display an inactive submit button', () => {
      const compiled = fixture.nativeElement as HTMLElement;
      const inputs = fixture.debugElement.queryAll(By.css('form input'))
      inputs[0].triggerEventHandler('input', { target: { value: 'invalidEmail'}})
      inputs[1].triggerEventHandler('input', { target: { value: 'validPassword'}})
      const submitButton = compiled.querySelector('button[type="submit"]')
      expect((submitButton as HTMLButtonElement).disabled).toBeTruthy()
    })
  })

});
