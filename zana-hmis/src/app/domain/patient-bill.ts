import { IDay } from "./day"
import { IInsurancePlan } from "./insurance-plan"
import { IPatient } from "./patient"
import { IUser } from "./user"

export interface IPatientBill {
    id          : any
    description : string
    qty         : number
    amount      : number
    paid        : number
    balance     : number
    status      : string


    patient : IPatient

    insurancePlan : IInsurancePlan

    membershipNo : string
    
    created : string

    createdAt : Date
}