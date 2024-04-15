// @ts-nocheck

import { logToA2SessionsPageAsABaseUser, logToABlankSessionsPageAsAnAdmin } from "../support/utils/commands";

describe('Add Yoga Session spec', () => {

    // ---
    // as an admin, i should be able to create a new yoga session
    // ---

    describe('when logged as an admin', () => {
      it('is possible to create a new yoga session', () => {

        logToABlankSessionsPageAsAnAdmin()
        cy.url().should('include', '/sessions')
        
        // no session should be displayed at first
        cy.get('.item').should('not.exist')

        cy.intercept('GET', '/api/teacher', { fixture: 'teachers.json' }).as('teachers')

        cy.contains('button', 'Create').click()

        cy.url().should('include', '/sessions/create')

        // one session should now be displayed
        cy.get('input[formControlName=name]').should('exist').type("yoga fire")
        cy.get('input[formControlName=date]').should('exist').type("2026-06-10")
        cy.get('mat-select[formControlName=teacher_id]').should('exist').click()
        cy.get('#mat-option-1').should('exist').click()
        cy.get('textarea[formControlName=description]').should('exist').type("description")

        cy.intercept('POST', '/api/session', {
          statusCode : 200,
          body: {
            id: 1,
            name: "yoga fire",
            date : "2024-12-29 01:07:22",
            teacher_id : 1,
            description : "yoga fire description",
            users : [1],
            createdAt : "2023-12-29 01:07:22",
            updatedAt : "2023-12-29 01:07:22"
          }
        }).as('createSessionRequest');

        cy.intercept('GET', '/api/session', { fixture: 'createdSession.json' }).as('session')

        cy.get('button[type=submit]').should('exist').click()

        // The session endpoint should have been requested
        cy.wait('@createSessionRequest')
        cy.get('@createSessionRequest.all').should('have.length', 1);

        cy.get('.item').should('have.length', 1);

        cy.contains('mat-card-title', 'Rentals available').should('exist')
        cy.contains('mat-card-title', 'yoga fire').should('exist')
        cy.contains('p', 'yoga fire description').should('exist')
        cy.contains('mat-card-subtitle', 'December 29, 2024').should('exist')

        cy.get('img[src="assets/sessions.png"]').should('exist')

        // snackbar informing us about the sucessful creation
        cy.contains('div', 'Session created !').should('exist')

      })
    })

      // ---
      // as a base user, i shouldnt be able to access the new yoga session form
      // ---

      describe('When connected as a base user', () => {
        it('shouldnt be possible to access the yoga session creation form', () => {
          logToA2SessionsPageAsABaseUser()

          cy.intercept('GET', '/api/teacher', { fixture: 'teachers.json' }).as('teachers')
          cy.wait(1000)

          cy.visit("http://localhost:4200/sessions/create")

          cy.url().should('eq', 'http://localhost:4200/login');
        })
      })
  });