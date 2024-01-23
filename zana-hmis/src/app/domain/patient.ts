import { IDay } from "./day"
import { IInsurancePlan } from "./insurance-plan"
import { IUser } from "./user"


export interface IPatient {
    id              : any
    no              : string
    //searchKey       : string
    firstName       : string
    middleName      : string
    lastName        : string
    gender          : string
    dateOfBirth     : Date
    type            : string
    nationality     : string
    nationalId      : string
    passportNo      : string
    address         : string
    phoneNo         : string
    email           : string
    paymentType     : string
    membershipNo    : string
    kinFullName     : string
    kinRelationship : string
    kinPhoneNo      : string
    active          : boolean
    insurancePlan   : IInsurancePlan
    created       : string
}