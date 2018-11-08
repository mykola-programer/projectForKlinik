import { Component, OnInit } from "@angular/core";
import {Surgeon} from "../backend_types/surgeon";
import {Router} from "@angular/router";
import {SurgeonService} from "../service/surgeon.service";
import {NavbarService} from "../service/navbar.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: "app-surgeon-editor",
  templateUrl: "./surgeon-editor.component.html",
  styleUrls: ["./surgeon-editor.component.css"]
})
export class SurgeonEditorComponent implements OnInit {
  public surgeons: Surgeon[] = [];
  public filteredSurgeons: Surgeon[] = [];
  public genders: string[] = ["Ч", "Ж"];

  // "1" - ASC,
  // "-1" - DESC
  private sorting_order = 1;

  constructor(private router: Router, private surgeonService: SurgeonService, private serviceNavbar: NavbarService) {
  }

  ngOnInit() {
    this.getSurgeons();
    this.serviceNavbar.change("surgeon");
  }


  getSurgeons() {
    this.surgeonService.getUnlockSurgeons().toPromise().then((surgeons: Surgeon[]) => {
      this.surgeons = surgeons;
      this.sortSurgeons(this.surgeons);
      this.filteredSurgeons = this.surgeons;
    });
  }

  onBlur(surgeon: Surgeon, element: HTMLInputElement) {
    if (!element.checkValidity()) {
      surgeon.isChanged = false;
    }
  }

  changeSurname(surgeon: Surgeon, surnameElement: HTMLInputElement) {
    if (surnameElement.checkValidity()) {
      surgeon.isChanged = true;
      surgeon.surname = surnameElement.value;
    } else {
      surgeon.isChanged = false;
      alert("Введіть корректне Прізвище! \n\n 1. Не пусте поле. \n 2. Тільки букви. \n 3. Мін. - 3 , Макс. - 50");
    }
  }

  changeFirstName(surgeon: Surgeon, firstNameElement: HTMLInputElement) {
    if (firstNameElement.checkValidity()) {
      surgeon.isChanged = true;
      surgeon.firstName = firstNameElement.value;
    } else {
      surgeon.isChanged = false;
      alert("Введіть корректне Ім'я! \n\n 1. Не пусте поле. \n 2. Тільки букви. \n 3. Мін. - 3 , Макс. - 50");
    }
  }

  changeSecondName(surgeon: Surgeon, secondNameElement: HTMLInputElement) {
    if (secondNameElement.checkValidity()) {
      surgeon.isChanged = true;
      surgeon.secondName = secondNameElement.value;
    } else {
      surgeon.isChanged = false;
      alert("Введіть корректне по-Батькові! \n\n 1. Не пусте поле. \n 2. Тільки букви. \n 3. Мін. - 3 , Макс. - 50");
    }
  }

  changeSex(surgeon: Surgeon, sexElement: HTMLInputElement) {
    if (sexElement.checkValidity() && (sexElement.value === "Ч" || sexElement.value === "Ж")) {
      surgeon.isChanged = true;
      surgeon.sex = sexElement.value;
    } else {
      // alert("Виберіть корректну стать!");
    }
  }

  filteringSurgeons(surgeon_value: string) {
    if (surgeon_value) {
      const filterValue: string[] = surgeon_value.toLowerCase().split(" ");
      this.filteredSurgeons = this.surgeons.filter(surgeon => {
        switch (filterValue.length) {
          case 0:
            return true;
          case 1:
            if (surgeon.surname) {
              return (surgeon.surname.toLowerCase().indexOf(filterValue[0]) === 0);
            } else {
              return true;
            }
          case 2:
            if (surgeon.surname && surgeon.firstName) {
              return (surgeon.surname.toLowerCase().indexOf(filterValue[0]) === 0
                && surgeon.firstName.toLowerCase().indexOf(filterValue[1]) === 0);
            } else {
              return true;
            }
          default:
            if (surgeon.surname && surgeon.firstName && surgeon.secondName) {
              return (surgeon.surname.toLowerCase().indexOf(filterValue[0]) === 0
                && surgeon.firstName.toLowerCase().indexOf(filterValue[1]) === 0
                && surgeon.secondName.toLowerCase().indexOf(filterValue[2]) === 0);
            } else {
              return true;
            }
        }
      });
    } else {
      this.filteredSurgeons = this.surgeons;
    }
  }

