describe('Accesibilidad por teclado', () => {
  it('mantiene foco visible y navegación por teclado en controles principales', () => {
    cy.visit('/login');

    cy.get('#emailCorporativo').focus();
    cy.focused().should('have.id', 'emailCorporativo');
    cy.focused().should('have.css', 'outline-style').and('not.eq', 'none');

    cy.get('#contrasena').focus();
    cy.focused().should('have.id', 'contrasena');
    cy.focused().should('have.css', 'outline-style').and('not.eq', 'none');

    cy.contains('button', 'Iniciar sesión').focus();
    cy.focused().contains('Iniciar sesión');
    cy.focused().should('have.css', 'outline-style').and('not.eq', 'none');
  });
});
