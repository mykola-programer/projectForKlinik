import {Component, Input, OnInit} from "@angular/core";
import {MatDialog} from "@angular/material";
import {AuthService} from "../service/auth.service";
import {GlobalService} from "../service/global.service";

@Component({
  selector: "app-navbar",
  templateUrl: "./navbar.component.html",
  styleUrls: ["./navbar.component.css"]
})
export class NavbarComponent implements OnInit {

  @Input() statusNavbar: string;

  constructor(private dialog: MatDialog,
              private globalService: GlobalService,
              private authService: AuthService) {
  }

  ngOnInit() {
    this.globalService.statusNavbar.subscribe(statusNavbar => this.statusNavbar = statusNavbar);
  }

  logOut() {
    this.authService.isLogging.emit(false);
  }
}
