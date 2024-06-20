describe('Detail session spec', () => {
    it('Detail session successfull', () => {
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
              url: '/api/teacher',
            },
            [
                {
                    "id": 1,
                    "lastName": "DELAHAYE",
                    "firstName": "Margot",
                    "createdAt": "2024-06-09T19:17:01",
                    "updatedAt": "2024-06-09T19:17:01"
                },
                {
                    "id": 2,
                    "lastName": "THIERCELIN",
                    "firstName": "Hélène",
                    "createdAt": "2024-06-09T19:17:01",
                    "updatedAt": "2024-06-09T19:17:01"
                }
            ]).as('teacher')
      
        cy.contains('Create').click()
        cy.url().should('eq', 'http://localhost:4200/sessions/create')

        cy.get('input[formControlName=name]').type("Session 1")
        cy.get('input[formControlName=date]').type("2023-12-12")
        cy.get('mat-select[ng-reflect-name=teacher_id]').type("1")
        cy.contains('Margot').click()
        cy.get('textarea[formControlName=description]').type("This is the first session.")
        
        cy.intercept('POST', '/api/session', {
            body: {
                id: 1,
                name: "name",
                date: "date",
                teacher_id: "teacher_id",
                description: "description",
                users: [],
                createdAt: "2024-06-20T12:44:37.0756247",
                updatedAt: "2024-06-20T12:44:37.0934961"
            },
          }).as('PostSession')

          cy.intercept(
            {
              method: 'GET',
              url: '/api/session',
            },[{
                id: 1,
                name: "Session 1",
                date: "2023-12-12",
                teacher_id: 1,
                description: "This is the first session.",
                users: [],
                createdAt: "2024-06-20T12:44:37.0756247",
                updatedAt: "2024-06-20T12:44:37.0934961"
            }]).as('GetSession')
        cy.contains('Save').click()
        cy.url().should('eq', 'http://localhost:4200/sessions')
        
        cy.intercept(
            {
                method: 'GET',
                url: '/api/session/1'
            },
            []).as('GetTheSession')
        
            cy.contains('Detail').click()
    })

})