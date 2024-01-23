import { IDay } from "./day"
import { IInsurancePlan } from "./insurance-plan"
import { IPatient } from "./patient"
import { IUser } from "./user"
import { IVisit } from "./visit"
import { IWard } from "./ward"
import { IWardBed } from "./ward-bed"

export interface IAdmission {
    id              : any
    paymentType     : string
    membershipNo    : string
    status          : string

    patient         : IPatient
    visit           : IVisit
    insurancePlan   : IInsurancePlan
    wardBed         : IWardBed

    created     : string
    admitted    : string
    discharged  : string
}