  onAdd() {
    if (this.filteredSurgeons[0].surname != null) {
      const surgeon = new Surgeon();
      surgeon.sex = "Ч";
      this.filteredSurgeons.unshift(surgeon);
      window.scroll(0, 0);
    }
  }

  onRefresh() {
    this.getSurgeons();
  }

  onSave() {
    // if (!this.filteredSurgeons[0].surname) {
    //   this.filteredSurgeons.splice(0, 1);
    // }
      let isSuccess = true;
    const new_surgeons: Surgeon[] = this.filteredSurgeons.filter((surgeon: Surgeon) => {
      return surgeon.isChanged && surgeon.surgeonId === 0;
    });
    if (new_surgeons.length > 0) {
      new_surgeons.forEach((surgeon: Surgeon) => {
        this.surgeonService.addSurgeon(surgeon).toPromise().then((returned_surgeon: Surgeon) => {
          this.copySurgeon(returned_surgeon, surgeon);
          surgeon.isChanged = false;
        }).catch((err: HttpErrorResponse) => {

          const div = document.createElement("div");
          div.innerHTML = err.error.text;
          const text = div.textContent || div.innerText || "";

          alert(text);
          isSuccess = false;
        });
      });
    }

    const edit_surgeons: Surgeon[] = this.filteredSurgeons.filter((surgeon: Surgeon) => {
      return surgeon.isChanged && surgeon.surgeonId !== 0;
    });
    edit_surgeons.forEach((surgeon: Surgeon) => {
      this.surgeonService.editSurgeon(surgeon).toPromise().then(() => {
        surgeon.isChanged = false;
      }).catch((err: HttpErrorResponse) => {

        const div = document.createElement("div");
        div.innerHTML = err.error.text;
        const text = div.textContent || div.innerText || "";

        alert(text);
        isSuccess = false;
      });
    });
    if (isSuccess) {
      // console.log("Sorting");
      this.sortSurgeons(this.filteredSurgeons);
    }
  }

  onDelete() {
    const surgeons_for_lock: Surgeon[] = this.filteredSurgeons.filter((surgeon: Surgeon) => {
      return surgeon.isChanged && surgeon.surgeonId !== 0;
    });
    surgeons_for_lock.forEach((surgeon: Surgeon) => {
      this.surgeonService.lockSurgeon(surgeon).toPromise().then((returned_surgeon: Surgeon) => {
        if (returned_surgeon.surgeonId === surgeon.surgeonId && returned_surgeon.lock === true) {
          this.filteredSurgeons.splice(this.filteredSurgeons.indexOf(surgeon, 0), 1);
        }
      });
    });
  }

  onCancel() {
    this.onRefresh();
    // this.router.navigateByUrl("");

  }

  private sortSurgeons(surgeons: Surgeon[]) {
    surgeons.sort((surgeon1, surgeon2) => {
      if (surgeon1.surname && surgeon2.surname && surgeon1.surname.localeCompare(surgeon2.surname) !== 0) {
        return surgeon1.surname.localeCompare(surgeon2.surname) * this.sorting_order;
      } else if (surgeon1.firstName && surgeon2.firstName && surgeon1.firstName.localeCompare(surgeon2.firstName) !== 0) {
        return surgeon1.firstName.localeCompare(surgeon2.firstName) * this.sorting_order;
      } else if (surgeon1.secondName && surgeon2.secondName && surgeon1.secondName.localeCompare(surgeon2.secondName) !== 0) {
        return surgeon1.secondName.localeCompare(surgeon2.secondName) * this.sorting_order;
      }
    });
  }

  private copySurgeon(original: Surgeon, result: Surgeon) {
    result.surgeonId = original.surgeonId;
    result.surname = original.surname;
    result.firstName = original.firstName;
    result.secondName = original.secondName;
    result.sex = original.sex;
    result.lock = original.lock;
    result.isChanged = original.isChanged;
  }
}
