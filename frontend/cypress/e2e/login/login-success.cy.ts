describe('Login exitoso', () => {
  it('redirige a /inicio con credenciales válidas', function () {
    const email = Cypress.env('loginEmail') as string | undefined;
    const password = Cypress.env('loginPassword') as string | undefined;

    if (!email || !password) {
      cy.log(
        'Configura loginEmail y loginPassword para ejecutar este escenario contra backend real.',
      );
      return;
    }

    cy.loginViaUi(email, password);
    cy.url({ timeout: 15000 }).should('include', '/inicio');
    cy.contains('Acceso autenticado correctamente.').should('be.visible');
  });
});
