describe('Sesión ya autenticada', () => {
  it('redirige automáticamente a /inicio al abrir /login', () => {
    cy.visit('/login', {
      onBeforeLoad(win) {
        win.localStorage.setItem('empleados_basic_auth_header', 'Basic ZmFrZTp0b2tlbg==');
        win.localStorage.setItem('empleados_auth_role', 'ADMIN');
      },
    });

    cy.url({ timeout: 10000 }).should('include', '/inicio');
  });
});
