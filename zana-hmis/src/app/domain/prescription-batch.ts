import { IPrescription } from "./prescription"

export interface IPrescriptionBatch {
    id : any
    no : string
    qty : number
    manufacturedDate : Date
    expiryDate : Date

    prescription : IPrescription

    checked : boolean
}