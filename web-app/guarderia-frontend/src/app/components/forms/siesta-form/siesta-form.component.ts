import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import { Siesta } from '@models/siesta.model';
import { Child } from '@models/child.model';
import { Educator } from '@models/educator.model';
import { SiestaService } from '@services/siesta/siesta-service.service';
import { ChildService } from '@services/child/child.service';
import { EducatorService } from '@services/educator/educator.service';
import { NombreCompletoPipe } from '@shared/pipes/nombre-completo.pipe';

@Component({
  selector: 'app-siesta-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CalendarModule,
    DropdownModule,
    ButtonModule,
    NombreCompletoPipe
   ],
  templateUrl: './siesta-form.component.html',
  styleUrl: './siesta-form.component.scss',
  providers: [MessageService]
})
export class SiestaFormComponent implements OnInit {
  @Input() siestaEditando?: Siesta;
  @Output() formularioCerrado = new EventEmitter<void>();
  @Output() siestaGuardada = new EventEmitter<void>();

  form!: FormGroup;
  ninos: Child[] = [];
  educadores: Educator[] = [];
  isEditing = false;

  constructor(
    private fb: FormBuilder,
    private siestaService: SiestaService,
    private ninoService: ChildService,
    private educadorService: EducatorService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      nino: [null, Validators.required],
      educador: [null, Validators.required],
      inicioSiesta: [null, Validators.required],
      finSiesta: [null],
      observaciones: ['']
    });

    this.loadNinos();
    this.loadEducadores();

    if (this.siestaEditando) {
      this.isEditing = true;
      this.form.patchValue({
        nino: this.siestaEditando.nino,
        educador: this.siestaEditando.educador,
        inicioSiesta: new Date(this.siestaEditando.inicioSiesta),
        finSiesta: this.siestaEditando.finSiesta ? new Date(this.siestaEditando.finSiesta) : null,
        observaciones: this.siestaEditando.observaciones
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
    formData.inicioSiesta = this.ajustarZonaHorariaLocal(formData.inicioSiesta);
    if (formData.finSiesta) {
      formData.finSiesta = this.ajustarZonaHorariaLocal(formData.finSiesta);
    }

    if (this.isEditing && this.siestaEditando) {
      this.siestaService.update(this.siestaEditando.id!, formData).subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: 'Siesta actualizada' });
          this.formularioCerrado.emit();
          this.siestaGuardada.emit();
        },
        error: err => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error al actualizar siesta',
            detail: err?.error?.message || 'Ocurrió un error inesperado',
            life: 5000
          });
        }
      });
    } else {
      this.siestaService.create(formData).subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: 'Siesta registrada' });
          this.formularioCerrado.emit();
          this.siestaGuardada.emit();
        },
        error: err => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error al registrar siesta',
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
