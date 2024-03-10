import { IPatient } from "../domain/patient"
import { IPatientBill } from "../domain/patient-bill"

export interface IAdmissionBedCollection{
    sn : number
    description : string
    patientBill : IPatientBill
    patient : IPatient
    //user : IUser
    cashier : string
    dateTime : string
}