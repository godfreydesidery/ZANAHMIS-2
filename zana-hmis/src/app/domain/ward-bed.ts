import { IDay } from "./day"
import { IUser } from "./user"
import { IWard } from "./ward"

export interface IWardBed {
    id      : any
    no      : string
    status  : string
    active : boolean

    selected : boolean

    ward    : IWard
    
    created : string
}