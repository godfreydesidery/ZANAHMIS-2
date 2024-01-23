import { IAdmission } from "./admission"
import { IClinician } from "./clinician"
import { IConsultation } from "./consultation"
import { INonConsultation } from "./non-consultation"
import { INurse } from "./nurse"
import { IPatient } from "./patient"

export interface IPatientNursingChart {

    id : any
    feeding : string
    changingPosition : string
    bedBathing : string
    randomBloodSugar : string
    fullBloodSugar : string
    drainageOutput : string
    fluidIntake : string
    urineOutput : string
    
    consultation : IConsultation
    nonConsultation : INonConsultation
    admission : IAdmission
    patient : IPatient
    clinician : IClinician
    nurse : INurse

    created : string

}