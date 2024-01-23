import { IPharmacyToPharmacyRODetail } from "./pharmacy-to-pharmacy-r-o-detail"
import { IPharmacy } from "./pharmacy"
import { IPharmacyToPharmacyTO } from "./pharmacy-to-pharmacy-t-o"

export interface IPharmacyToPharmacyRO {
    id            : any
    no            : string
    orderDate     : Date
    validUntil    : Date
    status        : string
    statusDescription : string

    requestingPharmacy      : IPharmacy
    deliveringPharmacy : IPharmacy

    pharmacyToPharmacyRODetails : IPharmacyToPharmacyRODetail[]

    pharmacyToPharmacyTO : IPharmacyToPharmacyTO

    
    created     : string
    verified    : string
    approved    : string
}