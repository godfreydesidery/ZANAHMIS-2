import { IDay } from "./day"
import { IUser } from "./user"
import { IWardCategory } from "./ward-category"
import { IWardType } from "./ward-type"

export interface IWard {
    id      : any
    code    : string
    name    : string
    status  : string
    active  : boolean

    noOfBeds : number

    wardCategory    : IWardCategory
    wardType        : IWardType
    
    created : string
}