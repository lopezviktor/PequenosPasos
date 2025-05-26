import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';
import { Educator } from '@models/educator.model';
import { EducatorService } from '@services/educator/educator.service';
import { AuthService } from '@services/auth/auth.service';
import { KeyFilterModule } from 'primeng/keyfilter';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-educator-table',
  standalone: true,
  imports: [
    CommonModule, 
    FormsModule, 
    TableModule, 
    ButtonModule, 
    ConfirmDialogModule, 
    KeyFilterModule,
    TranslateModule
  ],
  templateUrl: './educator-table.component.html',
  styleUrls: ['./educator-table.component.scss']
})
export class EducatorTableComponent {
  @Input() educators: Educator[] = [];
  nombreFiltro: string = '';
  apellidosFiltro: string = '';
  emailFiltro: string = '';
  telefonoFiltro: string = '';

  @Output() editar = new EventEmitter<Educator>();
  @Output() eliminado = new EventEmitter<number>();

  get educatorsFiltrados(): Educator[] {
    return this.educators.filter(e =>
      e.nombre.toLowerCase().includes(this.nombreFiltro.toLowerCase()) &&
      e.apellidos.toLowerCase().includes(this.apellidosFiltro.toLowerCase()) &&
      e.email.toLowerCase().includes(this.emailFiltro.toLowerCase()) &&
      e.telefono.toString().toLowerCase().includes(this.telefonoFiltro.toLowerCase())
    );
  }

  constructor(
    private confirmationService: ConfirmationService,
    private educatorService: EducatorService, 
    public authService: AuthService
  ) {}

  editarEducador(educador: Educator) {
    this.editar.emit(educador);
  }

  confirmarEliminar(educador: Educator) {
    this.confirmationService.confirm({
      message: `¿Seguro que deseas eliminar a ${educador.nombre}?`,
      header: 'Confirmar eliminación',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.educatorService.deleteEducator(educador.id).subscribe(() => {
          this.eliminado.emit(educador.id);
        });
      }
    });
  }

  limpiarFiltros() {
    this.nombreFiltro = '';
    this.apellidosFiltro = '';
    this.emailFiltro = '';
    this.telefonoFiltro = '';
  }
}
