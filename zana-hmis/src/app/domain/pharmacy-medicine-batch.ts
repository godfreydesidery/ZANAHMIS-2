import { IMedicine } from "./medicine"
import { IPharmacy } from "./pharmacy"

export interface IPharmacyMedicineBatch{
    id : any
    no : string
    manufacturedDate : Date
    expiryDate : Date
    qty : number
    
    pharmacy : IPharmacy
    medicine : IMedicine
}