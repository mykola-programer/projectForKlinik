import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {ClientEditorComponent} from "./client-editor/client-editor.component";
import {DateEditorComponent} from "./date/date-editor/date-editor.component";
import {AccomodationComponent} from "./accomodation/accomodation.component";
import {SurgeonEditorComponent} from "./surgeon-editor/surgeon-editor.component";
import {ManagerEditorComponent} from "./manager-editor/manager-editor.component";
import {VisitComponent} from "./visit/visit.component";
import {OperationTypeEditorComponent} from "./operation-type-editor/operation-type-editor.component";

const routes: Routes = [
  {path: "", pathMatch: "full", redirectTo: "visit"},
  // {path: "accomodation", component: AccomodationComponent},
  {path: "visit", component: VisitComponent},
  {path: "dates", component: DateEditorComponent},
  {path: "client", component: ClientEditorComponent},
  {path: "surgeon", component: SurgeonEditorComponent},
  {path: "manager", component: ManagerEditorComponent},
  {path: "operation-type", component: OperationTypeEditorComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
