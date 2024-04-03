// @ts-nocheck
import { logToABlankSessionsPageAsAnAdmin } from "../support/utils/commands";

describe('Login spec', () => {

  // Successful Login
  it('Login successful', () => {
    logToABlankSessionsPageAsAnAdmin()

    cy.url().should('include', '/sessions')
  })

  // Failing Login
  it('Login failing', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', (req) => {
      req.reply({
        statusCode: 401,
        body: {
          "path": "/api/auth/login",
          "error": "Unauthorized",
          "message": "Bad credentials",
          "status": 401
        }
      });
    }).as('unauthorized');

    cy.get('input[formControlName=email]').type("aaaaaaa@aaaaaaaaaaa")
    cy.get('input[formControlName=password]').type(`${"test!1234"}`)

    cy.contains('button', 'Submit').click()

    cy.wait('@unauthorized')
    cy.get('@unauthorized.all').should('have.length', 1)

    cy.url().should('include', '/login')

    cy.contains('p', 'An error occurred').should('exist')
  })
});