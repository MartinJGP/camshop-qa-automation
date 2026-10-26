import { Component, OnDestroy, OnInit, inject, signal } from "@angular/core";
import { RouterLink } from "@angular/router";
import { CommonModule } from "@angular/common";
import { AuthService } from "../../../../services/auth.service";

export interface CameraFeed {
  id: string;
  name: string;
  location: string;
  resolution: string;
  fps: number;
  signalStrength: number;
  alert: string;
  mode: "normal" | "motion" | "night";
}

@Component({
  selector: "app-landing-hero",
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: "./hero.component.html"
})
export class HeroComponent implements OnInit, OnDestroy {
  auth = inject(AuthService);

  cameras: CameraFeed[] = [
    {
      id: "CAM-01",
      name: "Entrada Principal",
      location: "Acceso Peatonal",
      resolution: "4K UHD",
      fps: 30,
      signalStrength: 99,
      alert: "Monitoreo Normal",
      mode: "normal"
    },
    {
      id: "CAM-02",
      name: "Perímetro Exterior",
      location: "Fachada Norte",
      resolution: "4K Ultra",
      fps: 60,
      signalStrength: 96,
      alert: "Sensor Infrarrojo Activo",
      mode: "night"
    },
    {
      id: "CAM-03",
      name: "Almacén Central",
      location: "Área de Stock",
      resolution: "2K QHD",
      fps: 30,
      signalStrength: 98,
      alert: "Detección Inteligente",
      mode: "motion"
    }
  ];

  selectedCamera = signal<CameraFeed>(this.cameras[0]);
  currentTimestamp = signal<string>("");
  private timerId: any = null;

  ngOnInit(): void {
    this.updateClock();
    this.timerId = setInterval(() => this.updateClock(), 1000);
  }

  ngOnDestroy(): void {
    if (this.timerId) {
      clearInterval(this.timerId);
    }
  }

  selectCamera(cam: CameraFeed): void {
    this.selectedCamera.set(cam);
  }

  private updateClock(): void {
    const now = new Date();
    const timeStr = now.toLocaleTimeString("es-PE", { hour12: false });
    this.currentTimestamp.set(timeStr);
  }
}
