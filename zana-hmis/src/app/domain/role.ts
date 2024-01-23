import { IDay } from "./day"
import { IUser } from "./user"

export interface IRole {
    id      : any
    name    : string
    owner   : string
    granted : boolean
    active  : boolean
    
    created       : string
    
}