import { IPatient } from "./patient"

export interface ICollection {
    id : any
    amount : number
    itemName : string
    paymentChannel : string
    paymentReferenceNo : string

    patient : IPatient

    created : string

    /**
     * Auxiliary
     */
    sn : any

}