import {Component, OnInit} from "@angular/core";
import {AuthService} from "../service/auth.service";
import {UserService} from "../service/user.service";
import {User} from "../backend_types/user";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {ToastMessageService} from "../service/toast-message.service";

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.css"]
})
export class LoginComponent implements OnInit {
  users: User[];
  isBadConnection = false;
  invalidPassword = false;
  invalidLogin = false;

  constructor(private authService: AuthService,
              private userService: UserService,
              private http: HttpClient,
              private toastMessageService: ToastMessageService) {
  }

  ngOnInit() {
    this.getUsers();
  }

  getUsers() {
    this.userService.getUsers().toPromise().then(users => {
      this.users = users;
      this.isBadConnection = false;
    }).catch((err: HttpErrorResponse) => {
      this.isBadConnection = true;
      this.toastMessageService.inform("Сервер недоступний!",
        "Спробуйте пізніше !" + "<br>" + err.error + "<br>" + err.message, "error", 10000);
      setTimeout(() => {
        this.getUsers();
      }, 15000);
    });
  }

  loginUser(login, password) {
    const user: User = this.users.find((value: User) => value.username === login);
    if (user) {
      user.password = password;
      this.userService.loginUser(user).subscribe((isLogged: boolean) => {
          this.authService.isLogging.emit(isLogged);
          this.invalidLogin = false;
          this.invalidPassword = !isLogged;
          if (!isLogged) {
            this.toastMessageService.inform("Помилка авторизації!", "Неправельний пароль!", "error", 1000);
          }
        },
        (err: HttpErrorResponse) => {
          this.invalidLogin = true;
          this.invalidPassword = true;
          this.toastMessageService.inform("Помилка авторизації!", err.error, "error");
        });
    } else {
      this.invalidPassword = true;
      this.invalidLogin = true;
    }
  }

  test() {
    const user = new User();
    user.username = "admin";
    user.password = "123";
     this.userService.testLogin(user).subscribe(value => console.log(value));
  }
}
