import { IPharmacyCustomer } from "./pharmacy-customer"

export interface IPharmacyCustomerBill {
    id          : any
    description : string
    qty         : number
    amount      : number
    paid        : number
    balance     : number
    status      : string

    pharmacyCustomer : IPharmacyCustomer

    created : string

    createdAt : Date
}