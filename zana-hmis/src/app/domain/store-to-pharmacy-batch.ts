import { IItem } from "./item"
import { IStoreToPharmacyRNDetail } from "./store-to-pharmacy-r-n-detail"
import { IStoreToPharmacyTODetail } from "./store-to-pharmacy-t-o-detail"

export interface IStoreToPharmacyBatch{
    id : any
    no : string
    pharmacySKUQty : number
    storeSKUQty : number
    manufacturedDate : Date
    expiryDate : Date

    storeToPharmacyTODetail : IStoreToPharmacyTODetail
    storeToPharmacyRNDetail : IStoreToPharmacyRNDetail
    item : IItem

    checked : boolean
}