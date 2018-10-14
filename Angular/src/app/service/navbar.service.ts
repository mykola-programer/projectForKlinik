import {EventEmitter, Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class NavbarService {
  statusNavbar: EventEmitter<string> = new EventEmitter();

  constructor() {
  }

  public change(status: string) {
    this.statusNavbar.emit(status);
  }
}

