import { login, interceptTeachers } from './utils.cy'

describe('Delete session spec', () => {
  beforeEach(() => {
    login()
    interceptTeachers()
  })

  it('Delete session successful', () => {
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

    cy.intercept('GET', '/api/session/1', []).as('GetTheSession')

    cy.contains('Detail').click()

    cy.intercept('DELETE', '/api/session/1', []).as('delete')
    cy.intercept('GET', '/api/session', []).as('GetSession')

    cy.contains('Delete').click()
    cy.url().should('eq', 'http://localhost:4200/sessions')
  })
})
