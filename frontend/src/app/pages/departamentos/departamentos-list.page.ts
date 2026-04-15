import { CommonModule } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { resolvePermissionsByRole } from '../../services/auth/role-permissions';
import {
  DepartamentoView,
  PaginatedDepartamentosResponse,
} from '../../services/departamentos/departamentos.models';
import { DepartamentosService } from '../../services/departamentos/departamentos.service';

@Component({
  selector: 'app-departamentos-list-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './departamentos-list.page.html',
  styleUrl: './departamentos-list.page.css',
})
export class DepartamentosListPage implements OnInit {
  private readonly departamentosService = inject(DepartamentosService);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  readonly rol = this.authService.role;
  readonly permissions = computed(() => resolvePermissionsByRole(this.rol()));

  readonly rows = signal<DepartamentoView[]>([]);
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
    this.departamentosService.listar(page).subscribe({
      next: (response: PaginatedDepartamentosResponse) => {
        this.rows.set(response.content);
        this.page.set(response.page);
        this.totalPages.set(response.totalPages);
        this.totalElements.set(response.totalElements);
        if (!response.content.length && response.totalElements === 0) {
          this.message.set('No hay departamentos para mostrar.');
        }
        this.loading.set(false);
      },
      error: (error) => {
        const actionMessage = this.departamentosService.resolveActionMessage(
          error,
          'No se pudo cargar el listado de departamentos.',
        );
        if (actionMessage === 'Sesión expirada o no autenticada.') {
          this.authService.logout();
          void this.router.navigate(['/login'], { queryParams: { reason: 'session-expired' } });
          return;
        }
        this.message.set(actionMessage);
        this.loading.set(false);
      },
    });
  }

  irANuevo(): void {
    if (!this.permissions().puedeCrear) {
      this.message.set('No tienes permisos para esta acción.');
      return;
    }
    void this.router.navigateByUrl('/departamentos/nuevo');
  }

  irAEditar(id: number): void {
    if (!this.permissions().puedeEditar) {
      this.message.set('No tienes permisos para esta acción.');
      return;
    }
    void this.router.navigateByUrl(`/departamentos/${id}/editar`);
  }

  cerrarSesion(): void {
    this.authService.logout();
    void this.router.navigateByUrl('/login');
  }

  eliminar(item: DepartamentoView): void {
    if (!this.permissions().puedeEliminar) {
      this.message.set('No tienes permisos para esta acción.');
      return;
    }

    const confirmDelete = window.confirm(`¿Eliminar departamento ${item.nombre}?`);
    if (!confirmDelete) {
      return;
    }

    this.departamentosService.eliminar(item.id).subscribe({
      next: () => {
        this.message.set('Departamento eliminado correctamente.');
        this.load(this.page());
      },
      error: (error) => {
        const actionMessage = this.departamentosService.resolveActionMessage(
          error,
          'No se pudo eliminar el departamento.',
        );
        if (actionMessage === 'Sesión expirada o no autenticada.') {
          this.authService.logout();
          void this.router.navigate(['/login'], { queryParams: { reason: 'session-expired' } });
          return;
        }
        this.message.set(actionMessage);
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
