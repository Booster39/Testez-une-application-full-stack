import { login, interceptTeachers } from './utils.cy'

describe('Create session spec', () => {
  beforeEach(() => {
    login()
    interceptTeachers()
  })

  it('Create session successful', () => {
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

    cy.intercept('GET', '/api/session', [
      {
        id: 1,
        name: "Session 1",
        date: "2023-12-12",
        teacher_id: 1,
        description: "This is the first session.",
        users: [],
        createdAt: "2024-06-20T12:44:37.0756247",
        updatedAt: "2024-06-20T12:44:37.0934961"
      }
    ]).as('GetSession')

    cy.contains('Save').click()
    cy.url().should('eq', 'http://localhost:4200/sessions')
  })

  it('Displays error when required fields are missing', () => {
    cy.contains('Create').click()
    cy.url().should('eq', 'http://localhost:4200/sessions/create')

    cy.get('input[formControlName=name]').should('have.class', 'ng-invalid')
    cy.get('input[formControlName=date]').should('have.class', 'ng-invalid')
    cy.get('mat-select[ng-reflect-name=teacher_id]').click()
    cy.contains('Margot').click()
    cy.get('textarea[formControlName=description]').should('have.class', 'ng-invalid')

    cy.get('button[type=submit]').should('be.disabled')
    cy.url().should('eq', 'http://localhost:4200/sessions/create')
  })
})
