import { IMedicine } from "./medicine"
import { IPharmacy } from "./pharmacy"

export interface IPharmacyStockCard {

    id          : any
    qtyIn       : number
    qtyOut      : number
    balance     : number
    reference   : string
    dateTime    : Date
    medicine    : IMedicine
    pharmacy    : IPharmacy

    created     : string

    /**
     * Auxilliary attributes
     */
    sn : number
}