export interface Asistencia {
    id: number;
    nino: {
      id: number;
      nombre: string;
      apellidos: string;
    };
    padreEntrega?: {
      id: number;
      nombre: string;
    };
    educadorRecibe: {
      id: number;
      nombre: string;
      apellidos: string;
    };
    horaEntrada: string;  
    educadorEntrega?: {
      id: number;
      nombre: string;
    };
    padreRecoge?: {
      id: number;
      nombre: string;
    };
    horaSalida?: string; 
  }