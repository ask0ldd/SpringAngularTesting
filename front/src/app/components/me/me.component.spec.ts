import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';

import { expect } from '@jest/globals';

import { MeComponent } from './me.component';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { UpperCasePipe } from '@angular/common';
import { mockBaseUser } from 'src/app/testing/mockDatas';

const mockSessionService = {
  sessionInformation: {
    admin: false,
    id: 1
  },
  logOut : jest.fn()
}

const mockSessionServiceAdmin = {...mockSessionService, sessionInformation : { admin : true, id : 1}}

const userDetails = {...mockBaseUser}

const mockUserService = {
  getById : (id : string) => of(userDetails),
  delete : jest.fn((id) => of(void 0))
}

const mockUserServiceAdmin = {
  getById : (id : string) => of({...userDetails, admin : true}),
  delete : jest.fn((id) => of(void 0))
}

const snackBarMock = {
  open : jest.fn()
}

const routerMock = {
  navigate : jest.fn((commands : string[]) => null)
}

const upperCasePipe = new UpperCasePipe();

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        { provide: MatSnackBar, useValue : snackBarMock },
        { provide: Router, useValue: routerMock },
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
    jest.clearAllMocks()
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // --------
  // Should display the users infos / Integration Test
  // --------

  it('should display all the sessions datas', () => {
    // Arrange
    const attendeesnDateContainer = fixture.debugElement.query(By.css('mat-card-content'))
    const spans = attendeesnDateContainer.queryAll(By.css('span'))
    // Assert
    expect(fixture.debugElement.query(By.css('h1')).nativeElement.textContent).toEqual("User information")
    expect(fixture.debugElement.query(By.css('mat-card-content p:first-child')).nativeElement.textContent).toContain("Name: " + userDetails.firstName + " " + upperCasePipe.transform(userDetails.lastName))
  })

  // --------
  // Back button / Integration Test
  // --------

  it('should go back in history when clicking on the back button', () => {
    // Arrange
    const windowHistorySpy = jest.spyOn(window.history, 'back')
    const backButton = fixture.debugElement.query(By.css('button[mat-icon-button]'))
    // Act
    backButton.triggerEventHandler('click', null)
    // Assert
    expect(windowHistorySpy).toHaveBeenCalled()
  })

  // --------
  // Delete button displayed as a base User / Integration Test
  // --------

  describe('if i click on the delete button', () => {
    it('should display a related message into a snackbar, logout and redirect me to the homepage when i try to delete my account', () => {
      // Arrange
      const deleteAccountButton = fixture.debugElement.query(By.css('button[color="warn"]'))
      // Act
      deleteAccountButton.triggerEventHandler('click', null)
      // Assert
      expect(mockUserService.delete).toHaveBeenCalledWith(mockSessionService.sessionInformation.id.toString())
      expect(snackBarMock.open).toHaveBeenCalledWith("Your account has been deleted !", 'Close', { duration: 3000 })
      expect(mockSessionService.logOut).toHaveBeenCalled()
      expect(routerMock.navigate).toHaveBeenCalledWith(['/'])
    })
  })

})

// --------
// Delete button not displayed as an Admin / Integration Test
// --------

describe('When logged as an admin', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionServiceAdmin },
        { provide: UserService, useValue: mockUserServiceAdmin },
        { provide: MatSnackBar, useValue : snackBarMock },
        { provide: Router, useValue: routerMock },
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
    jest.clearAllMocks()
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(fixture.debugElement.query(By.css('mat-card-content')).nativeElement.textContent).toContain("You are admin")
  });

  it('shouldnt display any delete account button', () => {
    const deleteAccountButton = fixture.debugElement.query(By.css('button[color="warn"]'))
    // Assert
    expect(deleteAccountButton).toBeNull();
  })
})