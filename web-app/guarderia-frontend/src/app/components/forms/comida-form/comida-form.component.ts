import { Component, Input, Output, EventEmitter, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Comida } from '@models/comida.model';
import { Child } from '@models/child.model';
import { Educator } from '@models/educator.model';
import { ComidaService } from '@services/comida/comida.service';
import { ChildService } from '@services/child/child.service';
import { EducatorService } from '@services/educator/educator.service';
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import { NombreCompletoPipe } from '@shared/pipes/nombre-completo.pipe';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-comida-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CalendarModule,
    DropdownModule,
    ButtonModule,
    NombreCompletoPipe,
    TranslateModule
  ],
  templateUrl: './comida-form.component.html',
  styleUrls: ['./comida-form.component.scss'],
  providers: [MessageService]
})
export class ComidaFormComponent implements OnInit, OnChanges {
  @Input() comidaEditando?: Comida;
  @Output() comidaGuardada = new EventEmitter<void>();
  @Output() formularioCerrado = new EventEmitter<void>();

  form!: FormGroup;
  ninos: Child[] = [];
  educadores: Educator[] = [];
  isEditing = false;

  constructor(
    private fb: FormBuilder,
    private comidaService: ComidaService,
    private childService: ChildService,
    private educatorService: EducatorService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      nino: [null, Validators.required],
      educador: [null, Validators.required],
      horaComida: [null, Validators.required],
      descripcionComida: ['', Validators.required],
      observaciones: ['']
    });

    this.loadNinos();
    this.loadEducadores();

    if (this.comidaEditando) {
      this.isEditing = true;
      this.form.patchValue({
        nino: this.comidaEditando.nino,
        educador: this.comidaEditando.educador,
        horaComida: new Date(this.comidaEditando.horaComida),
        descripcionComida: this.comidaEditando.descripcionComida,
        observaciones: this.comidaEditando.observaciones || ''
      });
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['comidaEditando']) {
      if (this.comidaEditando) {
        this.isEditing = true;
        this.form.patchValue({
          nino: this.comidaEditando.nino,
          educador: this.comidaEditando.educador,
          horaComida: new Date(this.comidaEditando.horaComida),
          descripcionComida: this.comidaEditando.descripcionComida,
          observaciones: this.comidaEditando.observaciones || ''
        });
      } else {
        this.isEditing = false;
        this.form.reset({
          nino: null,
          educador: null,
          horaComida: null,
          descripcionComida: '',
          observaciones: ''
        });
        this.form.markAsPristine();
        this.form.markAsUntouched();
      }
    }
  }

  loadNinos() {
    this.childService.getAllChildren().subscribe(ninos => {
      this.ninos = ninos.map(n => ({
        ...n,
        nombreCompleto: `${n.nombre} ${n.apellidos}`
      }));
    });
  }

  loadEducadores() {
    this.educatorService.getEducators().subscribe(educadores => {
      this.educadores = educadores.map(e => ({
        ...e,
        nombreCompleto: `${e.nombre} ${e.apellidos}`
      }));
    });
  }

  ajustarZonaHorariaLocal(date: Date): Date {
    const offset = date.getTimezoneOffset();
    return new Date(date.getTime() - offset * 60000);
  }

  onSubmit(): void {
    if (this.form.invalid) return;

    const formData = { ...this.form.value };
    formData.horaComida = this.ajustarZonaHorariaLocal(formData.horaComida);

    if (this.isEditing && this.comidaEditando) {
      this.comidaService.update(this.comidaEditando.id!, formData).subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: 'Actualizada', detail: 'Comida actualizada correctamente' });
          this.formularioCerrado.emit();
          this.comidaGuardada.emit();
        },
        error: (err) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error al actualizar comida',
            detail: err?.error?.message || 'Ocurrió un error inesperado',
            life: 5000
          });
        }
      });
    } else {
      this.comidaService.create(formData).subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: 'Registrada', detail: 'Comida creada correctamente' });
          this.formularioCerrado.emit();
          this.comidaGuardada.emit();
        },
        error: (err) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error al registrar comida',
            detail: err?.error?.message || 'Ocurrió un error inesperado',
            life: 5000
          });
        }
      });
    }
  }

  cancelar(): void {
    this.formularioCerrado.emit();
  }
}