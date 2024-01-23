import { IGoodsReceivedNoteDetail } from "./goods-received-note-detail"

export interface IGoodsReceivedNoteDetailBatch {
    
	id : any
    
	no : string
	manufacturedDate : Date
	expiryDate : Date
	
    goodsReceivedNoteDetail : IGoodsReceivedNoteDetail
}