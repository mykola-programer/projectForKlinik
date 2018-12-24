import {Component, Input, OnInit} from "@angular/core";
import {DateEditorComponent} from "../date/date-editor/date-editor.component";
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

  // addDate() {
  //   this.openDialogChangeDate();
  //   return false;
  // }

  private openDialogChangeDate() {
    const dialogRef = this.dialog.open(DateEditorComponent, {
      width: "550px",
      height: "330px",
    });
    dialogRef.afterClosed().subscribe(() => {
      console.log("The dialog was closed !");
      // this.setNgbDatepickerConfig();
      // this.getVisitDates();
    });
  }

  stop() {
    return false;
  }
}
