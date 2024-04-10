import { IRadiology } from "./radiology"

export interface IRadiologyAttachment{
    id : any

    name : string
    fileName : string

    radiology : IRadiology
}