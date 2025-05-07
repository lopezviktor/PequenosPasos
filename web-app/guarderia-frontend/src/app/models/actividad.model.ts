import { Educator } from "./educator.model";
import { Child } from "./child.model";
import { Clase } from "./clase.model";

export interface Actividad {
    id?: number;
    actividadId?: number;
    nombre: string;
    descripcion: string;
    educador?: Educator;
    ninos?: Child[];
    fecha?: string;
    clase?: Clase;
}

export interface ActividadConDetalles extends Actividad {
    educadorNombre?: string;
    claseNombre?: string;
}