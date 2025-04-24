import { Component, Input } from '@angular/core';
import { Clase } from '@models/clase.model';
import { CardModule } from 'primeng/card';

@Component({
  selector: 'app-class-card',
  imports: [CardModule],
  templateUrl: './class-card.component.html',
  styleUrl: './class-card.component.scss'
})
export class ClassCardComponent {
  @Input() clase!: Clase;
}
