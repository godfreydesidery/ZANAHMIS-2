import { IConsultation } from "./consultation"


export interface IPatientVital {
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

    status : string

    consultation        : IConsultation

    
    created       : string

}