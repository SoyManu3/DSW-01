import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { EmpleadosService } from '../../services/empleados/empleados.service';

@Component({
  selector: 'app-empleado-create-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './empleado-create.page.html',
  styleUrl: './empleado-form.page.css',
})
export class EmpleadoCreatePage {
  private readonly formBuilder = inject(FormBuilder);
  private readonly empleadosService = inject(EmpleadosService);
  private readonly router = inject(Router);

  readonly form = this.formBuilder.nonNullable.group({
    nombre: ['', [Validators.required, Validators.maxLength(100)]],
    direccion: ['', [Validators.required, Validators.maxLength(100)]],
    telefono: ['', [Validators.required, Validators.maxLength(100)]],
    correo: ['', [Validators.required, Validators.email, Validators.maxLength(320)]],
    password: ['', [Validators.required, Validators.minLength(8)]],
    departamentoId: [1, [Validators.required, Validators.min(1)]],
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
    this.empleadosService.crear(payload).subscribe({
      next: () => {
        this.message.set('Empleado creado correctamente.');
        this.loading.set(false);
        void this.router.navigateByUrl('/empleados');
      },
      error: (error) => {
        this.message.set(
          this.empleadosService.resolveActionMessage(error, 'No se pudo crear el empleado.'),
        );
        this.loading.set(false);
      },
    });
  }
}
