// @ts-nocheck

import { logToA2SessionsPageAsABaseUser, logToA2SessionsPageAsAnAdmin } from "../support/utils/commands"

describe('Logout spec', () => {
    describe('when logged as an admin', () => {
        it('should display a functional logout button', () => {
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

    describe('when logged as a base user', () => {
        it('should display a functional logout button', () => {
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