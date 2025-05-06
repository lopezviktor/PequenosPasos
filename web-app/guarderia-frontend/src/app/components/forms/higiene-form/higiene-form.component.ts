import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import { Higiene } from '@models/higiene.model';
import { Child } from '@models/child.model';
import { Educator } from '@models/educator.model';
import { HigieneService } from '@services/higiene/higiene.service';
import { ChildService } from '@services/child/child.service';
import { EducatorService } from '@services/educator/educator.service';

@Component({
  selector: 'app-higiene-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CalendarModule,
    DropdownModule,
    ButtonModule
  ],
  templateUrl: './higiene-form.component.html',
  styleUrl: './higiene-form.component.scss',
  providers: [MessageService]
})
export class HigieneFormComponent implements OnInit {
  @Input() higieneEditando?: Higiene;
  @Output() formularioCerrado = new EventEmitter<void>();
  @Output() higieneGuardada = new EventEmitter<void>();

  form!: FormGroup;
  ninos: Child[] = [];
  educadores: Educator[] = [];
  isEditing = false;

  estados = [
    { label: 'Normal', value: 'NORMAL' },
    { label: 'Estreñido', value: 'ESTREÑIDO' },
    { label: 'Suelto', value: 'SUELTO' }
  ];

  constructor(
    private fb: FormBuilder,
    private higieneService: HigieneService,
    private ninoService: ChildService,
    private educadorService: EducatorService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      nino: [null, Validators.required],
      educador: [null, Validators.required],
      fechaHora: [null, Validators.required],
      estado: [null, Validators.required],
      observaciones: ['']
    });

    this.loadNinos();
    this.loadEducadores();

    if (this.higieneEditando) {
      this.isEditing = true;
      this.form.patchValue({
        nino: this.higieneEditando.nino,
        educador: this.higieneEditando.educador,
        fechaHora: new Date(this.higieneEditando.fechaHora),
        estado: this.higieneEditando.estado,
        observaciones: this.higieneEditando.observaciones
      });
    }
  }

  loadNinos() {
    this.ninoService.getAllChildren().subscribe(ninos => {
      this.ninos = ninos;
    });
  }

  loadEducadores() {
    this.educadorService.getEducators().subscribe(educadores => {
      this.educadores = educadores;
    });
  }

  ajustarZonaHorariaLocal(date: Date): Date {
    const offset = date.getTimezoneOffset();
    return new Date(date.getTime() - offset * 60 * 1000);
  }

  onSubmit() {
    const formData = { ...this.form.value };
    formData.fechaHora = this.ajustarZonaHorariaLocal(formData.fechaHora);
    formData.estado = formData.estado.value;

    if (this.isEditing && this.higieneEditando) {
      this.higieneService.update(this.higieneEditando.id!, formData).subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: 'Higiene actualizada' });
          this.formularioCerrado.emit();
          this.higieneGuardada.emit();
        },
        error: err => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error al actualizar higiene',
            detail: err?.error?.message || 'Ocurrió un error inesperado',
            life: 5000
          });
        }
      });
    } else {
      console.log('Datos enviados al crear higiene:', formData);
      this.higieneService.create(formData).subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: 'Higiene registrada' });
          this.formularioCerrado.emit();
          this.higieneGuardada.emit();
        },
        error: err => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error al registrar higiene',
            detail: err?.error?.message || 'Ocurrió un error inesperado',
            life: 5000
          });
        }
      });
    }
  }

  cancelar() {
    this.formularioCerrado.emit();
  }
}
