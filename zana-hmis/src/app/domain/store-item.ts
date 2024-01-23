import { IItem } from "./item";
import { IStore } from "./store";
import { IStoreItemBatch } from "./store-item-batch";

export interface IStoreItem{
    store : IStore
    item : IItem
    stock : number

    minimumInventory : number
    maximumInventory : number
    defaultReorderQty : number
    defaultReorderLevel : number

    active : boolean

    storeItemBatches : IStoreItemBatch[]

    sn : number

    
}