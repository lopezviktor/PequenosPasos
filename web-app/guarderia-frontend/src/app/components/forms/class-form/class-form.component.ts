import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Clase } from '@models/clase.model';
import { Educator } from '@models/educator.model';
import { DropdownModule } from 'primeng/dropdown';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-class-form',
  standalone: true,
  imports: [ReactiveFormsModule, DropdownModule, TranslateModule],
  templateUrl: './class-form.component.html',
  styleUrl: './class-form.component.scss'
})
export class ClassFormComponent {
  @Input() clase: Clase | null = null;
  @Input() educadores: Educator[] = [];
  @Output() submitClase = new EventEmitter<Clase>();
  @Output() cancelar = new EventEmitter<void>();

  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      nombre: ['', Validators.required],
      educador: [null, Validators.required]
    });
  }

  ngOnChanges() {
    if (this.clase) {
      this.form.patchValue({
        nombre: this.clase.nombre,
        educador: this.clase.educador || null
      });
    } else {
      this.form.reset();
    }
  }

  onSubmit() {
    if (this.form.valid) {
      const claseActualizada: Clase = {
        ...this.clase,
        ...this.form.value
      };
      this.submitClase.emit(claseActualizada);
    }
  }

  onCancel() {
    this.cancelar.emit();
  }
}
