import { IPharmacy } from "./pharmacy"
import { IStoreToPharmacyRNDetail } from "./store-to-pharmacy-r-n-detail"
import { IStoreToPharmacyTO } from "./store-to-pharmacy-t-o"

export interface IStoreToPharmacyRN {
    id : any
    no : string
    receivingDate : Date
    status : string

    pharmacy : IPharmacy
    storeToPharmacyTO : IStoreToPharmacyTO

    created : string
    verified : string
    approved : string

    storeToPharmacyRNDetails : IStoreToPharmacyRNDetail[]
}