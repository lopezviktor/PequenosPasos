import { Pipe, PipeTransform } from '@angular/core';

interface PersonaConNombre {
  nombre: string;
  apellidos: string;
}

@Pipe({
  name: 'nombreCompleto',
  standalone: true
})
export class NombreCompletoPipe implements PipeTransform {
  transform(value: PersonaConNombre | null | undefined): string {
    if (!value) return '';
    return `${value.nombre} ${value.apellidos}`;
  }
}