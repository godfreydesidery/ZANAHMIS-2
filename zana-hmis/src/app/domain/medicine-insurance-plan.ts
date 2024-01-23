import { IDay } from "./day"
import { IInsurancePlan } from "./insurance-plan"
import { IMedicine } from "./medicine"
import { IUser } from "./user"

export interface IMedicineInsurancePlan {
    id : any
    price   : number

    covered : boolean

    medicine : IMedicine
    insurancePlan : IInsurancePlan
    
    created : string
}