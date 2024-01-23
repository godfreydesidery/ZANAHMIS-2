import { IProcedureType } from "./procedure-type"
import { IProcedureTypeInsurancePlan } from "./procedure-type-insurance-plan"

export interface IProcedureTypePrice{
    procedureType : IProcedureType
    procedureTypeInsurancePlan : IProcedureTypeInsurancePlan
    price : number
    covered : boolean
}