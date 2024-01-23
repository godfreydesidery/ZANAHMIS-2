import { IRadiologyType } from "./radiology-type"
import { IRadiologyTypeInsurancePlan } from "./radiology-type-insurance-plan"

export interface IRadiologyTypePrice{
    radiologyType : IRadiologyType
    radiologyTypeInsurancePlan : IRadiologyTypeInsurancePlan
    price : number
    covered : boolean
}