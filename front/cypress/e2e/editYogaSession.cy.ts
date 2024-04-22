// @ts-nocheck

import { logToA2SessionsPageAsABaseUser, logToA2SessionsPageAsAnAdmin } from "../support/utils/commands";

describe('Edit Yoga Session spec', () => {

    // ---
    // as an admin, i should be able to edit a yoga session
    // ---
    describe('When connected as an admin', () => {
        it('should be possible to edit a session', () => {
            logToA2SessionsPageAsAnAdmin()

            cy.intercept('GET', '/api/session/*', { fixture: 'session.json' }).as('session')
            cy.intercept('GET', '/api/teacher', { fixture: 'teachers.json' }).as('teachers')

            cy.get('.item').first().contains('button', 'Edit').click()

            cy.url().should('include', 'update')

            // check the content of the fields
            cy.get('input[formControlName=name]').invoke('val').should((value) => {
                expect(value).to.equal('yoga fire')
            })
            cy.get('input[formControlName=date]').invoke('val').should((value) => {
                expect(value).to.equal('2024-12-29')
            })
            cy.get('textarea').invoke('val').should((value) => {
                expect(value).to.equal('yoga fire description')
            })
            cy.contains('span', 'teacher1 lastname1').should('exist')

            // filling the form
            cy.get('input[formControlName=name]').should('exist').clear().type("yoga sansara")
            cy.get('input[formControlName=date]').should('exist').clear().type("2026-02-02")
            cy.get('mat-select[formControlName=teacher_id]').should('exist').click()
            cy.get('#mat-option-1').should('exist').click()
            cy.get('textarea[formControlName=description]').should('exist').clear().type("yoga sansara description")

            // mocking the api before submitting
            cy.intercept('PUT', '/api/session/*', {
                statusCode : 200,
                body: {
                  id: 1,
                  name: "yoga sansara",
                  date : "2026-02-02 01:07:22",
                  teacher_id : 1,
                  description : "yoga sansara description",
                  users : [1],
                  createdAt : "2023-12-29 01:07:22",
                  updatedAt : "2023-12-29 01:07:22"
                }
              }).as('updateSessionRequest');

            cy.intercept('GET', '/api/session', { fixture: 'updatedSession.json' }).as('session')

            cy.get('button[type=submit]').should('exist').click()

            cy.wait('@updateSessionRequest')
            cy.get('@updateSessionRequest.all').should('have.length', 1)
            
            // snackbar informing us about the sucessful update
            cy.contains('div', 'Session updated !').should('exist')

            cy.contains('mat-card-title', 'Rentals available').should('exist')
            cy.contains('mat-card-title', 'yoga sansara').should('exist')
            cy.contains('p', 'yoga sansara description').should('exist')
            cy.contains('mat-card-subtitle', 'February 2, 2026').should('exist')
    
            cy.get('img[src="assets/sessions.png"]').should('exist')
        })
    })

  });