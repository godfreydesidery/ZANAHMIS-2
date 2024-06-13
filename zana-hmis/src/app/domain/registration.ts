import { IPatientBill } from "./patient-bill"
import { IDay } from "./day"
import { IPatient } from "./patient"
import { IUser } from "./user"

export interface IRegistration {
    id      : any
    status  : string

    patient : IPatient
    bill    : IPatientBill
    patientBill : IPatientBill
    
    createdBy       : IUser
    createdOn       : IDay
    createdAt       : Date
}