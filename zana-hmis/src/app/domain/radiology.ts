///import { Byte } from '@angular/compiler/src/util';
import { IAdmission } from "./admission"
import { IPatientBill } from "./patient-bill"
import { IConsultation } from "./consultation"
import { IDay } from "./day"
import { INonConsultation } from "./non-consultation"
import { IPatient } from "./patient"
import { IRadiologyType } from "./radiology-type"
import { IUser } from "./user"
import { IDiagnosisType } from "./diagnosis-type"
import { Byte } from "src/custom-packages/util"
import { IInsurancePlan } from "./insurance-plan"
import { IRadiologyAttachment } from "./radiology-attachment"

export interface IRadiology {
    id          : any
    result      : string
    report      : string
    diagnosisType   : IDiagnosisType
    description : string
    attachment  : Byte[]

    status      : string

    

    patient         : IPatient
    consultation    : IConsultation
    nonConsultation : INonConsultation
    admission       : IAdmission
    patientBill     : IPatientBill
    radiologyType   : IRadiologyType

    insurancePlan   : IInsurancePlan

    radiologyAttachments : IRadiologyAttachment[]
    
    //createdBy       : IUser
    //createdOn       : IDay
    //createdAt       : Date

    created : string
    accepted : string
    rejected : string
    verified : string

    //acceptedBy       : IUser
    //acceptedOn       : IDay
    //acceptedAt       : Date

    //rejectedBy       : IUser
    //rejectedOn       : IDay
    //rejectedAt       : Date
    rejectComment    : string

    //verifiedBy       : IUser
    //verifiedOn       : IDay
    //verifiedAt       : Date

    createdAt : Date

    /**
     * Auxiliary attributes
     */
     sn : number
}