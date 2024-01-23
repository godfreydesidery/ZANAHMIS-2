import { IMedicine } from "./medicine"
import { IPharmacyToPharmacyTODetail } from "./pharmacy-to-pharmacy-t-o-detail"

export interface IPharmacyToPharmacyBatch{
    id : any
    no : string
    qty : number
    manufacturedDate : Date
    expiryDate : Date

    pharmacyToPharmacyTODetail : IPharmacyToPharmacyTODetail
    //pharmacyToPharmacyRNDetail : IPharmacyToPharmacyRNDetail
    medicine : IMedicine

    checked : boolean
}