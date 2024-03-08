import { ILabTestType } from "../domain/lab-test-type"
import { IPatient } from "../domain/patient"
import { IPatientBill } from "../domain/patient-bill"
import { IUser } from "./user"

export interface ILabTestCollection{
    sn : number
    description : string
    labTestType : ILabTestType
    patientBill : IPatientBill
    patient : IPatient
    //user : IUser
}