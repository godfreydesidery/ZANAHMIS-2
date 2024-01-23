import { IDay } from "./day"
import { IUser } from "./user"
import { IWard } from "./ward"
import { IInsurancePlan } from "./insurance-plan"
import { IWardType } from "./ward-type"

export interface IWardTypeInsurancePlan {
    id      : any
    price   : number
    active : boolean

    covered : boolean

    wardType : IWardType

    insurancePlan : IInsurancePlan
    
    createdBy       : IUser
    createdOn       : IDay
    createdAt       : Date
}