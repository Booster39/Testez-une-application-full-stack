describe('Delete Account spec', () => {
    it('Delete Account successfull', () => {
        cy.visit('/login')

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



        cy.intercept(
            {
              method: 'GET',
              url: '/api/user/1',
            },
            []).as('user')
        cy.contains('Account').click()
        cy.intercept(
            {
                method: 'DELETE',
                url: '/api/user/1',
            },[]).as('delete')
        
        cy.contains('Detail').click();
        cy.url().should('eq', 'http://localhost:4200/')
    })

})