import { IAdmissionBed } from "./admission-bed";
import { IConsultation } from "./consultation";
import { ILabTest } from "./lab-test";
import { IPrescription } from "./prescription";
import { IProcedure } from "./procedure";
import { IRadiology } from "./radiology";
import { IRegistration } from "./registration";

export interface IRevenueBreakDownReport{
    id : any
    registrations : IRegistration[]
    consultations : IConsultation[]
    radiologies : IRadiology[]
    labTests : ILabTest[]
    procedures : IProcedure[]
    prescriptions : IPrescription[]
    admissionBeds : IAdmissionBed[]
}