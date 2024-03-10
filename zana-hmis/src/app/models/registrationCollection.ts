import { IPatient } from "../domain/patient"
import { IPatientBill } from "../domain/patient-bill"

export interface IRegistrationCollection{
    sn : number
    description : string
    patientBill : IPatientBill
    patient : IPatient
    //user : IUser
    cashier : string
    dateTime : string
}