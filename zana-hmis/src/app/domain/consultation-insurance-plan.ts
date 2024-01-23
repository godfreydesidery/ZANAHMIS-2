import { IClinic } from "./clinic"
import { IDay } from "./day"
import { IInsurancePlan } from "./insurance-plan"
import { IUser } from "./user"

export interface IConsultationInsurancePlan {
    id      : any
    consultationFee   : number

    covered : boolean

    clinic : IClinic
    insurancePlan : IInsurancePlan
    
    created : string
}