import { IDay } from "./day"
import { IUser } from "./user"

export interface IClinic {
    id              : any
    code            : string
    name            : string
    description     : string
    consultationFee : number
    active          : boolean
    
    createdBy       : IUser
    createdOn       : IDay
    createdAt       : Date

    assigned : boolean
}