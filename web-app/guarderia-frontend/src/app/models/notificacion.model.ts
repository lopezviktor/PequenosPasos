export interface Notificacion {
  id: number;
  mensaje: string;
  fechaHora: string;
  estado: string;
  receptorId: number;
  emisorId: number;
}