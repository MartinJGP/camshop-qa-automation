import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { SolucionesComponent } from './soluciones.component';

describe('SolucionesComponent', () => {
  let component: SolucionesComponent;
  let fixture: ComponentFixture<SolucionesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SolucionesComponent],
      providers: [provideRouter([])]
    }).compileComponents();

    fixture = TestBed.createComponent(SolucionesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('debe crearse exitosamente', () => {
    expect(component).toBeTruthy();
  });

  it('debe mostrar todas las soluciones cuando el filtro es "todos"', () => {
    expect(component.solucionesFiltradas.length).toBe(3);
  });

  it('debe filtrar correctamente por categoría interior', () => {
    component.filtrar('interior');
    expect(component.solucionesFiltradas.length).toBe(1);
    expect(component.solucionesFiltradas[0].id).toBe('interior');
  });
});
