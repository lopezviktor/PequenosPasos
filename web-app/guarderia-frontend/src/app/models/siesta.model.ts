import { Child } from "./child.model";
import { Educator } from "./educator.model";

export interface Siesta {
    id?: number;
    nino: Child;
    educador: Educator;
    inicioSiesta: Date;
    finSiesta?: Date;
    observaciones?: string;
}