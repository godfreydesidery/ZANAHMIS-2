import { IDay } from "./day"
import { IPatientBill } from "./patient-bill"
import { IPatientPayment } from "./patient-payment"
import { IUser } from "./user"

export interface IPatientPaymentDetail {
    id : any
    status : string

    bill : IPatientBill
    patientPayment : IPatientPayment

    createdBy       : IUser
    createdOn       : IDay
    createdAt       : Date
}