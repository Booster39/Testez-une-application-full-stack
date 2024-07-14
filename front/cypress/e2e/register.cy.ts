describe('Register spec', () => {
    it('Register successfull', () => {
      cy.visit('/register')
  
      cy.intercept('POST', '/api/auth/register', {
        body: {
          id: 1,
          firstName: 'firstName',
          lastName: 'lastName',
          email: 'email',
          password: 'password'
        },
      }).as('register')
      cy.get('input[formControlName=firstName]').type("toto")
      cy.get('input[formControlName=lastName]').type("toto")
      cy.get('input[formControlName=email]').type("toto3@toto.com")
      cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
  
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
  
      cy.get('input[formControlName=email]').type("toto3@toto.com")
      cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
  
      cy.url().should('include', '/sessions')
    })

    it('Displays error when required fields are missing', () => {
      cy.visit('/register')
  
      // Attempt to submit the form without filling in any fields
      cy.get('button[type=submit]').should('be.disabled')
  
      // Check that the email and password input fields have the 'ng-invalid' class
      cy.get('input[formControlName=firstName]').should('have.class', 'ng-invalid')
      cy.get('input[formControlName=lastName]').should('have.class', 'ng-invalid')
      cy.get('input[formControlName=email]').should('have.class', 'ng-invalid')
      cy.get('input[formControlName=password]').should('have.class', 'ng-invalid')
  
      cy.url().should('include', '/register')
    })

    
  });