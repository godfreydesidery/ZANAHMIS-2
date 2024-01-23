import { IDay } from "./day"
import { IUser } from "./user"

export interface IMedicine {
    id : any
    code        : string
    name        : string
    description : string
    type        : string
    price       : number
    uom         : string
    category    : string
    active      : boolean
    
    createdBy       : IUser
    createdOn       : IDay
    createdAt       : Date
}