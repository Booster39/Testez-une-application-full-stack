import { login } from './utils.cy'

describe('Delete Account spec', () => {
  beforeEach(() => {
    login()
  })

  it('Delete Account successful', () => {
    cy.intercept('GET', '/api/user/1', []).as('user')
    cy.contains('Account').click()

    cy.intercept('DELETE', '/api/user/1', []).as('delete')
    
    cy.contains('Detail').click()
    cy.url().should('eq', 'http://localhost:4200/')
  })
})
