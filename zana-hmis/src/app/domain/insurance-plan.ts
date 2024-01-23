import { IDay } from "./day"
import { IInsuranceProvider } from "./insurance-provider"
import { IUser } from "./user"

export interface IInsurancePlan {
    id      : any
    code    : string
    name    : string
    description : string
    status  : string
    active  : boolean

    insuranceProvider : IInsuranceProvider
    
    createdBy       : IUser
    createdOn       : IDay
    createdAt       : Date
}