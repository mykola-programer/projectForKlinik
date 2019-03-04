import {EventEmitter, Injectable} from "@angular/core";

@Injectable({
  providedIn: "root"
})
export class AuthService {

  isLogging: EventEmitter<boolean> = new EventEmitter(false);

  constructor() {
  }

}
