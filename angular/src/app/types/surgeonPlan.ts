import {DatePlan} from "./date-plan";

export class SurgeonPlan {
  public surgeonPlanId = 0;
  // public datePlanId: number;
  public datePlan: DatePlan;
  public surgeonId: number;
  public disable = false;
  public isChanged = false;

  constructor(datePlan?: DatePlan, surgeonId?: number, isChanged?: boolean) {
    this.datePlan = datePlan ? datePlan : null;
    this.surgeonId = surgeonId ? surgeonId : 0;
    this.surgeonPlanId = 0;
    this.disable = false;
    this.isChanged = isChanged ? isChanged : false;
  }
}
