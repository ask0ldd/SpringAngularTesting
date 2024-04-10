// @ts-nocheck
describe('User Profile spec', () => {
    it('Check User Profile', () => {
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

        cy.intercept('GET', '/api/user/*', { fixture: 'user.json' }).as('user')

        cy.contains('span', 'Account').click()

        cy.wait('@user').then((interception) => {
            cy.contains('p', 'Name: admin ADMIN').should('exist')
            cy.contains('p', 'Email: john.doe@email.com').should('exist')
            cy.contains('p', 'You are admin').should('exist')
            cy.contains('p', 'December 29, 2023').should('exist')
        })
    })

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