import {Pipe, PipeTransform} from "@angular/core";
import {AbstractControl} from "@angular/forms";

@Pipe({
  name: "filterDepartment"
})
export class FilterDepartmentPipe implements PipeTransform {

  transform(controls: AbstractControl[], search_value: string): any {
    if (!controls) return null;
    if (!search_value) {
      return controls;
    } else {
      return controls.filter((control: AbstractControl) => {
        return control.get("name").value.toLowerCase().indexOf(search_value.toLowerCase()) === 0
          || control.get("departmentId").value === 0;
      });
    }
  }
}
