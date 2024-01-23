import { ILabTestType } from "./lab-test-type";
import { ILabTestTypeInsurancePlan } from "./lab-test-type-insurance-plan";

export interface ILabTestTypePrice{
    labTestType : ILabTestType
    labTestTypeInsurancePlan : ILabTestTypeInsurancePlan
    price : number
    covered : boolean
}