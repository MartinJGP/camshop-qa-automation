import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { HeroComponent } from './hero.component';

describe('HeroComponent', () => {
  let component: HeroComponent;
  let fixture: ComponentFixture<HeroComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeroComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(HeroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('debe crearse exitosamente', () => {
    expect(component).toBeTruthy();
  });

  it('debe inicializarse con la primera cámara seleccionada (CAM-01)', () => {
    expect(component.selectedCamera().id).toBe('CAM-01');
  });

  it('debe permitir cambiar de cámara en el simulador CCTV', () => {
    const cam02 = component.cameras[1];
    component.selectCamera(cam02);
    expect(component.selectedCamera().id).toBe('CAM-02');
  });

  it('debe mostrar el reloj digital en tiempo real', () => {
    expect(component.currentTimestamp()).toBeTruthy();
    expect(component.currentTimestamp().length).toBeGreaterThan(0);
  });
});
