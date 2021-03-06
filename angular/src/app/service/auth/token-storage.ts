import {Injectable} from "@angular/core";

const TOKEN_KEY = "xAuthToken";

@Injectable({
  providedIn: "root"
})
export class TokenStorage {

  constructor() {
  }

  save(token) {
    window.localStorage[TOKEN_KEY] = token;
  }

  get() {
    return window.localStorage[TOKEN_KEY];
  }

  destroy() {
    window.localStorage.removeItem(TOKEN_KEY);
  }

}
