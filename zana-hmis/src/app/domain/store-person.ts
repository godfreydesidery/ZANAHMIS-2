import { IDay } from "./day"
import { IStore } from "./store"
import { IUser } from "./user"

export interface IStorePerson {
    id          : any
    code        : string
    type        : string
    firstName   : string
    middleName  : string
    lastName    : string
    nickname    : string
    active      : boolean

    stores : IStore[]

    user : IUser
    
    createdBy       : IUser
    createdOn       : IDay
    createdAt       : Date
}