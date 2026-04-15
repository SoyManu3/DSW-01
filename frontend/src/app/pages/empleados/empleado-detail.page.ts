import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { EmpleadoView } from '../../services/empleados/empleados.models';
import { EmpleadosService } from '../../services/empleados/empleados.service';

@Component({
  selector: 'app-empleado-detail-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './empleado-detail.page.html',
  styleUrl: './empleado-form.page.css',
})
export class EmpleadoDetailPage implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly empleadosService = inject(EmpleadosService);

  readonly empleado = signal<EmpleadoView | null>(null);
  readonly loading = signal(false);
  readonly message = signal('');

  ngOnInit(): void {
    const clave = this.route.snapshot.paramMap.get('clave');
    if (!clave) {
      this.message.set('No se encontró la clave del empleado.');
      return;
    }

    this.loading.set(true);
    this.empleadosService.obtenerPorClave(clave).subscribe({
      next: (value) => {
        this.empleado.set(value);
        this.loading.set(false);
      },
      error: (error) => {
        this.message.set(
          this.empleadosService.resolveActionMessage(
            error,
            'No se pudo cargar el detalle del empleado.',
          ),
        );
        this.loading.set(false);
      },
    });
  }
}
