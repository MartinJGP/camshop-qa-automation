import { Component } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterLink } from "@angular/router";

export interface PasoProceso {
  paso: string;
  titulo: string;
  descripcion: string;
  icono: string;
}

@Component({
  selector: "app-landing-proceso",
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: "./proceso.component.html"
})
export class ProcesoComponent {
  pasos: PasoProceso[] = [
    {
      paso: "01",
      titulo: "Elige tu equipo ideal",
      descripcion: "Explora marcas líderes, compara resoluciones 4K/1080p y valida existencias en tiempo real.",
      icono: "search"
    },
    {
      paso: "02",
      titulo: "Compra con pago simulado",
      descripcion: "Añade al carrito, ingresa tu dirección de entrega y valida el checkout sin cargos bancarios reales.",
      icono: "credit-card"
    },
    {
      paso: "03",
      titulo: "Monitorea tu pedido",
      descripcion: "Sigue la evolución de tu entrega en Mis Pedidos y comparte tu reseña cuando lo recibas.",
      icono: "truck"
    }
  ];
}

