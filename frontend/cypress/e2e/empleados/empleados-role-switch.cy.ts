describe('Empleados - Cambio de sesión entre roles', () => {
  it('actualiza capacidades visibles al cambiar de sesión', () => {
    const adminEmail = Cypress.env('adminEmail') as string | undefined;
    const adminPassword = Cypress.env('adminPassword') as string | undefined;
    const empleadoEmail = Cypress.env('empleadoEmail') as string | undefined;
    const empleadoPassword = Cypress.env('empleadoPassword') as string | undefined;

    if (!adminEmail || !adminPassword || !empleadoEmail || !empleadoPassword) {
      cy.log(
        'Configura adminEmail/adminPassword y empleadoEmail/empleadoPassword para este escenario.',
      );
      return;
    }

    cy.loginViaUi(adminEmail, adminPassword);
    cy.contains('a', 'Ir a gestión de empleados').click();
    cy.contains('button', 'Nuevo empleado').should('not.be.disabled');

    cy.contains('button', 'Cerrar sesión').click();

    cy.loginViaUi(empleadoEmail, empleadoPassword);
    cy.contains('a', 'Ir a gestión de empleados').click();
    cy.contains('button', 'Nuevo empleado').should('be.disabled');
  });
});
