import { IMedicine } from "./medicine"
import { IMedicineInsurancePlan } from "./medicine-insurance-plan"

export interface IMedicinePrice{
    medicine : IMedicine
    medicineInsurancePlan : IMedicineInsurancePlan
    price : number
    covered : boolean
}