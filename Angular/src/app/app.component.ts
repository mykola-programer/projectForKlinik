import {Component, OnInit} from "@angular/core";
import {AuthService} from "./service/auth/auth.service";
import {ToastMessageService} from "./service/toast-message.service";
import {UrlProperty} from "./service/url-property";
import {User} from "./types/user";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.css"]
})
export class AppComponent implements OnInit {

  isLogging = true; // TODO false

  constructor(private auth: AuthService,
              private toastMessageService: ToastMessageService,
  ) {
  }

  ngOnInit() {
    UrlProperty.authUser.subscribe((user: User) => {
      user ? this.isLogging = true : this.isLogging = false;
    });
  }

}
