import { IRadiologyType } from "../domain/radiology-type"
import { IPatient } from "../domain/patient"
import { IPatientBill } from "../domain/patient-bill"
import { IUser } from "./user"

export interface IRadiologyCollection{
    sn : number
    description : string
    radiologyType : IRadiologyType
    patientBill : IPatientBill
    patient : IPatient
    //user : IUser
    cashier : string
    dateTime : string
}