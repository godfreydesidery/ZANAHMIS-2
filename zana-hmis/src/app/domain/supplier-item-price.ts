import { IItem } from "./item"
import { ISupplier } from "./supplier"

export interface ISupplierItemPrice {
    id : any
    price : number
    active : boolean
    terms : string

    supplier : ISupplier
    item : IItem
}