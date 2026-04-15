describe('Departamentos - Admin CRUD', () => {
  it('permite navegar al módulo y visualizar acciones de escritura', () => {
    const email =
      (Cypress.env('adminEmail') as string | undefined) ??
      (Cypress.env('loginEmail') as string | undefined);
    const password =
      (Cypress.env('adminPassword') as string | undefined) ??
      (Cypress.env('loginPassword') as string | undefined);

    if (!email || !password) {
      cy.log(
        'Configura adminEmail/adminPassword o loginEmail/loginPassword para ejecutar este escenario.',
      );
      return;
    }

    cy.loginViaUi(email, password);
    cy.contains('a', 'Ir a gestión de departamentos').click();
    cy.url().should('include', '/departamentos');
    cy.contains('button', 'Nuevo departamento').should('be.visible').and('not.be.disabled');
  });
});
