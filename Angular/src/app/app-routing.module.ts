import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {ClientEditorComponent} from "./client-editor/client-editor.component";
import {DateEditorComponent} from "./date/date-editor/date-editor.component";
import {SurgeonEditorComponent} from "./surgeon-editor/surgeon-editor.component";
import {ManagerEditorComponent} from "./manager-editor/manager-editor.component";
import {VisitComponent} from "./visit/visit.component";
import {OperationTypeEditorComponent} from "./operation-type-editor/operation-type-editor.component";
import {DepartmentEditorComponent} from "./department/department-editor/department-editor.component";
import {SurgeonPlanComponent} from "./surgeon-plan/surgeon-plan.component";

const routes: Routes = [
  {path: "", pathMatch: "full", redirectTo: "client"},
  {path: "visit", component: VisitComponent},
  {path: "dates", component: DateEditorComponent},
  {path: "client", component: ClientEditorComponent},
  {path: "surgeon", component: SurgeonEditorComponent},
  {path: "surgeonPlan", component: SurgeonPlanComponent},
  {path: "manager", component: ManagerEditorComponent},
  {path: "operation-type", component: OperationTypeEditorComponent},
  {path: "department", component: DepartmentEditorComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
