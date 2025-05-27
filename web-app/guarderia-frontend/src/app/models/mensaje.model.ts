export interface Mensaje {
  id: number;
  contenido: string;
  fechaHora: string;
  estado: 'NO_LEIDO' | 'LEIDO';
  emisor: {
    id: number;
    nombre: string;
    apellidos: string;
    tipoUsuario: 'EDUCADOR' | 'PADRE' | 'ADMINISTRADOR';
  };
  receptor: {
    id: number;
    nombre: string;
    apellidos: string;
    tipoUsuario: 'EDUCADOR' | 'PADRE' | 'ADMINISTRADOR';
  };
}