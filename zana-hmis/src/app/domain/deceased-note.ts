import { Time } from "@angular/common"
import { IAdmission } from "./admission"
import { IConsultation } from "./consultation"
import { IDay } from "./day"
import { IPatient } from "./patient"
import { IUser } from "./user"

export interface IDeceasedNote {

    id : any
    patientSummary : string
    causeOfDeath : string
    date : Date
    time : Time
    status : string
    
    admission : IAdmission
    consultation : IConsultation

    patient : IPatient

    createdBy : IUser
    createdOn : IDay
    createdAt : Date

    approvedBy : IUser
    approvedOn : IDay
    approvedAt : Date
	
}