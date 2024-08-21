import { IPatientBill } from "./patient-bill"
import { IPharmacist } from "./pharmacist"
import { IPharmacy } from "./pharmacy"
import { IPharmacyCustomer } from "./pharmacy-customer"
import { IPharmacySaleOrderDetail } from "./pharmacy-sale-order-detail"

export interface IPharmacySaleOrder {
    id : any

    no : string

    pharmacyCustomer : IPharmacyCustomer

    status          : string

    patientBill : IPatientBill
    pharmacist       : IPharmacist
    pharmacy          : IPharmacy
    
    pharmacySaleOrderDetails : IPharmacySaleOrderDetail[]

    created     : string
    verified    : string
    approved    : string
    canceled    : string

    createdAt       : Date

    /**
     * Auxiliary attributes
     */
    sn : number

}