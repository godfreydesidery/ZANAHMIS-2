import { IMedicine } from "./medicine"
import { IPharmacyToStoreRO } from "./pharmacy-to-store-r-o"

export interface IPharmacyToStoreRODetail {
    id            : any
    pharmacyToStoreRO : IPharmacyToStoreRO
    medicine    : IMedicine
    orderedQty  : number
    receivedQty : number

    created     : string
    verified    : string
    approved    : string
}