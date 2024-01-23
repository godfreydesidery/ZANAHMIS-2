import { IGoodsReceivedNote } from "./goods-received-note"
import { IGoodsReceivedNoteDetailBatch } from "./goods-received-note-detail-batch"
import { IItem } from "./item"

export interface IGoodsReceivedNoteDetail {
    id            : any

    goodsReceivedNote : IGoodsReceivedNote
    
    item    : IItem
    orderedQty  : number
    receivedQty : number
    price : number
    status : string

    goodsReceivedNoteDetailBatches : IGoodsReceivedNoteDetailBatch[]

    created     : string
    verified    : string
    approved    : string

     /**
     * Auxilliary attributes
     */

      sn : number
}