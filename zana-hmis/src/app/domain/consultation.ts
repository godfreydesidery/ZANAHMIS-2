import { IPatientBill } from "./patient-bill"
import { IClinic } from "./clinic"
import { IClinician } from "./clinician"
import { IDay } from "./day"
import { IInsurancePlan } from "./insurance-plan"
import { IPatient } from "./patient"
import { IUser } from "./user"
import { IVisit } from "./visit"

export interface IConsultation {
    id              : any
    status          : string
    paymentType     : string
    membershipNo    : string

    patient         : IPatient
    patientBill     : IPatientBill
    clinician       : IClinician
    clinic          : IClinic
    visit           : IVisit
    insurancePlan   : IInsurancePlan

    followUp        : boolean
    
    created : string

    createdAt       : Date

    /**
     * Auxiliary attributes
     */
    sn : number
}