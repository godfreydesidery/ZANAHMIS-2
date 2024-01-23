import { IAdmission } from "./admission"
import { IPatientBill } from "./patient-bill"
import { IConsultation } from "./consultation"
import { IDay } from "./day"
import { ILabTestType } from "./lab-test-type"
import { INonConsultation } from "./non-consultation"
import { IPatient } from "./patient"
import { IUser } from "./user"
import { IDiagnosisType } from "./diagnosis-type"
import { IInsurancePlan } from "./insurance-plan"
import { ILabTestAttachment } from "./lab-test-attachment"

export interface ILabTest {

    id      : any
    result  : string
    report  : string 
    description   : string   
    range   : string
    level   : string
    unit    : string
    status  : string

    membershipNo : string
    paymentType : string

    patient         : IPatient
    consultation    : IConsultation
    nonConsultation : INonConsultation
    admission       : IAdmission
    patientBill     : IPatientBill
    labTestType     : ILabTestType
    diagnosisType   : IDiagnosisType

    labTestAttachments : ILabTestAttachment[]

    insurancePlan   : IInsurancePlan

    created : string
    ordered : string
    accepted : string
    rejected : string
    rejectComment : string
    collected : string
    verified : string

    createdBy : IUser
    createdAt : Date

    collectedBy : IUser
    collectedAt : Date
    verifiedBy : IUser
    verifieddAt : Date


    /**
     * Auxilliary attributes
     */

    sn : number
    checked : boolean
}