import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { DepartamentosService } from '../../services/departamentos/departamentos.service';

@Component({
  selector: 'app-departamento-create-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './departamento-create.page.html',
  styleUrl: './departamento-form.page.css',
})
export class DepartamentoCreatePage {
  private readonly formBuilder = inject(FormBuilder);
  private readonly departamentosService = inject(DepartamentosService);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  readonly form = this.formBuilder.nonNullable.group({
    nombre: ['', [Validators.required, Validators.maxLength(100)]],
  });

  readonly loading = signal(false);
  readonly message = signal('');

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
    this.departamentosService.crear(payload).subscribe({
      next: () => {
        this.message.set('Departamento creado correctamente.');
        this.loading.set(false);
        void this.router.navigateByUrl('/departamentos');
      },
      error: (error) => {
        const actionMessage = this.departamentosService.resolveActionMessage(
          error,
          'No se pudo crear el departamento.',
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
