import { IAdmission } from "./admission"
import { IClinician } from "./clinician"
import { IConsultation } from "./consultation"
import { IInsurancePlan } from "./insurance-plan"
import { INonConsultation } from "./non-consultation"
import { INurse } from "./nurse"
import { IPatient } from "./patient"
import { IPatientBill } from "./patient-bill"
import { IProcedureType } from "./procedure-type"

export interface IPatientDressingChart {

    id : any
    qty : number
    paymentType : string
    membershipNo : string

    consultation : IConsultation
    nonConsultation : INonConsultation
    admission : IAdmission
    clinician : IClinician
    nurse : INurse
    procedureType : IProcedureType
    patientBill : IPatientBill
    patient : IPatient
    insurancePlan : IInsurancePlan
    
    created : string


}