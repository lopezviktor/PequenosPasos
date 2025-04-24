import { Clase } from './clase.model';

export interface Child {
    id?: number;
    nombre: string;
    apellidos: string;
    fechaNacimiento: string;
    primerDia: string;
    alergias?: string;
    condicionesMedicas?: string;
    fotoUrl?: string;
    clase?: Clase;
}