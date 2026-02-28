import { environment } from '../environments/environment';

declare global {
  interface Window {
    __env?: { apiUrl: string };
  }
}

export const appConfig = {
  get apiUrl(): string {
    return window.__env?.apiUrl ?? environment.apiUrl;
  },
};
