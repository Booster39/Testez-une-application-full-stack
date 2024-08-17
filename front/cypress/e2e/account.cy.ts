import { login } from './utils.cy'

describe('Account spec', () => {
  beforeEach(() => {
    login()
  })

  it('Account successful', () => {
    cy.intercept('GET', '/api/user/1', {
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
  })
})
