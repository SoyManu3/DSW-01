import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { DepartamentosService } from '../../services/departamentos/departamentos.service';

@Component({
  selector: 'app-departamento-edit-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './departamento-edit.page.html',
  styleUrl: './departamento-form.page.css',
})
export class DepartamentoEditPage implements OnInit {
  private readonly formBuilder = inject(FormBuilder);
  private readonly departamentosService = inject(DepartamentosService);
  private readonly authService = inject(AuthService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  readonly form = this.formBuilder.nonNullable.group({
    nombre: ['', [Validators.required, Validators.maxLength(100)]],
  });

  readonly loading = signal(false);
  readonly message = signal('');

  private id = 0;

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id') ?? 0);
    if (!this.id) {
      void this.router.navigateByUrl('/departamentos');
      return;
    }

    this.loading.set(true);
    this.departamentosService.obtenerPorId(this.id).subscribe({
      next: (departamento) => {
        this.form.patchValue({ nombre: departamento.nombre });
        this.loading.set(false);
      },
      error: (error) => {
        const actionMessage = this.departamentosService.resolveActionMessage(
          error,
          'No se pudo cargar el departamento.',
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

  submit(): void {
    if (this.loading()) {
      return;
    }

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.message.set('Completa todos los campos obligatorios.');
      return;
    }

    this.loading.set(true);
    const payload = this.form.getRawValue();

    this.departamentosService.actualizar(this.id, payload).subscribe({
      next: () => {
        this.message.set('Departamento actualizado correctamente.');
        this.loading.set(false);
        void this.router.navigateByUrl('/departamentos');
      },
      error: (error) => {
        const actionMessage = this.departamentosService.resolveActionMessage(
          error,
          'No se pudo actualizar el departamento.',
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
