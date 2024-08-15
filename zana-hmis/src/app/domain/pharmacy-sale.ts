import { IPharmacist } from "./pharmacist"
import { IPharmacy } from "./pharmacy"
import { IPharmacyCustomer } from "./pharmacy-customer"
import { IPharmacyCustomerBill } from "./pharmacy-customer-bill"

export interface IPharmacySale {
    id : any

    pharmacyCustomer : IPharmacyCustomer

    status          : string
    paymentType     : string

    pharmacyCustomerBill : IPharmacyCustomerBill
    pharmacist       : IPharmacist
    Pharmacy          : IPharmacy
    
    created : string

    createdAt       : Date

    /**
     * Auxiliary attributes
     */
    sn : number

}