import { IDay } from "./day"
import { IInsurancePlan } from "./insurance-plan"
import { IPatient } from "./patient"
import { IUser } from "./user"
import { IVisit } from "./visit"

export interface INonConsultation {
    id : any
    status          : string
    paymentType     : string
    membershipNo    : string

    patient         : IPatient
    visit           : IVisit
    insurancePlan   : IInsurancePlan
    
    createdBy       : IUser
    createdOn       : IDay
    createdAt       : Date
}