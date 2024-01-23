import { IItem } from "./item"
import { IMedicine } from "./medicine"
import { IPharmacyToPharmacyBatch } from "./pharmacy-to-pharmacy-batch"
//import { IPharmacyToPharmacyBatch } from "./pharmacy-to-pharmacy-batch"
import { IPharmacyToPharmacyRN } from "./pharmacy-to-pharmacy-r-n"

export interface IPharmacyToPharmacyRNDetail{
    id : any
    orderedQty : number
    receivedQty : number

    status : string

    pharmacyToPharmacyRN : IPharmacyToPharmacyRN
    medicine : IMedicine

    pharmacyToPharmacyBatches : IPharmacyToPharmacyBatch[]

    created : string

   
}