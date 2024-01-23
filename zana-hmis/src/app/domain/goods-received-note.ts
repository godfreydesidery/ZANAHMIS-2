import { IGoodsReceivedNoteDetail } from "./goods-received-note-detail"
import { ILocalPurchaseOrder } from "./local-purchase-order"
import { IStore } from "./store"

export interface IGoodsReceivedNote {
    id            : any
    no            : string
    store         : IStore
    status        : string
    statusDescription : string

    orderedQty : number
    receivedQty : number

    localPurchaseOrder : ILocalPurchaseOrder

    goodsReceivedNoteDetails : IGoodsReceivedNoteDetail[]

    created     : string
    verified    : string
    approved    : string
}
    