import { Component, signal } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterLink } from "@angular/router";

export interface SolucionItem {
  id: string;
  numero: string;
  titulo: string;
  subtitulo: string;
  descripcion: string;
  caracteristicas: string[];
  icono: string;
  recomendadoPara: string;
}

@Component({
  selector: "app-landing-soluciones",
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: "./soluciones.component.html"
})
export class SolucionesComponent {
  soluciones: SolucionItem[] = [
    {
      id: "interior",
      numero: "01",
      titulo: "Hogar e Interior",
      subtitulo: "Discreción y confort diario",
      descripcion: "Supervisa habitaciones, mascotas, oficinas y accesos familiares con audio bidireccional y ángulo panorámico.",
      caracteristicas: [
        "Audio bidireccional en vivo",
        "Detección de llanto o ruidos",
        "Modo de privacidad programable",
        "Instalación rápida plug & play"
      ],
      icono: "home",
      recomendadoPara: "Departamentos, salas y oficinas"
    },
    {
      id: "exterior",
      numero: "02",
      titulo: "Fachadas y Exterior",
      subtitulo: "Protección perimetral blindada",
      descripcion: "Resistencia certificada IP66/IP67 ante lluvia y temperaturas extremas con visión nocturna infrarroja de largo alcance.",
      caracteristicas: [
        "Chasis metálico antivandálico IK10",
        "Visión nocturna hasta 35 metros",
        "Luz disuasiva estroboscópica",
        "Sirena de alerta integrada"
      ],
      icono: "shield",
      recomendadoPara: "Patios, entradas y cocheras"
    },
    {
      id: "ptz",
      numero: "03",
      titulo: "PTZ & Negocios",
      subtitulo: "Monitoreo 360° de alta escala",
      descripcion: "Control total con paneo horizontal, inclinación vertical y zoom óptico inteligente para comercios y almacenes.",
      caracteristicas: [
        "Rotación continua 360° sin puntos ciegos",
        "Auto-tracking de personas y autos",
        "Zoom óptico de alta resolución",
        "Grabación local y respaldo NVR"
      ],
      icono: "camera",
      recomendadoPara: "Tiendas, almacenes y fábricas"
    }
  ];

  filtroActivo = signal<string>("todos");

  filtrar(id: string): void {
    this.filtroActivo.set(id);
  }

  get solucionesFiltradas(): SolucionItem[] {
    const f = this.filtroActivo();
    if (f === "todos") {
      return this.soluciones;
    }
    return this.soluciones.filter(s => s.id === f);
  }
}

