import { IDay } from "./day"
import { IUser } from "./user"

export interface IPatientPayment {
    id      : any
    amount  : number
    status  : string
    
    createdBy       : IUser
    createdOn       : IDay
    createdAt       : Date
}