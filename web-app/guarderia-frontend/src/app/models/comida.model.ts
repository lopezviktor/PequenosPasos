import { Child } from './child.model';
import { Educator } from './educator.model';

export interface Comida {
    id?: number;
    nino: Child;
    educador: Educator;
    horaComida: string;
    descripcionComida: string;
    observaciones?: string;
}
