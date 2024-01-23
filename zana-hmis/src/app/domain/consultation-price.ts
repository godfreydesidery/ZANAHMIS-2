import { IClinic } from "./clinic"
import { IConsultationInsurancePlan } from "./consultation-insurance-plan"

export interface IConsultationPrice{
    clinic : IClinic
    consultationInsurancePlan : IConsultationInsurancePlan
    price : number
    covered : boolean
}