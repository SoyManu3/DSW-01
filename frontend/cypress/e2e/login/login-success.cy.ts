describe('Login exitoso', () => {
  it('redirige a /inicio con credenciales válidas', () => {
    const email = (Cypress.env('loginEmail') as string | undefined) ?? 'admin@local.test';
    const password = (Cypress.env('loginPassword') as string | undefined) ?? 'admin123';

    cy.loginViaUi(email, password);
    cy.url({ timeout: 15000 }).should('include', '/inicio');
    cy.contains('Acceso autenticado correctamente.').should('be.visible');
  });
});
