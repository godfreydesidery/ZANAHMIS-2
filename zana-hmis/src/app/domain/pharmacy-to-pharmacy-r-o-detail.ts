import { IMedicine } from "./medicine"
import { IPharmacyToPharmacyRO } from "./pharmacy-to-pharmacy-r-o"

export interface IPharmacyToPharmacyRODetail {
    id            : any
    pharmacyToPharmacyRO : IPharmacyToPharmacyRO
    medicine    : IMedicine
    orderedQty  : number
    receivedQty : number

    created     : string
    verified    : string
    approved    : string
}