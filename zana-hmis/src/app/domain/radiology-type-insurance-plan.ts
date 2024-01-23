import { IDay } from "./day"
import { IInsurancePlan } from "./insurance-plan"
import { IRadiologyType } from "./radiology-type"
import { IUser } from "./user"

export interface IRadiologyTypeInsurancePlan {
    id      : any
    price   : number

    covered : boolean

    radiologyType : IRadiologyType
    insurancePlan : IInsurancePlan

    created : string
}