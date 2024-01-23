import { ILocalPurchaseOrderDetail } from "./local-purchase-order-detail"
import { IStore } from "./store"
import { ISupplier } from "./supplier"

export interface ILocalPurchaseOrder {
    id            : any
    no            : string
    orderDate     : Date
    validUntil    : Date
    status        : string
    statusDescription : string

    supplier      : ISupplier
    store : IStore

    localPurchaseOrderDetails : ILocalPurchaseOrderDetail[]

    created     : string
    verified    : string
    approved    : string
}