export const login = () => {
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
  
    cy.intercept('GET', '/api/session', []).as('session')
  
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
  }
  
  export const interceptTeachers = () => {
    cy.intercept('GET', '/api/teacher', [
      {
        id: 1,
        lastName: 'DELAHAYE',
        firstName: 'Margot',
        createdAt: '2024-06-09T19:17:01',
        updatedAt: '2024-06-09T19:17:01'
      },
      {
        id: 2,
        lastName: 'THIERCELIN',
        firstName: 'Hélène',
        createdAt: '2024-06-09T19:17:01',
        updatedAt: '2024-06-09T19:17:01'
      }
    ]).as('teacher')
  }
  