import { IDay } from "./day"
import { IUser } from "./user"

export interface ISupplier {
    id                  : any
    code                : string
    name                : string
    contactName         : string
    active              : boolean
    tin                 : string
    vrn                 : string
    termsOfContract     : string
    physicalAddress     : string
    postCode            : string
    postAddress         : string
    telephone           : string
    mobile              : string
    email               : string
    fax                 : string
    bankAccountName     : string
    bankPhysicalAddress : string
    bankPostCode        : string
    bankPostAddress     : string
    bankName            : string
    bankAccountNo       : string

    createdBy       : IUser
    createdOn       : IDay
    createdAt       : Date

}