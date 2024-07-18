import { IPayrollDetail } from "./payroll-detail"

export interface IPayroll{
    id : any
    no : string
    name : string
    description: string
    startDate : Date
    endDate : Date
    status : string
    statusDescription : string
    comments : string

    payrollDetails : IPayrollDetail[]

    created     : string
    verified    : string
    approved    : string
}