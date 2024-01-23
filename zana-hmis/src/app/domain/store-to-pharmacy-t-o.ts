import { IPharmacy } from "./pharmacy"
import { IPharmacyToStoreRO } from "./pharmacy-to-store-r-o"
import { IPharmacyToStoreRODetail } from "./pharmacy-to-store-r-o-detail"
import { IStore } from "./store"
import { IStoreToPharmacyTODetail } from "./store-to-pharmacy-t-o-detail"

export interface IStoreToPharmacyTO {
    id            : any
    no            : string
    orderDate     : Date
    status        : string
    statusDescription : string

    pharmacy      : IPharmacy
    pharmacyToStoreRO : IPharmacyToStoreRO

    store : IStore

    storeToPharmacyTODetails : IStoreToPharmacyTODetail[]

    
    created     : string
    verified    : string
    approved    : string
}