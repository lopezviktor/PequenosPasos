import { Component, OnInit } from '@angular/core';
import { Parent } from '@models/parent.model';
import { ParentService } from '@services/parent/parent.service';
import { ParentTableComponent } from '@components/tables/parent-table/parent-table.component';
import { ParentFormComponent } from '@components/forms/parent-form/parent-form.component';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-parents-page',
  standalone: true,
  imports: [
    CommonModule,
    ParentTableComponent,
    ParentFormComponent,
    DialogModule,
    ButtonModule
  ],
  templateUrl: './parents-page.component.html',
  styleUrl: './parents-page.component.scss'
})
export class ParentsPageComponent implements OnInit {
  globalFilter: string = '';
  parents: Parent[] = [];
  mostrarDialogoPadre = false;
  selectedParent?: Parent;
  parentEditando = false;

  constructor(private parentService: ParentService) {}

  ngOnInit(): void {
    this.cargarPadres();
  }

  cargarPadres(): void {
    this.parentService.getParents().subscribe({
      next: (data) => (this.parents = data),
      error: (err) => console.error('Error al cargar padres:', err)
    });
  }

  guardarPadre(parent: Parent): void {
    console.log('Datos enviados al backend:', parent);
    if (this.parentEditando) {
      this.parentService.updateParent(parent).subscribe({
        next: () => {
          this.cargarPadres();
          this.cancelarDialogoPadre();
        },
        error: (err: any) => console.error('Error al actualizar padre:', err)
      });
    } else {
      this.parentService.createParent(parent).subscribe({
        next: (nuevoPadre) => {
          this.parents.push(nuevoPadre);
          this.cancelarDialogoPadre();
        },
        error: (err: any) => console.error('Error al guardar padre:', err)
      });
    }
  }

  editarPadre(parent: Parent): void {
    this.selectedParent = parent;
    this.mostrarDialogoPadre = true;
    this.parentEditando = true;
  }
  
  eliminarPadre(id: number): void {
    this.parentService.deleteParent(id).subscribe({
      next: () => this.cargarPadres(),
      error: (err) => console.error('Error al eliminar padre:', err)
    });
  }
  
  abrirDialogoParaNuevoPadre(): void {
    this.selectedParent = undefined; 
    this.mostrarDialogoPadre = true; 
  }

  cancelarDialogoPadre(): void {
    this.mostrarDialogoPadre = false;
    this.selectedParent = undefined;
    this.parentEditando = false;
  }
  
}
