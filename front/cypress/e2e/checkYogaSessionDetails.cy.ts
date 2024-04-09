// @ts-nocheck

import { logToA2SessionsPageAsABaseUser, logToA2SessionsPageAsAnAdmin } from "../support/utils/commands";

describe('Yoga Session Details spec', () => {
    
    // ---
    // as an admin, i should be able to check the details of a yoga session
    // ---
    describe('When connected as an admin', () => {
        it('Should display target yoga session details', () => {
            logToA2SessionsPageAsAnAdmin()
            cy.url().should('include', '/sessions')

            cy.contains('mat-card-title', 'Rentals available').should('exist')
            cy.contains('mat-card-title', 'yoga fire').should('exist')
            cy.contains('p', 'yoga fire description').should('exist')
            cy.contains('mat-card-subtitle', 'December 29, 2024').should('exist')

            cy.get('img[src="assets/sessions.png"]').should('exist')

            cy.intercept('GET', '/api/session/*', { fixture: 'session.json' }).as('session')
            cy.intercept('GET', '/api/teacher/*', { fixture: 'teacher.json' }).as('teacher')

            // cy.get('button[ng-reflect-router-link="detail,1"]').should('exist').click()

            cy.get('.item').first().contains('button', 'Detail').click()

            cy.url().should('include', 'detail')

            // The session endpoint should have been requested
            cy.wait('@session')
            cy.get('@session.all').should('have.length', 1);

            cy.contains('div.description', 'yoga fire description').should('exist')
            cy.contains('span', 'December 29, 2024').should('exist')
            cy.contains('button', 'Delete').should('exist')
            cy.contains('span', 'teacher1 LASTNAME1').should('exist')
            cy.contains('span', '1 attendees').should('exist')
        })

        it('and the back button should lead you back to the sessions page', () => {
            cy.intercept('GET', '/api/session', { fixture: 'sessions.json' }).as('sessions')
            cy.contains('button', 'arrow_back').should('exist').click()
    
            cy.url().should('include', '/sessions')
    
            cy.contains('mat-card-title', 'Rentals available').should('exist')
            cy.contains('mat-card-title', 'yoga fire').should('exist')
            cy.contains('p', 'yoga fire description').should('exist')
            cy.contains('mat-card-subtitle', 'December 29, 2024').should('exist')
        })
    })

    // ---
    // as a base user, i should be able to check the details of a yoga session
    // ---
    describe('When connected as a simple user', () => {
        it('Should display target yoga session details', () => {
            logToA2SessionsPageAsABaseUser()
            cy.url().should('include', '/sessions')

            cy.contains('mat-card-title', 'Rentals available').should('exist')
            cy.contains('mat-card-title', 'yoga fire').should('exist')
            cy.contains('p', 'yoga fire description').should('exist')
            cy.contains('mat-card-subtitle', 'December 29, 2024').should('exist')

            cy.get('img[src="assets/sessions.png"]').should('exist')

            cy.intercept('GET', '/api/session/*', { fixture: 'session.json' }).as('session')
            cy.intercept('GET', '/api/teacher/*', { fixture: 'teacher.json' }).as('teacher')

            // cy.get('button[ng-reflect-router-link="detail,1"]').should('exist').click()

            cy.get('.item').first().contains('button', 'Detail').click()

            cy.url().should('include', 'detail')

            // The session endpoint should have been requested
            cy.wait('@session')
            cy.get('@session.all').should('have.length', 1);

            cy.contains('div.description', 'yoga fire description').should('exist')
            cy.contains('span', 'December 29, 2024').should('exist')
            cy.contains('span', 'teacher1 LASTNAME1').should('exist')
            cy.contains('span', '1 attendees').should('exist')
        })

        it('and the back button should lead you back to the sessions page', () => {
            cy.intercept('GET', '/api/session', { fixture: 'sessions.json' }).as('sessions')
            cy.contains('button', 'arrow_back').should('exist').click()
    
            cy.url().should('include', '/sessions')
    
            cy.contains('mat-card-title', 'Rentals available').should('exist')
            cy.contains('mat-card-title', 'yoga fire').should('exist')
            cy.contains('p', 'yoga fire description').should('exist')
            cy.contains('mat-card-subtitle', 'December 29, 2024').should('exist')
        })

    })

  });