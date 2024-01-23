import { IAdmission } from "./admission"
import { IClinician } from "./clinician"
import { IConsultation } from "./consultation"
import { INonConsultation } from "./non-consultation"
import { INurse } from "./nurse"
import { IPatient } from "./patient"
import { IPrescription } from "./prescription"

export interface IPatientPrescriptionChart {

    id : any
    dosage : string
    output : string
    remark : string

    consultation : IConsultation
    nonConsultation : INonConsultation
    admission : IAdmission
    prescription : IPrescription
    patient : IPatient
    clinician : IClinician
    nurse : INurse

    created : string

}