import { IDay } from "./day"
import { IUser } from "./user"

export interface ITheatre {
    id              : any
    code            : string
    name            : string
    description     : string
    location        : string
    active          : boolean
    
    createdBy       : IUser
    createdOn       : IDay
    createdAt       : Date
}