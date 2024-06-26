import { IConsultation } from "./consultation"
import { IDay } from "./day"
import { IDiagnosisType } from "./diagnosis-type"
import { IUser } from "./user"

export interface IFinalDiagnosis {
    id              : any
    description     : string

    consultation    : IConsultation
    diagnosisType   : IDiagnosisType

    doctor : string

    created         : string
}