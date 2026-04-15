describe('Error técnico y reintento', () => {
  it('permite reintentar después de fallo técnico', () => {
    cy.intercept('GET', '**/api/v1/empleados?page=0', { forceNetworkError: true }).as('authFail');

    cy.visit('/login');
    cy.get('#emailCorporativo').type('user@empresa.com');
    cy.get('#contrasena').type('clave1234', { log: false });
    cy.contains('button', 'Iniciar sesión').click();

    cy.wait('@authFail');
    cy.contains('No fue posible validar tus credenciales. Intenta nuevamente.').should(
      'be.visible',
    );

    cy.intercept('GET', '**/api/v1/empleados?page=0', {
      statusCode: 401,
      body: {},
    }).as('authRetry');

    cy.contains('button', 'Iniciar sesión').click();
    cy.wait('@authRetry');
    cy.contains('Credenciales inválidas').should('be.visible');
  });
});
