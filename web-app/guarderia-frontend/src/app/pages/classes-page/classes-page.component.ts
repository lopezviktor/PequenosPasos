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

@Component({
  selector: 'app-classes-page',
  imports: [
    CommonModule, 
    ClassCardComponent, 
    DialogModule, 
    ButtonModule,
    ClassFormComponent
  ],
  templateUrl: './classes-page.component.html',
  styleUrl: './classes-page.component.scss'
})

export class ClassesPageComponent implements OnInit {
  clases: Clase[] = [];
  mostrarDialogoClase: boolean = false;
  claseEditando: Clase | null = null;
  educadores: Educator[] = [];
  
  constructor(private classroomService: ClassroomService, private educatorService: EducatorService) {}

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
    this.mostrarDialogoClase = false;
    this.classroomService.getClases().subscribe(clases => this.clases = clases); 
  }

  cancelarDialogoClase() {
    this.mostrarDialogoClase = false;
    this.claseEditando = null;
  }
}