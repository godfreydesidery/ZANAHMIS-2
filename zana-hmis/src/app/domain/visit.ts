import { IDay } from "./day"
import { IPatient } from "./patient"
import { IUser } from "./user"

export interface IVisit {
    id          : any
    sequence    : string
    status      : string
    
    patient     : IPatient

    createdBy       : IUser
    createdOn       : IDay
    createdAt       : Date
}