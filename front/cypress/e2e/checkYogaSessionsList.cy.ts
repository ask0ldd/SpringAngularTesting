
// @ts-nocheck
import { logToA2SessionsPageAsABaseUser, logToA2SessionsPageAsAnAdmin } from "../support/utils/commands";

describe('Yoga Session Details spec', () => {
    
    // ---
    // as an admin, i should be able to check the list of yoga sessions
    // ---
    
    describe('When connected as an admin', () => {
        it('Should display a list of two yoga sessions', () => {
            logToA2SessionsPageAsAnAdmin()
            cy.url().should('include', '/sessions')

            cy.get('.item').should('have.length', 2)

            cy.contains('mat-card-title', 'Rentals available').should('exist')
            cy.contains('mat-card-title', 'yoga fire').should('exist')
            cy.contains('p', 'yoga fire description').should('exist')
            cy.contains('mat-card-subtitle', 'December 29, 2024').should('exist')

            cy.contains('mat-card-title', 'Rentals available').should('exist')
            cy.contains('mat-card-title', 'yoga flame').should('exist')
            cy.contains('p', 'yoga flame description').should('exist')
            cy.contains('mat-card-subtitle', 'December 29, 2024').should('exist')

            cy.get('img[src="assets/sessions.png"]').should('exist')
        })
    })

    // ---
    // as a base user, i should be able to check the list of yoga sessions
    // ---

    describe('When connected as a base user', () => {
        it('Should display target yoga session details', () => {
            logToA2SessionsPageAsABaseUser()
            cy.url().should('include', '/sessions')

            cy.get('.item').should('have.length', 2)

            cy.contains('mat-card-title', 'Rentals available').should('exist')
            cy.contains('mat-card-title', 'yoga fire').should('exist')
            cy.contains('p', 'yoga fire description').should('exist')
            cy.contains('mat-card-subtitle', 'December 29, 2024').should('exist')

            cy.contains('mat-card-title', 'Rentals available').should('exist')
            cy.contains('mat-card-title', 'yoga flame').should('exist')
            cy.contains('p', 'yoga flame description').should('exist')
            cy.contains('mat-card-subtitle', 'December 29, 2024').should('exist')

            cy.get('img[src="assets/sessions.png"]').should('exist')
        })
    })
})