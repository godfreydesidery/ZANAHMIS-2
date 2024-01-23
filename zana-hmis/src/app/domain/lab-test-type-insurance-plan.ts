import { IDay } from "./day"
import { IInsurancePlan } from "./insurance-plan"
import { ILabTestType } from "./lab-test-type"
import { IUser } from "./user"

export interface ILabTestTypeInsurancePlan {
    id : any

    price   : number

    covered : boolean

    labTestType     : ILabTestType
    insurancePlan   : IInsurancePlan
    
    created : string
    
}