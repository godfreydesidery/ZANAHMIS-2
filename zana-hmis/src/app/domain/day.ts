import { IUser } from "./user"

export interface IDay {
    id : any
    bussinessDate : string
    systemDate : string
    
    startedBy       : IUser
    startedOn       : IDay
    startedAt       : Date

    endedBy       : IUser
    endedOn       : IDay
    endedAt       : Date
}