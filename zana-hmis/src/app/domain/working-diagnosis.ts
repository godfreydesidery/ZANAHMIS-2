import { IConsultation } from "./consultation"
import { IDay } from "./day"
import { IDiagnosisType } from "./diagnosis-type"
import { IUser } from "./user"

export interface IWorkingDiagnosis {
    id              : any
    description     : string

    consultation    : IConsultation
    diagnosisType   : IDiagnosisType
    
    created       : string
}