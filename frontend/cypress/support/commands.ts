declare global {
  namespace Cypress {
    interface Chainable {
      loginViaUi(email: string, password: string): Chainable<void>;
    }
  }
}

Cypress.Commands.add('loginViaUi', (email: string, password: string) => {
  cy.visit('/login');
  cy.get('#emailCorporativo').clear().type(email);
  cy.get('#contrasena').clear().type(password, { log: false });
  cy.contains('button', 'Iniciar sesión').click();
  cy.url().should('include', '/inicio');
});

export {};
