import { IPharmacy } from "./pharmacy"
import { IPharmacyToStoreRODetail } from "./pharmacy-to-store-r-o-detail"
import { IStore } from "./store"
import { IStoreToPharmacyTO } from "./store-to-pharmacy-t-o"

export interface IPharmacyToStoreRO {
    id            : any
    no            : string
    orderDate     : Date
    validUntil    : Date
    status        : string
    statusDescription : string

    pharmacy      : IPharmacy
    store : IStore

    pharmacyToStoreRODetails : IPharmacyToStoreRODetail[]

    storeToPharmacyTO : IStoreToPharmacyTO

    
    created     : string
    verified    : string
    approved    : string
}