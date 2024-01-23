import { IDay } from "./day"
import { IInsurancePlan } from "./insurance-plan"
import { IUser } from "./user"

export interface IRegistrationInsurancePlan {
    id      : any
    registrationFee : number

    covered : boolean

    insurancePlan   : IInsurancePlan
    
    created : string
}