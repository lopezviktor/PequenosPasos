import { Component, Input, Output, EventEmitter, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ComidaService } from '@services/comida/comida.service';
import { Comida } from '@models/comida.model';
import { Educator } from '@models/educator.model';
import { Child } from '@models/child.model';
import { EducatorService } from '@services/educator/educator.service'; 
import { ChildService } from '@services/child/child.service'; 
import { SelectModule } from 'primeng/select';
import { CalendarModule } from 'primeng/calendar';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-comida-form',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SelectModule,
    CalendarModule,
    ButtonModule
  ],
  standalone: true,
  templateUrl: './comida-form.component.html',
  styleUrls: ['./comida-form.component.scss']
})
export class ComidaFormComponent implements OnInit {
  @Input() comidaEditando: Comida | null = null; // Recibe los datos de la comida si se está editando
  @Output() submitComida = new EventEmitter<Comida>();
  @Output() cancelar = new EventEmitter<void>();

  form: FormGroup;
  ninos: Child[] = []; // Lista de niños
  educadores: Educator[] = []; // Lista de educadores

  constructor(
    private fb: FormBuilder,
    private comidaService: ComidaService,
    private childService: ChildService, 
    private educatorService: EducatorService 
  ) {
    this.form = this.fb.group({
      nino: [null, Validators.required],
      educador: [null, Validators.required],
      horaComida: [null, Validators.required],
      descripcionComida: ['', Validators.required],
      observaciones: ['']
    });
  }

  ngOnInit(): void {
    this.cargarDatos();
    this.cargarNinos();
    this.cargarEducadores();
  }

  cargarDatos() {
    if (this.comidaEditando) {
      this.form.patchValue({
        nino: this.comidaEditando.nino,
        educador: this.comidaEditando.educador,
        horaComida: this.comidaEditando.horaComida,
        descripcionComida: this.comidaEditando.descripcionComida,
        observaciones: this.comidaEditando.observaciones || ''
      });
    }
  }

  // Cargar niños desde el servicio
  cargarNinos() {
    this.childService.getAllChildren().subscribe((ninos) => {
      this.ninos = ninos;
    });
  }

  // Cargar educadores desde el servicio
  cargarEducadores() {
    this.educatorService.getEducators().subscribe((educadores) => {
      this.educadores = educadores;
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['comidaEditando'] && this.comidaEditando) {
      this.form.patchValue({
        nino: this.comidaEditando.nino,
        educador: this.comidaEditando.educador,
        horaComida: this.comidaEditando.horaComida,
        descripcionComida: this.comidaEditando.descripcionComida,
        observaciones: this.comidaEditando.observaciones || ''
      });
    }
  }

  onSubmit() {
    if (this.form.valid) {
      const comida = this.form.value;
      console.log('Datos enviados:', comida);  // Verifica los datos antes de enviarlos
  
      if (this.comidaEditando && this.comidaEditando.id !== undefined) {
        this.comidaService.updateComida(this.comidaEditando.id, comida).subscribe(
          () => {
            alert('Comida actualizada correctamente');
          },
          (error) => {
            console.error('Error al guardar comida:', error);
          }
        );
      } else {
        this.comidaService.createComida(comida).subscribe(
          () => {
            alert('Comida creada correctamente');
          },
          (error) => {
            console.error('Error al guardar comida:', error);
          }
        );
      }
    }
  }
}