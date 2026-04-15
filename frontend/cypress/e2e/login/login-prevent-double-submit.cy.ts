describe('Prevención de doble envío', () => {
  it('deshabilita botón mientras valida credenciales', () => {
    cy.visit('/login');
    cy.get('#emailCorporativo').type('sinacceso@empresa.com');
    cy.get('#contrasena').type('clave-invalida', { log: false });

    cy.contains('button', 'Iniciar sesión').click();
    cy.contains('button', 'Validando...').should('be.disabled');
  });
});
