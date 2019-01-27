import {Component, Input, OnInit} from "@angular/core";
import {MatDialog} from "@angular/material";
import {NavbarService} from "../service/navbar.service";

@Component({
  selector: "app-navbar",
  templateUrl: "./navbar.component.html",
  styleUrls: ["./navbar.component.css"]
})
export class NavbarComponent implements OnInit {

  @Input() statusNavbar: string;

  constructor(private dialog: MatDialog, private serviceNavbar: NavbarService) {
  }

  ngOnInit() {
    this.serviceNavbar.statusNavbar.subscribe(statusNavbar => this.statusNavbar = statusNavbar);
  }

}
