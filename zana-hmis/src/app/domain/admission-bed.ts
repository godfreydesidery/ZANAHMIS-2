import { IAdmission } from "./admission"
import { IPatientBill } from "./patient-bill"
import { IDay } from "./day"
import { IPatient } from "./patient"
import { IUser } from "./user"
import { IWard } from "./ward"
import { IWardBed } from "./ward-bed"

export interface IAdmissionBed {
    id : any

    status : string

    patient     : IPatient
    wardBed     : IWardBed
    patientBill : IPatientBill
    admission   : IAdmission

    opened : string

    closed : string

    admitted : string
}