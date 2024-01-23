import { IWardType } from "./ward-type"
import { IWardTypeInsurancePlan } from "./ward-type-insurance-plan"

export interface IWardTypePrice{
    wardType : IWardType
    wardTypeInsurancePlan : IWardTypeInsurancePlan
    price : number
    covered : boolean
    
}