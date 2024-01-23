import { IItem } from "./item"
import { IMedicine } from "./medicine"
import { IStoreToPharmacyBatch } from "./store-to-pharmacy-batch"
import { IStoreToPharmacyRN } from "./store-to-pharmacy-r-n"

export interface IStoreToPharmacyRNDetail{
    id : any
    orderedPharmacySKUQty : number
    receivedPharmacySKUQty : number
    receivedStoreSKUQty : number

    status : string

    storeToPharmacyRN : IStoreToPharmacyRN
    medicine : IMedicine
    item : IItem

    storeToPharmacyBatches : IStoreToPharmacyBatch[]

    created : string

   
}