import { IItem } from "./item";
import { ISupplier } from "./supplier";
import { ISupplierItemPrice } from "./supplier-item-price";

export interface ISupllierItemPriceList {
    supplier : ISupplier
    supplierItemPrices : ISupplierItemPrice[]
}