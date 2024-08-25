import { login, interceptTeachers } from './utils.cy'

describe('Edit session spec', () => {
  beforeEach(() => {
    login()
    interceptTeachers()
  })

  it('Edit session successful', () => {
    cy.contains('Create').click()
    cy.url().should('eq', 'http://localhost:4200/sessions/create')

    cy.get('input[formControlName=name]').type("Session 1")
    cy.get('input[formControlName=date]').type("2023-12-12")
    cy.get('mat-select[ng-reflect-name=teacher_id]').click()
    cy.contains('Margot').click()
    cy.get('textarea[formControlName=description]').type("This is the first session.")

    cy.intercept('POST', '/api/session', {
      body: {
        id: 1,
        name: "Session 1",
        date: "2023-12-12",
        teacher_id: 1,
        description: "This is the first session.",
        users: [],
        createdAt: "2024-06-20T12:44:37.0756247",
        updatedAt: "2024-06-20T12:44:37.0934961"
      }
    }).as('PostSession')

    cy.intercept('GET', '/api/session', [{
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

    cy.intercept('GET', '/api/session/1', {
      id: 1,
      name: "Session 1",
      date: "2023-12-12T00:00:00.000+00:00",
      teacher_id: 1,
      description: "This is the yoga session.",
      users: [],
      createdAt: "2024-06-20T12:44:37",
      updatedAt: "2024-06-20T12:44:37"
    }).as('GetTheSession')

    cy.intercept('GET', '/api/teacher/1', {
      id: 1,
      lastName: "DELAHAYE",
      firstName: "Margot",
      createdAt: "2024-06-09T19:17:01",
      updatedAt: "2024-06-09T19:17:01"
    }).as('GetTheTeacher')

    cy.contains('Edit').click()
    cy.url().should('eq', 'http://localhost:4200/sessions/update/1')

    cy.updateSession({
      description: "It is not the same session as before."
    })

    cy.intercept('GET', '/api/session').as('Sessions')

    cy.get('textarea[formControlName=description]').clear()
    cy.get('textarea[formControlName=description]').type("It is not the same session as before.")
    cy.contains('Save').click()
    cy.url().should('eq', 'http://localhost:4200/sessions')
  })

  it('Displays error when required fields are missing', () => {
    cy.contains('Create').click()
    cy.url().should('eq', 'http://localhost:4200/sessions/create')
  
    cy.get('input[formControlName=name]').type("Session 1")
    cy.get('input[formControlName=date]').type("2023-12-12")
    cy.get('mat-select[ng-reflect-name=teacher_id]').click()
    cy.contains('Margot').click()
  
    cy.get('textarea[formControlName=description]').clear()
  
    cy.get('button[type=submit]').should('be.disabled')
    cy.get('textarea[formControlName=description]').should('have.class', 'ng-invalid')
    cy.url().should('eq', 'http://localhost:4200/sessions/create')
  })
})
