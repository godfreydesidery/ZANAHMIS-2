import { IAdmission } from "./admission"
import { IDay } from "./day"
import { IUser } from "./user"

export interface IDischargePlan{
    id : any
    history : string
    investigation : string
    management : string
    operationNote : string
    icuAdmissionNote : string
    generalRecommendation : string

    admission : IAdmission

    createdBy       : IUser
    createdOn       : IDay
    createdAt       : Date
    
    created : string

}