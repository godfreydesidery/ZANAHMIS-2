import { IMedicine } from "./medicine"
import { IPharmacy } from "./pharmacy"
import { IPharmacyCustomerBill } from "./pharmacy-customer-bill"
import { IPharmacySale } from "./pharmacy-sale"

export interface IPharmacySaleDetail {
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

    pharmacySale    : IPharmacySale
    pharmacyCustomerBill     : IPharmacyCustomerBill
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