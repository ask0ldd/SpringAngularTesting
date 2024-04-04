// @ts-nocheck

// log as admin qol function
export function logToA2SessionsPageAsAnAdmin(){
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

    cy.intercept('GET', '/api/session', { fixture: 'sessions.json' }).as('sessions')
        
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
}

// log as base user qol function
export function logToA2SessionsPageAsABaseUser(){
    cy.visit('/login')
        
    cy.intercept('POST', '/api/auth/login', {
        statusCode : 200,
        body: {
            token: "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5b2dhQHN0dWRpby5jb20iLCJpYXQiOjE3MTIwMDI4NTEsImV4cCI6MTcxMjA4OTI1MX0.WhlvHw9kw0NPtORRoiZh5_Lm0Ic3r7CvuBJv0w4rvW2ZrRO14OcMiO4MBt-0aQ83-bD0xmuwLT9V0mqvfXRcRw",
            type: "Bearer",
            id: 1,
            username: "yoga@studio.com",
            firstName: "Regular",
            lastName: "User",
            admin: false
        },
    })

    cy.intercept('GET', '/api/session', { fixture: 'sessions.json' }).as('sessions')
        
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
}

export function logToABlankSessionsPageAsAnAdmin(){
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

    cy.intercept({ method: 'GET', url: '/api/session',}, []).as('session')
        
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
}