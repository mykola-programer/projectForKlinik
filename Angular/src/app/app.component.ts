import {Component, OnInit} from "@angular/core";
import {AuthService} from "./service/auth.service";
import {ToastMessageService} from "./service/toast-message.service";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.css"]
})
export class AppComponent implements OnInit {

  isLogging: boolean = false;

  constructor(private auth: AuthService,
              private toastMessageService: ToastMessageService,
  ) {
  }

  ngOnInit() {
    this.auth.isLogging.subscribe(value => this.isLogging = value);
  }

}
