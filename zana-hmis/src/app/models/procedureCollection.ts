import { IProcedureType } from "../domain/procedure-type"
import { IPatient } from "../domain/patient"
import { IPatientBill } from "../domain/patient-bill"
import { IUser } from "./user"

export interface IProcedureCollection{
    sn : number
    description : string
    procedureType : IProcedureType
    patientBill : IPatientBill
    patient : IPatient
    //user : IUser
    cashier : string
    dateTime : string
}