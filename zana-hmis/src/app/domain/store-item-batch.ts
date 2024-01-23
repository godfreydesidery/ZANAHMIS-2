import { IItem } from "./item"
import { IStore } from "./store"

export interface IStoreItemBatch{

    id : any
    no : string
    manufacturedDate : Date
    expiryDate : Date
    qty : number
    
    store : IStore
    item : IItem
}