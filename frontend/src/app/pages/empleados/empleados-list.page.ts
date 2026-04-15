import { CommonModule } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { resolvePermissionsByRole } from '../../services/auth/role-permissions';
import {
  EmpleadoView,
  PaginatedEmpleadosResponse,
} from '../../services/empleados/empleados.models';
import { EmpleadosService } from '../../services/empleados/empleados.service';

@Component({
  selector: 'app-empleados-list-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './empleados-list.page.html',
  styleUrl: './empleados-list.page.css',
})
export class EmpleadosListPage implements OnInit {
  private readonly empleadosService = inject(EmpleadosService);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  readonly rol = this.authService.role;
  readonly permissions = computed(() => resolvePermissionsByRole(this.rol()));

  readonly rows = signal<EmpleadoView[]>([]);
  readonly page = signal(0);
  readonly totalPages = signal(0);
  readonly totalElements = signal(0);
  readonly loading = signal(false);
  readonly message = signal('');

  ngOnInit(): void {
    if (this.route.snapshot.queryParamMap.get('denied') === '1') {
      this.message.set('No tienes permisos para esta acción.');
    }
    this.load(0);
  }

  load(page: number): void {
    this.loading.set(true);
    this.empleadosService.listar(page).subscribe({
      next: (response: PaginatedEmpleadosResponse) => {
        this.rows.set(response.content);
        this.page.set(response.page);
        this.totalPages.set(response.totalPages);
        this.totalElements.set(response.totalElements);
        if (!response.content.length && response.totalElements === 0) {
          this.message.set('No hay empleados para mostrar.');
        }
        this.loading.set(false);
      },
      error: (error) => {
        this.message.set(
          this.empleadosService.resolveActionMessage(
            error,
            'No se pudo cargar el listado de empleados.',
          ),
        );
        this.loading.set(false);
      },
    });
  }

  irANuevo(): void {
    if (!this.permissions().puedeCrear) {
      this.message.set('No tienes permisos para esta acción.');
      return;
    }
    void this.router.navigateByUrl('/empleados/nuevo');
  }

  irAEditar(clave: string): void {
    if (!this.permissions().puedeEditar) {
      this.message.set('No tienes permisos para esta acción.');
      return;
    }
    void this.router.navigateByUrl(`/empleados/${clave}/editar`);
  }

  cerrarSesion(): void {
    this.authService.logout();
    void this.router.navigateByUrl('/login');
  }

  eliminar(empleado: EmpleadoView): void {
    if (!this.permissions().puedeEliminar) {
      this.message.set('No tienes permisos para esta acción.');
      return;
    }

    const confirmDelete = window.confirm(`¿Eliminar a ${empleado.nombre}?`);
    if (!confirmDelete) {
      return;
    }

    this.empleadosService.eliminar(empleado.clave).subscribe({
      next: () => {
        this.message.set('Empleado eliminado correctamente.');
        this.load(this.page());
      },
      error: (error) => {
        this.message.set(
          this.empleadosService.resolveActionMessage(error, 'No se pudo eliminar el empleado.'),
        );
      },
    });
  }

  anterior(): void {
    if (this.page() <= 0) {
      return;
    }
    this.load(this.page() - 1);
  }

  siguiente(): void {
    if (this.page() >= Math.max(this.totalPages() - 1, 0)) {
      return;
    }
    this.load(this.page() + 1);
  }
}
