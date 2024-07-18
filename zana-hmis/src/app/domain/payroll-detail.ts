import { IEmployee } from "./employee-register"

export interface IPayrollDetail{
    id : any
    name : string
    basicSalary : number
    grossSalary : number
    netSalary : number
    addOns : number
    deductions : number
    employerContributions : number

    employee : IEmployee
}