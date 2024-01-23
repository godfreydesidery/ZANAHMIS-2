import { IAdmission } from "./admission"
import { IClinician } from "./clinician"
import { IConsultation } from "./consultation"
import { IInsurancePlan } from "./insurance-plan"
import { IMedicine } from "./medicine"
import { INonConsultation } from "./non-consultation"
import { INurse } from "./nurse"
import { IPatient } from "./patient"
import { IPatientBill } from "./patient-bill"

export interface IPatientConsumableChart {

    id : any
    qty : number
    status : string
    paymentType : string
    membershipNo : string

    consultation : IConsultation
    nonConsultation : INonConsultation
	admission : IAdmission
	medicine : IMedicine
    patientBill : IPatientBill
    patient : IPatient
    clinician : IClinician
	nurse : INurse
    insurancePlan : IInsurancePlan
    
    created : string
}