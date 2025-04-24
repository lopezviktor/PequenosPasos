import { Educator } from './educator.model';
import { Child } from './child.model'; 

export interface Clase {
  id: number;
  nombre: string;
  educador?: Educator;
  ninos?: Child[];
}