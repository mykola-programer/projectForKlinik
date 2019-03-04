import {Component, Input, OnInit} from "@angular/core";
import {MatDialog} from "@angular/material";
import {NavbarService} from "../service/navbar.service";
import {AuthService} from "../service/auth.service";

@Component({
  selector: "app-navbar",
  templateUrl: "./navbar.component.html",
  styleUrls: ["./navbar.component.css"]
})
export class NavbarComponent implements OnInit {

  @Input() statusNavbar: string;

  constructor(private dialog: MatDialog,
              private serviceNavbar: NavbarService,
              private authService: AuthService,) {
  }

  ngOnInit() {
    this.serviceNavbar.statusNavbar.subscribe(statusNavbar => this.statusNavbar = statusNavbar);
  }

  logOut (){
    this.authService.isLogging.emit(false);
  }
}
