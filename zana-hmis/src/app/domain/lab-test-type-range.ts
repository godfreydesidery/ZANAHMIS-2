import { ILabTestType } from "./lab-test-type"

export interface ILabTestTypeRange{
    id          : any
    name        : string
    labTestType : ILabTestType
    active      : boolean
  }