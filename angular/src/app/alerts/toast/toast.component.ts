import {Component, OnInit} from "@angular/core";
import {ToastaConfig, ToastaService, ToastData, ToastOptions} from "ngx-toasta";

@Component({
  selector: "app-toast",
  templateUrl: "./toast.component.html",
  styleUrls: ["./toast.component.css"]
})
export class ToastComponent implements OnInit {

  constructor(
    private toastaService: ToastaService,
    private toastaConfig: ToastaConfig,
  ) {
    this.toastaConfig.theme = "default";
  }

  ngOnInit() {
  }
  public addToast() {
    // Just add default Toast with title only
    this.toastaService.default("Hi there");
    // Or create the instance of ToastOptions
    const toastOptions: ToastOptions = {
      title: "My title",
      msg: "The message",
      showClose: true,
      timeout: 5000,
      theme: "default",
      onAdd: (toast: ToastData) => {
        console.log("Toast " + toast.id + " has been added!");
      },
      onRemove: function(toast: ToastData) {
        console.log("Toast " + toast.id + " has been removed!");
      }
    };
    // Add see all possible types in one shot
    this.toastaService.info(toastOptions);
    this.toastaService.success(toastOptions);
    this.toastaService.wait(toastOptions);
    this.toastaService.error(toastOptions);
    this.toastaService.warning(toastOptions);
  }
}
