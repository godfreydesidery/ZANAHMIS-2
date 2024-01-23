import { IDay } from "./day"
import { IInsurancePlan } from "./insurance-plan"
import { IProcedureType } from "./procedure-type"
import { IUser } from "./user"

export interface IProcedureTypeInsurancePlan {
    id      : any
    price   : number

    covered : boolean

    procedureType : IProcedureType
    insurancePlan : IInsurancePlan
    
    created : string
}