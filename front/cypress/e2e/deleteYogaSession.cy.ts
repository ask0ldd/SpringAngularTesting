// @ts-nocheck

import { logToA2SessionsPageAsABaseUser, logToA2SessionsPageAsAnAdmin } from "../support/utils/commands";

describe('Delete Yoga Session spec', () => {

    // ---
    // as an admin, i should be able to delete a yoga session from its details page
    // ---

    describe('When connected as an admin', () => {
        it('should allow me to delete the session', () => {

            logToA2SessionsPageAsAnAdmin()
            cy.url().should('include', '/sessions')

            cy.contains('mat-card-title', 'Rentals available').should('exist')
            cy.contains('mat-card-title', 'yoga fire').should('exist')
            cy.contains('p', 'yoga fire description').should('exist')
            cy.contains('mat-card-subtitle', 'December 29, 2024').should('exist')

            cy.get('img[src="assets/sessions.png"]').should('exist')

            cy.intercept('GET', '/api/session/*', { fixture: 'session.json' }).as('session')
            cy.intercept('GET', '/api/teacher/*', { fixture: 'teacher.json' }).as('teacher')

            cy.get('.item').first().contains('button', 'Detail').click()

            cy.url().should('include', 'detail')

            cy.contains('div.description', 'yoga fire description').should('exist')
            cy.contains('span', 'December 29, 2024').should('exist')
            cy.contains('button', 'Delete').should('exist')
            cy.contains('span', 'teacher1 LASTNAME1').should('exist')
            cy.contains('span', '1 attendees').should('exist')

            cy.intercept('DELETE', '/api/session/*', (req) => {
                req.reply({
                    statusCode: 200,
                    body: ''
                });
            }).as('deleteSessionRequest');

            cy.contains('button', 'Delete').click()

            // The delete endpoint should have been requested
            cy.wait('@deleteSessionRequest')
            cy.get('@deleteSessionRequest.all').should('have.length', 1);

            cy.url().should('include', '/sessions')

            // a snackbar should inform us about the successful deletion
            cy.contains('div', 'Session deleted !').should('exist')
        })
    })

    // ---
    // as a base user, i shouldn't be able to delete a session from its details page
    // ---

    describe('When connected as a simple user', () => {
        it('shouldnt allow me to delete the session', () => {
            logToA2SessionsPageAsABaseUser()
            cy.url().should('include', '/sessions')

            cy.contains('mat-card-title', 'Rentals available').should('exist')
            cy.contains('mat-card-title', 'yoga fire').should('exist')
            cy.contains('p', 'yoga fire description').should('exist')
            cy.contains('mat-card-subtitle', 'December 29, 2024').should('exist')

            cy.get('img[src="assets/sessions.png"]').should('exist')

            cy.intercept('GET', '/api/session/*', { fixture: 'session.json' }).as('session')
            cy.intercept('GET', '/api/teacher/*', { fixture: 'teacher.json' }).as('teacher')

            cy.get('.item').first().contains('button', 'Detail').click()

            cy.url().should('include', 'detail')

            cy.contains('div.description', 'yoga fire description').should('exist')
            cy.contains('span', 'December 29, 2024').should('exist')
            cy.contains('span', 'teacher1 LASTNAME1').should('exist')
            cy.contains('span', '1 attendees').should('exist')

            cy.contains('Delete').should('not.exist')
        })

    })

  });