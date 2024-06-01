import { IInsurancePlan } from "./insurance-plan"

export interface IRevenue{
    sn : number
    insurancePlan : IInsurancePlan
    registration : number
    consultation : number
    radiology : number
    labTest : number
    procedure : number
    prescription : number
    admissionBed : number
    total : number
}
