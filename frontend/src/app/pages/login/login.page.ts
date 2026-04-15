import { CommonModule } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { EstadoAutenticacionUI } from '../../services/auth/auth.models';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.page.html',
  styleUrl: './login.page.css',
})
export class LoginPage implements OnInit {
  private readonly formBuilder = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  readonly form = this.formBuilder.nonNullable.group({
    emailCorporativo: ['', [Validators.required, Validators.email]],
    contrasena: ['', [Validators.required]],
  });

  readonly estado = signal<EstadoAutenticacionUI>('INICIAL');
  readonly mensaje = signal('');

  readonly enviando = computed(() => this.estado() === 'ENVIANDO');

  ngOnInit(): void {
    if (this.authService.isAuthenticated()) {
      void this.router.navigateByUrl('/inicio');
      return;
    }

    if (this.route.snapshot.queryParamMap.get('reason') === 'session-expired') {
      this.estado.set('RECHAZADO');
      this.mensaje.set('Sesión expirada o no autenticada.');
    }
  }

  submit(): void {
    if (this.enviando()) {
      return;
    }

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.estado.set('VALIDACION_ERROR');
      this.mensaje.set('Completa los campos requeridos para iniciar sesión.');
      return;
    }

    this.estado.set('ENVIANDO');
    this.mensaje.set('Validando credenciales...');

    const credentials = this.form.getRawValue();

    this.authService.login(credentials).subscribe((resultado) => {
      if (resultado.resultado === 'ACEPTADO') {
        this.estado.set('AUTENTICADO');
        this.mensaje.set('Acceso concedido. Redirigiendo...');
        void this.router.navigateByUrl('/inicio');
        return;
      }

      if (resultado.resultado === 'RECHAZADO') {
        this.estado.set('RECHAZADO');
        this.mensaje.set(resultado.mensajeUsuario);
        return;
      }

      this.estado.set('ERROR_TECNICO');
      this.mensaje.set(resultado.mensajeUsuario);
    });
  }
}
