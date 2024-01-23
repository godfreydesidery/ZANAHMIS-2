import { IDay } from "./day"
import { IUser } from "./user"

export interface IInsuranceProvider {
    id      : any
    code    : string
    name    : string
    address : string
    telephone   : string
    fax     : string
    email   : string
    website : string
    active  : boolean

    
    createdBy       : IUser
    createdOn       : IDay
    createdAt       : Date
}