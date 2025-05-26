import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChildService } from '@services/child/child.service';
import { Child } from '@models/child.model';
import { ChildTableComponent } from '@components/tables/child-table/child-table.component';
import { ChildFormComponent } from '@components/forms/child-form/child-form.component';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-children-page',
  standalone: true,
  imports: [
    CommonModule,
    ChildTableComponent,
    ChildFormComponent,
    DialogModule,
    ButtonModule,
    TranslateModule
  ],
  templateUrl: './children-page.component.html',
  styleUrl: './children-page.component.scss'
})
export class ChildrenPageComponent implements OnInit {
  globalFilter: string = '';
  mostrarDialogoNino = false;
  selectedChild?: Child;
  childEditando = false;
  children: Child[] = [];

  constructor(private childService: ChildService) {}

  ngOnInit(): void {
    this.cargarNinos();
  }

  cargarNinos(): void {
    this.childService.getAllChildren().subscribe({
      next: (data) => (this.children = data),
      error: (err) => console.error('Error al cargar niños:', err)
    });
  }

  guardarNino(nino: Child): void {
    console.log('Datos enviados al backend:', nino);
    if (this.childEditando) {
      this.childService.updateChild(nino).subscribe({
        next: () => {
          this.cargarNinos();
          this.cancelarDialogoNino();
        },
        error: (err: any) => console.error('Error al actualizar niño:', err)
      });
    } else {
      this.childService.createChild(nino).subscribe({
        next: (nuevoNino) => {
          this.children.push(nuevoNino);
          this.cancelarDialogoNino();
        },
        error: (err: any) => console.error('Error al guardar niño:', err)
      });
    }
  }

  editarNino(nino: Child): void {
    this.selectedChild = nino;
    this.mostrarDialogoNino = true;
    this.childEditando = true;
  }

  eliminarNino(id: number): void {
    this.childService.deleteChild(id).subscribe({
      next: () => this.cargarNinos(),
      error: (err) => console.error('Error al eliminar niño:', err)
    });
  }

  abrirDialogoParaNuevoNino(): void {
    this.selectedChild = undefined;
    this.mostrarDialogoNino = true;
    this.childEditando = false;
  }

  cancelarDialogoNino(): void {
    this.mostrarDialogoNino = false;
    this.selectedChild = undefined;
    this.childEditando = false;
  }
}