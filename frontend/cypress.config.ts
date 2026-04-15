import { defineConfig } from 'cypress';

export default defineConfig({
  e2e: {
    baseUrl: 'http://localhost:4200',
    supportFile: 'cypress/support/e2e.ts',
    specPattern: 'cypress/e2e/**/*.cy.ts',
    env: {
      apiBaseUrl: 'http://localhost:8080',
    },
    video: false,
    screenshotOnRunFailure: true,
    chromeWebSecurity: false,
  },
});
