import { Component, OnInit, inject } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { NotificationService } from '../../services/notification.service';
import { HeroComponent } from './components/hero/hero.component';
import { BeneficiosComponent } from './components/beneficios/beneficios.component';
import { SolucionesComponent } from './components/soluciones/soluciones.component';
import { CamarasDestacadasComponent } from './components/camaras-destacadas/camaras-destacadas.component';
import { ProcesoComponent } from './components/proceso/proceso.component';
import { CtaBannerComponent } from './components/cta-banner/cta-banner.component';

@Component({
  selector: 'app-inicio',
  standalone: true,
  imports: [
    HeroComponent,
    BeneficiosComponent,
    SolucionesComponent,
    CamarasDestacadasComponent,
    ProcesoComponent,
    CtaBannerComponent
  ],
  templateUrl: './inicio.html',
  styleUrl: './inicio.css'
})
export class Inicio implements OnInit {
  auth = inject(AuthService);
  private notification = inject(NotificationService);

  ngOnInit(): void {
    const mensaje = localStorage.getItem('loginSuccessMessage');
    if (mensaje) {
      localStorage.removeItem('loginSuccessMessage');
      setTimeout(() => {
        this.notification.exito('Sesión iniciada', 'Bienvenido de vuelta a CamShop.');
      }, 100);
    }
  }
}
