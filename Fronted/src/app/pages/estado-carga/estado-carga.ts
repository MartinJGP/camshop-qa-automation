import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-estado-carga',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './estado-carga.html',
  styleUrl: './estado-carga.css'
})
export class EstadoCarga {
  @Input() mensaje: string = 'Cargando información...';
  @Input() subtitulo: string = 'Por favor espera un momento';
}
