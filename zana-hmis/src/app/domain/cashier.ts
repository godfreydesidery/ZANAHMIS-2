import { IClinic } from "./clinic"
import { IDay } from "./day"
import { IUser } from "./user"

export interface ICashier {
    id          : any
    code        : string
    type        : string
    firstName   : string
    middleName  : string
    lastName    : string
    nickname    : string
    active      : boolean

    user : IUser
    
    createdBy       : IUser
    createdOn       : IDay
    createdAt       : Date
}