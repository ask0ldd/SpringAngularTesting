// @ts-nocheck

import { logToA2SessionsPageAsABaseUser, logToABlankSessionsPageAsAnAdmin } from "../support/utils/commands";

describe('User Profile spec', () => {

    // ---
    // as a base user, i should be able to access my profile
    // ---

    it('Check User Profile', () => {

        logToABlankSessionsPageAsAnAdmin()

        cy.intercept('GET', '/api/user/*', { fixture: 'user.json' }).as('user')

        cy.contains('span', 'Account').click()

        cy.wait('@user').then((interception) => {
            cy.contains('p', 'Name: admin ADMIN').should('exist')
            cy.contains('p', 'Email: john.doe@email.com').should('exist')
            cy.contains('p', 'You are admin').should('exist')
            cy.contains('p', 'December 29, 2023').should('exist')
        })
    })

    // ---
    // clicking on the back button should lead me to the session page
    // ---

    it('and the back button should lead you back to the sessions page', () => {
        cy.intercept('GET', '/api/session', { fixture: 'sessions.json' }).as('sessions')
        cy.contains('button', 'arrow_back').should('exist').click()

        cy.url().should('include', '/sessions')

        cy.contains('mat-card-title', 'Rentals available').should('exist')
        cy.contains('mat-card-title', 'yoga fire').should('exist')
        cy.contains('p', 'yoga fire description').should('exist')
        cy.contains('mat-card-subtitle', 'December 29, 2024').should('exist')
    })
  });