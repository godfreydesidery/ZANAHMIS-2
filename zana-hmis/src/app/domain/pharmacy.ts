import { IDay } from "./day"
import { IUser } from "./user"

export interface IPharmacy {
    id              : any
    code            : string
    name            : string
    description     : string
    location        : string
    category        : string
    active          : boolean
    
    created         : string
}