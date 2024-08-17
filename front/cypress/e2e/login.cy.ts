describe('Login spec', () => {
  it('Login successfull', () => {
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

    cy.url().should('include', '/sessions')
  })

  it('Handles incorrect login credentials', () => {
    cy.visit('/login')


    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: {
        path: "/api/auth/login",
        error: "Unauthorized",
        message: "Bad credentials",
        status: 401
    },
    })

    cy.get('input[formControlName=email]').type("wrong@user.com")
    cy.get('input[formControlName=password]').type(`${"wrongpassword"}{enter}{enter}`)

    cy.get('.error').should('contain', 'An error occurred')
    cy.url().should('include', '/login')
  })


  it('Displays error when required fields are missing', () => {
    cy.visit('/login')

    cy.get('button[type=submit]').should('be.disabled')

    cy.get('input[formControlName=email]').should('have.class', 'ng-invalid')
    cy.get('input[formControlName=password]').should('have.class', 'ng-invalid')

    cy.url().should('include', '/login')
  })

});