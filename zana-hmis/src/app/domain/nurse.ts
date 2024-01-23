import { IUser } from "./user"

export interface INurse{
    id          : any
    code        : string
    type        : string
    firstName   : string
    middleName  : string
    lastName    : string
    nickname    : string
    active      : boolean


    user : IUser
    
    created : string
}