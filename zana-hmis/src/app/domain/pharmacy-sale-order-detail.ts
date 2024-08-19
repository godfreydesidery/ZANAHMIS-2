import { IMedicine } from "./medicine"
import { IPatientBill } from "./patient-bill"
import { IPharmacy } from "./pharmacy"
import { IPharmacySaleOrder } from "./pharmacy-sale-order"

export interface IPharmacySaleOrderDetail {
    id          : any
    unit        : string
    dosage      : string
    frequency   : string
    route       : string
    days        : string
    qty         : number
    issued      : number
    balance     : number
    stock       : number
    checked     : boolean
    status      : string
    instructions : string

    membershipNo : string
    paymentType : string

    pharmacySaleOrder    : IPharmacySaleOrder
    patientBill     : IPatientBill
    medicine        : IMedicine

    pharmacy   : IPharmacy

    
    created : string
    ordered : string
    accepted : string
    rejected : string
    rejectComment : string
    collected : string
    verified : string
    approved : string

    //prescriptionBatches : IPrescriptionBatch[]

    /**
     * Auxiliary attributes
     */
     sn : number
}