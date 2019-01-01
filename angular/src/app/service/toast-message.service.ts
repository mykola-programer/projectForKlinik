import {Injectable} from "@angular/core";
import {ToastaConfig, ToastaService, ToastData, ToastOptions} from "ngx-toasta";

@Injectable({
  providedIn: "root"
})
export class ToastMessageService {

  constructor(private toastaService: ToastaService,
              private toastaConfig: ToastaConfig,
  ) {
    this.toastaConfig.position = "top-center";
  }

  public inform(title: string, msg: string, command: string, timeout?: number) {
    const toastOptions: ToastOptions = {
      title: title,
      msg: msg,
      showClose: true,
      timeout: timeout,
      theme: "bootstrap",
      onAdd: (toast: ToastData) => {
      },
      onRemove: function (toast: ToastData) {
      }
    };
    switch (command) {
      case "default":
        if (timeout == null) {
          toastOptions.timeout = 3000;
        }
        this.toastaService.default(toastOptions);
        break;
      case "info":
        if (timeout == null) {
          toastOptions.timeout = 6000;
        }
        this.toastaService.info(toastOptions);
        break;
      case "success":
        if (timeout == null) {
          toastOptions.timeout = 3000;
        }
        this.toastaService.success(toastOptions);
        break;
      case "wait":
        if (timeout == null) {
          toastOptions.timeout = 3000;
        }
        this.toastaService.wait(toastOptions);
        break;
      case "error":
        if (timeout == null) {
          toastOptions.timeout = 8000;
        }
        this.toastaService.error(toastOptions);
        break;
      case "warning":
        if (timeout == null) {
          toastOptions.timeout = 8000;
        }
        this.toastaService.warning(toastOptions);
        break;
    }
  }

}
