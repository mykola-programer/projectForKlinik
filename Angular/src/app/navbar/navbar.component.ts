import {Component, Input, OnInit} from "@angular/core";
import {MatDialog} from "@angular/material";
import {GlobalService} from "../service/global.service";
import {UrlProperty} from "../service/url-property";
import {User} from "../types/user";

@Component({
  selector: "app-navbar",
  templateUrl: "./navbar.component.html",
  styleUrls: ["./navbar.component.css"]
})
export class NavbarComponent implements OnInit {

  @Input() statusNavbar: string;
  authUser: User = null;

  constructor(private dialog: MatDialog,
              private globalService: GlobalService) {
  }

  ngOnInit() {
    this.globalService.statusNavbar.subscribe(statusNavbar => this.statusNavbar = statusNavbar);
    UrlProperty.authUser.subscribe(user => this.authUser = user);
  }

  logOut() {
    UrlProperty.authUser.emit(null);
    return false;
  }
}
