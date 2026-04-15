import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { EmpleadosService } from '../../services/empleados/empleados.service';

@Component({
  selector: 'app-empleado-edit-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './empleado-edit.page.html',
  styleUrl: './empleado-form.page.css',
})
export class EmpleadoEditPage implements OnInit {
  private readonly formBuilder = inject(FormBuilder);
  private readonly empleadosService = inject(EmpleadosService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  readonly form = this.formBuilder.nonNullable.group({
    nombre: ['', [Validators.required, Validators.maxLength(100)]],
    direccion: ['', [Validators.required, Validators.maxLength(100)]],
    telefono: ['', [Validators.required, Validators.maxLength(100)]],
    correo: ['', [Validators.required, Validators.email, Validators.maxLength(320)]],
    password: [''],
    departamentoId: [1, [Validators.required, Validators.min(1)]],
  });

  readonly loading = signal(false);
  readonly message = signal('');

  private clave = '';

  ngOnInit(): void {
    this.clave = this.route.snapshot.paramMap.get('clave') ?? '';
    if (!this.clave) {
      void this.router.navigateByUrl('/empleados');
      return;
    }

    this.loading.set(true);
    this.empleadosService.obtenerPorClave(this.clave).subscribe({
      next: (empleado) => {
        this.form.patchValue({
          nombre: empleado.nombre,
          direccion: empleado.direccion,
          telefono: empleado.telefono,
          correo: empleado.correo,
          password: '',
          departamentoId: 1,
        });
        this.loading.set(false);
      },
      error: (error) => {
        this.message.set(
          this.empleadosService.resolveActionMessage(error, 'No se pudo cargar el empleado.'),
        );
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
    const raw = this.form.getRawValue();
    const payload = {
      nombre: raw.nombre,
      direccion: raw.direccion,
      telefono: raw.telefono,
      correo: raw.correo,
      departamentoId: raw.departamentoId,
      ...(raw.password.trim() ? { password: raw.password.trim() } : {}),
    };

    this.empleadosService.actualizar(this.clave, payload).subscribe({
      next: () => {
        this.message.set('Empleado actualizado correctamente.');
        this.loading.set(false);
        void this.router.navigateByUrl('/empleados');
      },
      error: (error) => {
        this.message.set(
          this.empleadosService.resolveActionMessage(error, 'No se pudo actualizar el empleado.'),
        );
        this.loading.set(false);
      },
    });
  }
}
