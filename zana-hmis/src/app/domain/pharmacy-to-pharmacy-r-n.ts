import { IPharmacy } from "./pharmacy"
import { IPharmacyToPharmacyRNDetail } from "./pharmacy-to-pharmacy-r-n-detail"
import { IPharmacyToPharmacyTO } from "./pharmacy-to-pharmacy-t-o"

export interface IPharmacyToPharmacyRN {
    id : any
    no : string
    receivingDate : Date
    status : string

    pharmacy : IPharmacy
    pharmacyToPharmacyTO : IPharmacyToPharmacyTO

    created : string
    verified : string
    approved : string

    pharmacyToPharmacyRNDetails : IPharmacyToPharmacyRNDetail[]
}