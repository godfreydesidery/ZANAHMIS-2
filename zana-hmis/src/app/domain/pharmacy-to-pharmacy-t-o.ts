import { IPharmacy } from "./pharmacy"
import { IPharmacyToPharmacyRO } from "./pharmacy-to-pharmacy-r-o"
import { IPharmacyToPharmacyTODetail } from "./pharmacy-to-pharmacy-t-o-detail"

export interface IPharmacyToPharmacyTO {
    id            : any
    no            : string
    orderDate     : Date
    status        : string
    statusDescription : string

    requestingPharmacy      : IPharmacy
    deliveringPharmacy      : IPharmacy
    pharmacyToPharmacyRO : IPharmacyToPharmacyRO

    pharmacyToPharmacyTODetails : IPharmacyToPharmacyTODetail[]

    
    created     : string
    verified    : string
    approved    : string
}