import { Component } from "@angular/core";
import { CommonModule } from "@angular/common";

export interface BeneficioItem {
  valor: string;
  titulo: string;
  descripcion: string;
  badge: string;
}

@Component({
  selector: "app-landing-beneficios",
  standalone: true,
  imports: [CommonModule],
  templateUrl: "./beneficios.component.html"
})
export class BeneficiosComponent {
  beneficios: BeneficioItem[] = [
    {
      valor: "24/7",
      titulo: "Vigilancia Continua",
      descripcion: "Monitoreo ininterrumpido día y noche con iluminación infrarroja adaptativa.",
      badge: "Siempre Activo"
    },
    {
      valor: "4K UHD",
      titulo: "Máxima Nitidez",
      descripcion: "Sensores CMOS de última generación para detalles precisos y zoom digital nítido.",
      badge: "Ultra HD"
    },
    {
      valor: "IP67",
      titulo: "Resistencia Extrema",
      descripcion: "Blindaje sellado contra lluvias torrenciales, polvo y climas severos.",
      badge: "Exterior"
    },
    {
      valor: "100%",
      titulo: "Compra Trazable",
      descripcion: "Historial de pedidos en tiempo real desde la orden hasta la entrega final.",
      badge: "Garantizado"
    }
  ];
}

