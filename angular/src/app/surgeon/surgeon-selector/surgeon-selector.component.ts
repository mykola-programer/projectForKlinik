import {Component, OnDestroy, OnInit} from "@angular/core";
import {Surgeon} from "../../types/surgeon";
import {SurgeonService} from "../../service/surgeon.service";
import {GlobalService} from "../../service/global.service";
import {Router} from "@angular/router";
import {ToastMessageService} from "../../service/toast-message.service";
import {Subscription} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: "app-surgeon-selector",
  templateUrl: "./surgeon-selector.component.html",
  styleUrls: ["./surgeon-selector.component.css"]
})
export class SurgeonSelectorComponent implements OnInit, OnDestroy {
  private defaultSurgeon = "Совва";
  surgeons: Surgeon[] = [];
  selectedSurgeon: Surgeon = this.globalService.getSurgeon();
  surgeons_loading = false;

  constructor(private surgeonService: SurgeonService,
              private globalService: GlobalService,
              private router: Router,
              private toastMessageService: ToastMessageService) {
  }

  private surgeonSubscriber: Subscription;

  ngOnInit() {
    this.getSurgeons();
    this.surgeonSubscriber = this.globalService.emittedSurgeon.subscribe(selectedSurgeon => {
      this.selectedSurgeon = selectedSurgeon;
    });
  }

  ngOnDestroy(): void {
    this.surgeonSubscriber.unsubscribe();
  }

  getSurgeons() {
    this.surgeons_loading = true;
    this.surgeonService.getSurgeons().toPromise().then((surgeons: Surgeon[]) => {
      this.surgeons = surgeons;
      if (!this.globalService.getSurgeon()) {
        this.change(surgeons.find(value => value.surname === this.defaultSurgeon));
      } else {
        if (this.surgeons.some(value => value.surgeonId === this.globalService.getSurgeon().surgeonId)){
          this.selectedSurgeon = this.globalService.getSurgeon();
        } else {
          this.change(surgeons.find(value => value.surname === this.defaultSurgeon));
        }
      }
      this.surgeons_loading = false;
    }).catch((err: HttpErrorResponse) => {
      this.surgeons_loading = true;
      this.toastMessageService.inform("Сервер недоступний!",
        "Спробуйте пізніше !" + "<br>" + err.error + "<br>" + err.message, "error", 10000);
      setTimeout(() => {
        this.getSurgeons();
      }, 15000);
    });
  }

  change(surgeon: Surgeon) {
    this.globalService.changeSurgeon(surgeon);
  }

  addSurgeon() {
    this.router.navigateByUrl("surgeon");
    // console.log(this.selectedSurgeon);
  }
}
