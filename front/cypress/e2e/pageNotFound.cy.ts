// @ts-nocheck
import { logToABlankSessionsPageAsAnAdmin, logToA2SessionsPageAsABaseUser } from "../support/utils/commands";

describe('Page not found spec', () => {

  describe('when visiting an unknown url as an admin', () => {
    it('should redirect me to the 404 page', () => {
      logToABlankSessionsPageAsAnAdmin()

      cy.url().should('include', '/sessions')

      cy.visit("http://localhost:4200/api/fake")

      cy.contains('h1', 'Page not found !').should('exist')
    })
  })

  describe('when visiting an unknown url as base user', () => {
    it('should redirect me to the 404 page', () => {
      logToA2SessionsPageAsABaseUser()

      cy.url().should('include', '/sessions')

      cy.visit("http://localhost:4200/api/fake")

      cy.contains('h1', 'Page not found !').should('exist')
    })
  })
})