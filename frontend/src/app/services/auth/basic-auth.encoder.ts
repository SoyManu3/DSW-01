export function encodeBasicAuth(email: string, password: string): string {
  const value = `${email}:${password}`;
  return `Basic ${btoa(value)}`;
}
