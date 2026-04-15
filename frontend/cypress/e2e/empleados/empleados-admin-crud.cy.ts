describe('Empleados - Admin CRUD', () => {
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
    cy.contains('a', 'Ir a gestión de empleados').click();
    cy.url().should('include', '/empleados');
    cy.contains('button', 'Nuevo empleado').should('be.visible');
  });
});
