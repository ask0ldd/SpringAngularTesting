// @ts-nocheck

describe('Unauthorized access spec', () => {

    describe('when navigating to a secured page without being logged', () => {
        
        it('should redirect me to the login page', () => {
    
        cy.visit("/sessions")
 
        cy.url().should('include', '/login')
        })
    })

  })