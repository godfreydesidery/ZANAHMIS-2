import { IAdmission } from "./admission"
import { IPatientBill } from "./patient-bill"
import { IConsultation } from "./consultation"
import { IDay } from "./day"
import { INonConsultation } from "./non-consultation"
import { IPatient } from "./patient"
import { IProcedureType } from "./procedure-type"
import { IUser } from "./user"
import { Time } from "@angular/common"
import { ITheatre } from "./theatre"
import { IDiagnosisType } from "./diagnosis-type"
import { IInsurancePlan } from "./insurance-plan"

export interface IProcedure {
    id      : any
    note    : string
    diagnosisType : IDiagnosisType
    type    : string
    time    : Time
    date    : Date
    hours   : number
    minutes : number
    status  : string

    theatre : ITheatre

    patient         : IPatient
    consultation?    : IConsultation
    nonConsultation? : INonConsultation
    admission?       : IAdmission
    patientBill?     : IPatientBill
    procedureType   : IProcedureType

    insurancePlan   : IInsurancePlan
    
    created        : string
    accepted       : string
    rejected       : string
    rejectComment  : string
    verified       : string
    held           : string

    createdAt      : Date

    /**
     * Auxiliary attributes
     */
     sn : number
}