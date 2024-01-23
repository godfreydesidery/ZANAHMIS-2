import { IRegistrationInsurancePlan } from "./registration-insurance-plan"

export interface IRegistrationPrice{
    registrationInsurancePlan : IRegistrationInsurancePlan
    price : number
    covered : boolean
}