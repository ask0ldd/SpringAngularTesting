// @ts-nocheck
describe('Login spec', () => {

  // Successful Login
  it('Login successful', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      statusCode : 200,
      body: {
          token: "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5b2dhQHN0dWRpby5jb20iLCJpYXQiOjE3MTIwMDI4NTEsImV4cCI6MTcxMjA4OTI1MX0.WhlvHw9kw0NPtORRoiZh5_Lm0Ic3r7CvuBJv0w4rvW2ZrRO14OcMiO4MBt-0aQ83-bD0xmuwLT9V0mqvfXRcRw",
          type: "Bearer",
          id: 1,
          username: "yoga@studio.com",
          firstName: "Admin",
          lastName: "Admin",
          admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  })

  // Failing Login
  it('Login failing', () => {
    cy.visit('/login')

    cy.intercept('GET', '/api/auth/login', (req) => {
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

    cy.url().should('include', '/login')

    // cy.wait('@unauthorized').then((interception) => {
      cy.contains('p', 'An error occurred').should('exist')
    // })
  })
});