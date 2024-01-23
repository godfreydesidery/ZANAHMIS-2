import { IDay } from "./day"
import { IUser } from "./user"
import { IConsultation } from "./consultation"

export interface IGeneralExamination {
    temperature: string
    id                  : any
    bodyMassIndex       : string
    bodyMassIndexComment  : string
    bodySurfaceArea     :string
    height              : string
    pressure            : string
    pulseRate           : string
    respiratoryRate     : string
    saturationOxygen    : string
    weight              : string
    description         : string

    consultation        : IConsultation

    
    created       : string

}