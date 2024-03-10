import { IMedicine } from "../domain/medicine"
import { IPatient } from "../domain/patient"
import { IPatientBill } from "../domain/patient-bill"


export interface IPrescriptionCollection{
    sn : number
    description : string
    medicine : IMedicine
    patientBill : IPatientBill
    patient : IPatient
    //user : IUser
    cashier : string
    dateTime : string
}