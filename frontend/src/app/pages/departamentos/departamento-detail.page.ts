import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { DepartamentoView } from '../../services/departamentos/departamentos.models';
import { DepartamentosService } from '../../services/departamentos/departamentos.service';

@Component({
  selector: 'app-departamento-detail-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './departamento-detail.page.html',
  styleUrl: './departamento-form.page.css',
})
export class DepartamentoDetailPage implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly authService = inject(AuthService);
  private readonly departamentosService = inject(DepartamentosService);

  readonly departamento = signal<DepartamentoView | null>(null);
  readonly loading = signal(false);
  readonly message = signal('');

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id') ?? 0);
    if (!id) {
      this.message.set('No se encontró el identificador del departamento.');
      return;
    }

    this.loading.set(true);
    this.departamentosService.obtenerPorId(id).subscribe({
      next: (value) => {
        this.departamento.set(value);
        this.loading.set(false);
      },
      error: (error) => {
        const actionMessage = this.departamentosService.resolveActionMessage(
          error,
          'No se pudo cargar el detalle del departamento.',
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
}
