// @ts-nocheck
describe('Register spec', () => {

  // Failing registration
  describe('when the registration is successful', () => {
    it('the user should be able to log in', () => {
      cy.visit('/register')

      const fn = "john"
      const ln = "doe"
      const email = "yoga@studio.com"
      const password = "test!1234"

      cy.intercept('POST', '/api/auth/register', {
        statusCode: 200,
        body: {"message":"User registered successfully!"},
      })
  
      cy.get('input[formControlName=firstName]').type(fn)
      cy.get('input[formControlName=lastName]').type(ln)
      cy.get('input[formControlName=email]').type(email)
      cy.get('input[formControlName=password]').type(password)

      cy.contains('span', 'Submit').click()
  
      cy.url().should('include', '/login')

      cy.intercept('POST', '/api/auth/login', {
        body: {
          id: 1,
          username: 'userName',
          firstName: 'firstName',
          lastName: 'lastName',
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
  })

  // Failing registration
  describe('when the registration is failing', () => {
    it('an error message should be displayed', () => {
      cy.visit('/register')

      const fn = "john"
      const ln = "doe"
      const email = "john.doe@email.com"
      const password = "wp"

      cy.intercept('POST', '/api/auth/register', {
        statusCode: 400,
        body: { },
      }).as('badRequest');
  
      cy.get('input[formControlName=firstName]').type(fn)
      cy.get('input[formControlName=lastName]').type(ln)
      cy.get('input[formControlName=email]').type(email)
      cy.get('input[formControlName=password]').type(password)
      cy.contains('button', 'Submit').click()
  
      cy.url().should('include', '/register')
  
      cy.contains('span', 'An error occurred').should('exist')
    })
  })
});