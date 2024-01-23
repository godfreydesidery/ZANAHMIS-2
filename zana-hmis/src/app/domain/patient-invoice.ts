import { IDay } from "./day"
import { IInsurancePlan } from "./insurance-plan"
import { IPatient } from "./patient"
import { IPatientInvoiceDetail } from "./patient-invoice-detail"
import { IUser } from "./user"

export interface IPatientInvoice {
    id              : any
    no              : string
    
    status          : string

    patient         : IPatient
    insurancePlan : IInsurancePlan

    patientInvoiceDetails : IPatientInvoiceDetail[]

    amountPaid : number
    amountAllocated : number
    amountUnallocated : number
    
    createdBy       : IUser
    createdOn       : IDay
    createdAt       : Date
}