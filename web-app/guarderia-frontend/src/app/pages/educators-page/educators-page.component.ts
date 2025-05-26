import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Educator } from '@models/educator.model';
import { EducatorService } from '@services/educator/educator.service';
import { AuthService } from '@services/auth/auth.service';
import { EducatorTableComponent } from '@components/tables/educator-table/educator-table.component';
import { EducatorFormComponent } from '@components/forms/educator-form/educator-form.component';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-educators-page',
  standalone: true,
  imports: [
    CommonModule,
    EducatorTableComponent,
    EducatorFormComponent,
    DialogModule,
    ButtonModule,
    TranslateModule
  ],
  templateUrl: './educators-page.component.html',
  styleUrl: './educators-page.component.scss'
})
export class EducatorsPageComponent implements OnInit {
  globalFilter: string = '';
  educators: Educator[] = [];
  mostrarDialogoEducador = false;
  selectedEducator?: Educator;
  educatorEditando = false;

  constructor(
    private educatorService: EducatorService,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    this.cargarEducadores();
  }

  cargarEducadores(): void {
    this.educatorService.getEducators().subscribe({
      next: (data) => (this.educators = data),
      error: (err) => console.error('Error al cargar educadores:', err)
    });
  }

  guardarEducador(educator: Educator): void {
    console.log('Datos enviados al backend:', educator);
    if (this.educatorEditando) {
      this.educatorService.updateEducator(educator).subscribe({
        next: () => {
          this.cargarEducadores();
          this.cancelarDialogoEducador();
        },
        error: (err: any) => console.error('Error al actualizar educador:', err)
      });
    } else {
      this.educatorService.createEducator(educator).subscribe({
        next: (nuevoEducador) => {
          this.educators.push(nuevoEducador);
          this.cancelarDialogoEducador();
        },
        error: (err: any) => console.error('Error al guardar educador:', err)
      });
    }
  }

  editarEducador(educator: Educator): void {
    this.selectedEducator = educator;
    this.mostrarDialogoEducador = true;
    this.educatorEditando = true;
  }

  eliminarEducador(id: number): void {
    this.educatorService.deleteEducator(id).subscribe({
      next: () => this.cargarEducadores(),
      error: (err) => console.error('Error al eliminar educador:', err)
    });
  }

  abrirDialogoParaNuevoEducador(): void {
    this.selectedEducator = undefined;
    this.mostrarDialogoEducador = true;
    this.educatorEditando = false;
  }

  cancelarDialogoEducador(): void {
    this.mostrarDialogoEducador = false;
    this.selectedEducator = undefined;
    this.educatorEditando = false;
  }
}
