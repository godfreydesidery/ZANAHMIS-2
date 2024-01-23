import { IAdmission } from "./admission"
import { IConsultation } from "./consultation"
import { IDay } from "./day"
import { IExternalMedicalProvider } from "./external-medical-provider"
import { IUser } from "./user"

export interface IReferralPlan{
    id : any
    referringDiagnosis : string
    history : string
    investigation : string
    management : string
    operationNote : string
    icuAdmissionNote : string
    generalRecommendation : string

    admission : IAdmission
    consultation : IConsultation

    externalMedicalProvider : IExternalMedicalProvider

    createdBy       : IUser
    createdOn       : IDay
    createdAt       : Date
    
    created : string
}