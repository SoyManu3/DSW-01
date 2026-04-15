describe('Departamentos - Solo lectura empleado', () => {
  it('redirecciona a listado al intentar editar por URL', () => {
    const email = Cypress.env('empleadoEmail') as string | undefined;
    const password = Cypress.env('empleadoPassword') as string | undefined;

    if (!email || !password) {
      cy.log('Configura empleadoEmail/empleadoPassword para ejecutar este escenario.');
      return;
    }

    cy.loginViaUi(email, password);
    cy.visit('/departamentos/1/editar');
    cy.url().should('include', '/departamentos');
    cy.contains('No tienes permisos para esta acción.').should('be.visible');
  });
});
