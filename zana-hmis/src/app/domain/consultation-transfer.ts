import { IClinic } from "./clinic"
import { IClinician } from "./clinician"
import { IConsultation } from "./consultation"

export interface IConsultationTransfer {
    id : any
    consultation : IConsultation
    clinic : IClinic

    reason : string

    status : string

    created : string
}