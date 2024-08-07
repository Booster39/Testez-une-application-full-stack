describe('Account spec', () => {
    it('Account successfull', () => {
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
            {
                id: 1,
                email: "yoga@studio.com",
                lastName: "lastName",
                firstName: "firstName",
                admin: true,
                createdAt: "2024-06-09T19:17:13",
                updatedAt: "2024-06-09T19:17:13"
            }).as('user')
        cy.contains('Account').click()
        cy.url().should('include', '/me')
        cy.contains('arrow_back').click()
        cy.url().should('include', '/sessions')
    })

})
