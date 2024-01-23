import { IMedicine } from "./medicine"
import { IPharmacyToPharmacyBatch } from "./pharmacy-to-pharmacy-batch"
import { IPharmacyToPharmacyRO } from "./pharmacy-to-pharmacy-r-o"
import { IPharmacyToPharmacyTO } from "./pharmacy-to-pharmacy-t-o"
import { IStoreToPharmacyBatch } from "./store-to-pharmacy-batch"

export interface IPharmacyToPharmacyTODetail {
    id                  : any
    pharmacyToPharmacyTO   : IPharmacyToPharmacyTO
    medicine            : IMedicine

    orderedQty       : number
	transferedQty    : number

    pharmacyToPharmacyBatches : IPharmacyToPharmacyBatch[]

    receivedQty : number

    created     : string
    verified    : string
    approved    : string
}