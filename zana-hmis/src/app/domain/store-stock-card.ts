import { IItem } from "./item"
import { IStore } from "./store"

export interface IStoreStockCard{
    id      : any

    qtyIn : number
    qtyOut : number
    balance : number

    reference : string

    item : IItem
    store : IStore

    created : string

    /**
     * Auxiliary attributes
     */
    sn : number

}