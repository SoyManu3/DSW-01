describe('Login inválido', () => {
  it('muestra mensaje genérico para credenciales inválidas', () => {
    cy.visit('/login');
    cy.get('#emailCorporativo').type('sinacceso@empresa.com');
    cy.get('#contrasena').type('clave-invalida', { log: false });
    cy.contains('button', 'Iniciar sesión').click();

    cy.contains('Credenciales inválidas', { timeout: 15000 }).should('be.visible');
  });
});
