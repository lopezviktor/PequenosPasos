import { CommonModule } from '@angular/common';
import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
import { Menu } from 'primeng/menu';

@Component({
  selector: 'app-floating-action-button',
  imports: [CommonModule, ButtonModule, MenuModule],
  templateUrl: './floating-action-button.component.html',
  styleUrl: './floating-action-button.component.scss'
})
export class FloatingActionButtonComponent implements AfterViewInit {
  @ViewChild('menu') menu!: Menu;

  actions: MenuItem[] = [
    { label: 'Registrar comida', icon: 'pi pi-utensils', command: () => console.log('Comida') },
    { label: 'Registrar siesta', icon: 'pi pi-moon', command: () => console.log('Siesta') },
    { label: 'Registrar higiene', icon: 'pi pi-shield', command: () => console.log('Higiene') }
  ];
  
  onClick(event: MouseEvent) {
    setTimeout(() => {
      if (this.menu) {
        this.menu.toggle(event);
      }
    }, 0);
  }

  ngAfterViewInit(): void {
    console.log('Menu ViewChild:', this.menu);
  }
}
