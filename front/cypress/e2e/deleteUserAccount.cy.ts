// @ts-nocheck
describe('Delete user account spec', () => {
    it('should display a functional delete button', () => {
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

        cy.intercept('GET', '/api/user/*', { fixture: 'baseUser.json' }).as('user')

        cy.contains('span', 'Account').click()

        cy.wait('@user').then((interception) => {
            cy.contains('p', 'Name: basic USER').should('exist')
            cy.contains('p', 'Email: john.doe@email.com').should('exist')
            cy.contains('p', 'December 29, 2023').should('exist')
        })

        // cy.intercept('GET', '/api/session', { fixture: 'sessions.json' }).as('sessions')
        cy.intercept('DELETE', '/api/user/*', 
        {   statusCode : 200,
            body : {}
        }).as('deleteUserRequest')
        cy.contains('button', 'Delete').should('exist').click()

      // The user delete endpoint should have been requested
      cy.wait('@deleteUserRequest')
      cy.get('@deleteUserRequest.all').should('have.length', 1);

      // snackbar
      cy.contains('div', 'Your account has been deleted !').should('exist')

      // should be back to the homepage
      cy.location().should((location) => {
        expect(location.pathname).to.eq('/')
      })
    })
  });