import { Child } from './child.model';
import { Educator } from './educator.model';

export interface Higiene {
  id?: number;
  nino: Child;
  educador: Educator;
  fechaHora: string | Date;
  estado: 'NORMAL' | 'ESTREÑIDO' | 'SUELTO';
  observaciones?: string;
}