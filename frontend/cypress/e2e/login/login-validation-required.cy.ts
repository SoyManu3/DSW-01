describe('Validaciones de formulario', () => {
  it('bloquea envío si faltan campos obligatorios', () => {
    cy.visit('/login');
    cy.contains('button', 'Iniciar sesión').click();

    cy.contains('El correo corporativo es obligatorio.').should('be.visible');
    cy.contains('La contraseña es obligatoria.').should('be.visible');
    cy.url().should('include', '/login');
  });
});
