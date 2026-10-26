import { Component, OnInit, inject, signal } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterLink } from "@angular/router";
import { CamaraService } from "../../../../services/camara.service";
import { CarritoService } from "../../../../services/carrito.service";
import { AuthService } from "../../../../services/auth.service";
import { NotificationService } from "../../../../services/notification.service";
import { Camara } from "../../../../models/camara";

@Component({
  selector: "app-landing-camaras-destacadas",
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: "./camaras-destacadas.component.html"
})
export class CamarasDestacadasComponent implements OnInit {
  private camaraService = inject(CamaraService);
  private carritoService = inject(CarritoService);
  private auth = inject(AuthService);
  private notification = inject(NotificationService);

  camaras = signal<Camara[]>([]);
  cargando = signal<boolean>(true);
  agregandoId = signal<number | null>(null);

  placeholder = "https://placehold.co/600x400/111a31/e94560?text=CamShop+Security";

  ngOnInit(): void {
    this.cargarDestacadas();
  }

  cargarDestacadas(): void {
    this.cargando.set(true);
    this.camaraService.obtenerTodos({ soloStock: true }).subscribe({
      next: (data) => {
        this.camaras.set(data.slice(0, 4));
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
      }
    });
  }

  agregarAlCarrito(camara: Camara, event: Event): void {
    event.stopPropagation();
    event.preventDefault();

    if (!this.auth.isLogged()) {
      this.notification.info("Inicia sesión", "Debes ingresar con tu cuenta para agregar productos al carrito.");
      return;
    }

    this.agregandoId.set(camara.id);
    this.carritoService.agregar(camara.id, 1).subscribe({
      next: () => {
        this.agregandoId.set(null);
        this.notification.exito("Agregado al carrito", `${camara.nombre} se añadió con éxito.`);
      },
      error: (err) => {
        this.agregandoId.set(null);
        this.notification.error("Error", err?.error?.message || "No se pudo agregar el producto.");
      }
    });
  }
}
