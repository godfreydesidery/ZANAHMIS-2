import { IDay } from "./day"
import { IUser } from "./user"

export interface IWardCategory {
    id      : any
    code    : string
    name    : string
    description : string
    active : boolean
    
    created : string
}