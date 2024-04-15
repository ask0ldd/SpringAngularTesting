// @ts-nocheck

import { logToA2SessionsPageAsABaseUser, logToA2SessionsPageAsAnAdmin } from "../support/utils/commands";

describe('Subscribe / Unsubscribe Yoga Session spec', () => {
    
    // ---
    // as an admin, i shouldn't be able to sub or unsub to a yoga session
    // ---

    describe('When connected as an admin', () => {
        it('Should not display any participate / unparticipate button', () => {
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

            // The session endpoint should have been requested
            cy.wait('@session')
            cy.get('@session.all').should('have.length', 1);

            cy.contains('div.description', 'yoga fire description').should('exist')
            cy.contains('span', 'December 29, 2024').should('exist')
            cy.contains('button', 'Delete').should('exist')
            cy.contains('span', 'teacher1 LASTNAME1').should('exist')
            cy.contains('span', '1 attendees').should('exist')

            cy.contains('span', 'Participate').should('not.exist')
            cy.contains('span', 'Do not participate').should('not.exist')
        })

    })

    // ---
    // as a base user, i participating to a session, i should be able to unsubscribe
    // ---

    describe('When connected as a simple user', () => {
        describe('and if subscribed to the target session', () => {
            it('Should be able to unsubscribe to the yoga session', () => {
                logToA2SessionsPageAsABaseUser()
                cy.url().should('include', '/sessions')

                cy.contains('mat-card-title', 'Rentals available').should('exist')
                cy.contains('mat-card-title', 'yoga fire').should('exist')
                cy.contains('p', 'yoga fire description').should('exist')
                cy.contains('mat-card-subtitle', 'December 29, 2024').should('exist')

                cy.get('img[src="assets/sessions.png"]').should('exist')

                // const sessionInterceptor = 
                cy.intercept('GET', '/api/session/*', { fixture: 'session.json' }).as('session')
                cy.intercept('GET', '/api/teacher/*', { fixture: 'teacher.json' }).as('teacher')

                cy.get('.item').first().contains('button', 'Detail').click()

                cy.url().should('include', 'detail/1')

                // The session endpoint should have been requested
                cy.wait('@session')
                cy.get('@session.all').should('have.length', 1);

                cy.contains('div.description', 'yoga fire description').should('exist')
                cy.contains('span', 'December 29, 2024').should('exist')
                cy.contains('span', 'teacher1 LASTNAME1').should('exist')
                cy.contains('span', '1 attendees').should('exist')

                // by default is participating
                cy.contains('span', 'Do not participate').should('exist')
                cy.contains('span', 'Participate').should('not.exist')

                cy.intercept('GET', '/api/session/*', { fixture: 'sessionNoParticipant.json' }).as('sessionNoParticipant')
                cy.intercept('DELETE', '/api/session/1/participate/*', (req) => {
                    req.reply({
                        statusCode: 200,
                        body: ''
                    });
                }).as('unparticipateRequest')
                cy.get('span').contains('Do not participate').click()

                // The participate endpoint should have been requested with the delete verb
                cy.wait('@unparticipateRequest')
                cy.get('@unparticipateRequest.all').should('have.length', 1);

                cy.wait('@sessionNoParticipant')
                cy.get('@sessionNoParticipant.all').should('have.length', 1);

                // the participate button should now be displayed
                cy.contains('span', 'Do not participate').should('not.exist')
                cy.contains('span', 'Participate').should('exist')
            })
        })

        describe('and if not subscribed to the target session', () => {
            it('Should be able to subscribe to the yoga session', () => {
                logToA2SessionsPageAsABaseUser()
                cy.url().should('include', '/sessions')

                cy.contains('mat-card-title', 'Rentals available').should('exist')
                cy.contains('mat-card-title', 'yoga fire').should('exist')
                cy.contains('p', 'yoga fire description').should('exist')
                cy.contains('mat-card-subtitle', 'December 29, 2024').should('exist')

                cy.get('img[src="assets/sessions.png"]').should('exist')

                cy.intercept('GET', '/api/session/*', { fixture: 'sessionNoParticipant.json' }).as('sessionNoParticipant')
                cy.intercept('GET', '/api/teacher/*', { fixture: 'teacher.json' }).as('teacher')

                cy.get('.item').first().contains('button', 'Detail').click()

                cy.url().should('include', 'detail/1')

                // The session endpoint should have been requested
                cy.wait('@sessionNoParticipant')
                cy.get('@sessionNoParticipant.all').should('have.length', 1);

                cy.contains('div.description', 'yoga fire description').should('exist')
                cy.contains('span', 'December 29, 2024').should('exist')
                cy.contains('span', 'teacher1 LASTNAME1').should('exist')
                cy.contains('span', '0 attendees').should('exist')

                // by default is participating
                cy.contains('span', 'Do not participate').should('not.exist')
                cy.contains('span', 'Participate').should('exist')

                cy.intercept('GET', '/api/session/*', { fixture: 'session.json' }).as('session')
                cy.intercept('POST', '/api/session/1/participate/*', (req) => {
                    req.reply({
                        statusCode: 200,
                        body: ''
                    });
                }).as('participateRequest')
                cy.get('span').contains('Participate').click()

                // The participate endpoint should have been requested with the post verb
                cy.wait('@participateRequest')
                cy.get('@participateRequest.all').should('have.length', 1);

                cy.wait('@session')
                cy.get('@session.all').should('have.length', 1);

                // the unparticipate button should now be displayed
                cy.contains('span', 'Do not participate').should('exist')
                cy.contains('span', 'Participate').should('not.exist')
            })
        })

    })

  });