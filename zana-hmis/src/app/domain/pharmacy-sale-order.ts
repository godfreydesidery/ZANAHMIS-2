import { IPatientBill } from "./patient-bill"
import { IPharmacist } from "./pharmacist"
import { IPharmacy } from "./pharmacy"
import { IPharmacyCustomer } from "./pharmacy-customer"

export interface IPharmacySaleOrder {
    id : any

    pharmacyCustomer : IPharmacyCustomer

    status          : string

    patientBill : IPatientBill
    pharmacist       : IPharmacist
    pharmacy          : IPharmacy
    
    created : string

    createdAt       : Date

    /**
     * Auxiliary attributes
     */
    sn : number

}