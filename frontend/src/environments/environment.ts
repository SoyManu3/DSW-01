const runtimeApiBaseUrl =
  typeof window !== 'undefined' ? window.__RUNTIME_CONFIG__?.API_BASE_URL?.trim() : undefined;

const normalizedApiBaseUrl = runtimeApiBaseUrl
  ? runtimeApiBaseUrl.replace(/\/+$/, '').replace(/\/api\/v1$/, '')
  : 'http://localhost:8080';

export const environment = {
  production: false,
  apiBaseUrl: normalizedApiBaseUrl,
};
