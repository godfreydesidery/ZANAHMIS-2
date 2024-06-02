import { IAdmission } from "./admission";
import { IWard } from "./ward";

export interface IIPDReport {
    sn : number
    admission   : IAdmission
    admittedBy  : string
    admittedAt  : string
    dischargedAt : string
    ward        : IWard
    paymentType : string
    covered     : number
    invoice     : number
    paid        : number
    balance     : number
    total       : number
}