import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { Parent } from '@models/parent.model';


@Component({
  selector: 'app-parent-table',
  standalone: true,
  imports: [CommonModule, TableModule, ButtonModule],
  templateUrl: './parent-table.component.html',
  styleUrls: ['./parent-table.component.scss']
})
export class ParentTableComponent {
  @Input() parents: Parent[] = [];
  @Input() globalFilter: string = '';
}