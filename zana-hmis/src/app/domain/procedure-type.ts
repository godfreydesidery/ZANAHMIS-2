import { IDay } from "./day"
import { IUser } from "./user"

export interface IProcedureType {
    id          : any
    code        : string
    name        : string
    description : string
    price       : number
    uom         : string
    active      : boolean
    
    createdBy       : IUser
    createdOn       : IDay
    createdAt       : Date
}