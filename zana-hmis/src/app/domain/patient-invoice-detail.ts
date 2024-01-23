import { IPatientBill } from "./patient-bill"
import { IDay } from "./day"
import { IPatient } from "./patient"
import { IPatientInvoice } from "./patient-invoice"
import { IUser } from "./user"

export interface IPatientInvoiceDetail {
    id : any

    patientInvoice  : IPatientInvoice
    patient         : IPatient
    patientBill     : IPatientBill

    checked : boolean


    
    createdBy : IUser
    createdOn : IDay
    createdAt : Date
}