import { IItem } from "./item"
import { IMedicine } from "./medicine"

export interface IItemMedicineCoefficient{
    id : any
    coefficient : number
    item : IItem
    medicine : IMedicine

    itemQty : number
    medicineQty : number

    created : string
}