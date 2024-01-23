import { IMedicine } from "./medicine"
import { IPharmacy } from "./pharmacy"
import { IPharmacyMedicineBatch } from "./pharmacy-medicine-batch"

export interface IPharmacyMedicine{
    id          : any
    stock       : number
    pharmacy    : IPharmacy
    medicine    : IMedicine

    pharmacyMedicineBatches : IPharmacyMedicineBatch[]

    /**
     * Auxiliary attributes
     */
    sn : number
}