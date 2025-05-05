import { Component, OnInit } from '@angular/core';
import { Clase } from '@models/clase.model';
import { Educator } from '@models/educator.model';
import { ClassroomService } from '@services/classroom/classroom.service';
import { EducatorService } from '@services/educator/educator.service';
import { CommonModule } from '@angular/common';
import { ClassCardComponent } from '@components/class-card/class-card.component';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { ClassFormComponent } from '@components/forms/class-form/class-form.component';
import { ConfirmationService } from 'primeng/api';
import { ConfirmDialogModule } from 'primeng/confirmdialog';

@Component({
  selector: 'app-classes-page',
  imports: [
    CommonModule, 
    ClassCardComponent, 
    DialogModule, 
    ButtonModule,
    ClassFormComponent,
    ConfirmDialogModule
  ],
  providers: [ConfirmationService],
  templateUrl: './classes-page.component.html',
  styleUrl: './classes-page.component.scss'
})

export class ClassesPageComponent implements OnInit {
  clases: Clase[] = [];
  mostrarDialogoClase: boolean = false;
  claseEditando: Clase | null = null;
  educadores: Educator[] = [];
  
  constructor(
    private classroomService: ClassroomService,
    private educatorService: EducatorService,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit(): void {
    this.classroomService.getClases().subscribe({
      next: (clases) => this.clases = clases,
      error: (err) => console.error('Error cargando clases:', err)
    });
    this.educatorService.getEducators().subscribe({
      next: (educadores) => this.educadores = educadores,
      error: (err) => console.error('Error cargando educadores:', err)
    });
  }

  abrirDialogoParaNuevaClase() {
    this.claseEditando = null;
    this.mostrarDialogoClase = true;
  }

  abrirDialogoParaEditarClase(clase: Clase) {
    this.claseEditando = clase;
    this.mostrarDialogoClase = true;
  }

  guardarClase(clase: Clase) {
    const observable = clase.id 
      ? this.classroomService.actualizarClase(clase) 
      : this.classroomService.crearClase(clase);
  
    observable.subscribe({
      next: () => {
        this.mostrarDialogoClase = false;
        this.claseEditando = null;
        this.classroomService.getClases().subscribe(clases => this.clases = clases);
      },
      error: err => console.error('Error al guardar clase:', err)
    });
  }

  confirmarEliminarClase(clase: Clase) {
    this.confirmationService.confirm({
      message: `¿Estás seguro de que quieres eliminar la clase "${clase.nombre}"?`,
      header: 'Confirmar eliminación',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.classroomService.deleteClase(clase.id!).subscribe({
          next: () => {
            this.clases = this.clases.filter(c => c.id !== clase.id);
          },
          error: err => console.error('Error al eliminar clase:', err)
        });
      }
    });
  }

  cancelarDialogoClase() {
    this.mostrarDialogoClase = false;
    this.claseEditando = null;
  }
}