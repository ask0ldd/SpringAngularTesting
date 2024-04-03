// @ts-nocheck
describe('Register spec', () => {

  // Failing registration
  describe('when the registration is successful', () => {
    it('the user should be able to log in', () => {
      cy.visit('/register')

      const fn = "firstName"
      const ln = "lastName"
      const email = "yoga@studio.com"
      const password = "test!1234"

      cy.intercept('POST', '/api/auth/register', {
        statusCode: 200,
        body: {"message":"User registered successfully!"},
      }).as('registerUserRequest')
  
      cy.get('input[formControlName=firstName]').type(fn)
      cy.get('input[formControlName=lastName]').type(ln)
      cy.get('input[formControlName=email]').type(email)
      cy.get('input[formControlName=password]').type(password)

      cy.contains('span', 'Submit').click()

      cy.wait('@registerUserRequest')
      cy.get('@registerUserRequest.all').should('have.length', 1)
  
      cy.url().should('include', '/login')

      cy.intercept('POST', '/api/auth/login', {
        statusCode : 200,
        body: {
          token: "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5b2dhQHN0dWRpby5jb20iLCJpYXQiOjE3MTIwMDI4NTEsImV4cCI6MTcxMjA4OTI1MX0.WhlvHw9kw0NPtORRoiZh5_Lm0Ic3r7CvuBJv0w4rvW2ZrRO14OcMiO4MBt-0aQ83-bD0xmuwLT9V0mqvfXRcRw",
          type: "Bearer",
          id: 1,
          username: email,
          firstName: fn,
          lastName: ln,
          admin: false
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
      // password is short enough to trigger a bad request
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

      cy.wait('@badRequest')
      cy.get('@badRequest.all').should('have.length', 1)
  
      cy.url().should('include', '/register')
  
      cy.contains('span', 'An error occurred').should('exist')
    })
  })
});