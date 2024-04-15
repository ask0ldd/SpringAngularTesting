// @ts-nocheck

import { logToA2SessionsPageAsABaseUser, logToA2SessionsPageAsAnAdmin } from "../support/utils/commands"

describe('Logout spec', () => {

    // ---
    // as an admin, i should be able to log out
    // ---

    describe('when logged as an admin', () => {
        it('should display a functional logout button redirecting to the homepage when clicked', () => {
            logToA2SessionsPageAsAnAdmin()
            cy.url().should('include', '/sessions')
            cy.contains('span', 'Logout').click()

            // should be back to the homepage
            cy.location().should((location) => {
                expect(location.pathname).to.eq('/')
            })

            cy.contains('span', 'Login').should('exist')
            cy.contains('span', 'Register').should('exist')
        })
    })

    // ---
    // as a base user, i should be able to log out
    // ---

    describe('when logged as a base user', () => {
        it('should display a functional logout button redirecting to the homepage when clicked', () => {
            logToA2SessionsPageAsABaseUser()
            cy.url().should('include', '/sessions')
            cy.contains('span', 'Logout').click()

            // should be back to the homepage
            cy.location().should((location) => {
                expect(location.pathname).to.eq('/')
            })

            cy.contains('span', 'Login').should('exist')
            cy.contains('span', 'Register').should('exist')
        })
    })

})