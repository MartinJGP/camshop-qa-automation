import { Routes } from '@angular/router';
import { Inicio } from './components/inicio/inicio';
import { Camaras } from './components/camaras/camaras';
import { DetalleCamara } from './components/detalle-camara/detalle-camara';
import { LoginComponent } from './components/login/login';
import { RegistroComponent } from './components/registro/registro';
import { Carrito } from './components/carrito/carrito';
import { Pagos } from './components/pagos/pagos';
import { Resenas } from './components/resenas/resenas';
import { Historial } from './components/historial/historial';
import { Admin } from './components/admin/admin';
import { Perfil } from './components/perfil/perfil';
import { authGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin.guard';

export const routes: Routes = [
  { path: '', component: Inicio },
  { path: 'camaras', component: Camaras },
  { path: 'camaras/:id', component: DetalleCamara },
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroComponent },
  { path: 'carrito', component: Carrito, canActivate: [authGuard] },
  { path: 'historial', component: Historial, canActivate: [authGuard] },
  { path: 'ordenes', redirectTo: 'historial', pathMatch: 'full' },
  { path: 'admin', component: Admin, canActivate: [adminGuard] },
  { path: 'perfil', component: Perfil, canActivate: [authGuard] },
  { path: 'pagos', component: Pagos, canActivate: [adminGuard] },
  { path: 'resenas', component: Resenas },
  { path: '**', redirectTo: '' }
];
