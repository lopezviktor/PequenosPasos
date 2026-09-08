# PequeñosPasos — Improvement Roadmap

Companion to `AUDIT.md`. Ordered so each task only depends on tasks above it — work top to bottom within a component, and do Backend before Web before Mobile, since both clients depend on backend response shapes changing first.

Checkbox groups map to the audit's Phase 1 (🔴) / Phase 2 (🟡) / Phase 3 (🟢) labels.

## Backend (Spring Boot)

### 🔴 Phase 1 — Critical

- [ ] Add `@EnableMethodSecurity` to `SecurityConfig` so every existing `@PreAuthorize` annotation is actually enforced (backend 1.1).
- [ ] Fix the `"ADMIN"` vs `"ADMINISTRADOR"` role-string mismatch: standardize every `@PreAuthorize`/`@PreAuthorize hasRole/hasAuthority` check across `AdminController`, `EducadorController`, `NinoController`, `PadreController`, `PadresHijosController`, `UsuarioController`, `SiestaController`, `NotificacionController` to match the real literal (`ADMIN`) (backend 1.2). Do this in the same PR as the item above — enabling method security alone would otherwise break admin access.
- [ ] Write one integration test (using the already-available `spring-security-test` + H2) that logs in as each of the three roles and asserts an admin-only endpoint returns 200 for `ADMIN`/403 for the others. This is the regression test that proves the two items above actually work.
- [ ] Add `@JsonIgnore` (or `@JsonProperty(access = WRITE_ONLY)`) to `Usuario.password` so the hash stops being serialized anywhere, as an immediate patch (backend 1.3).
- [ ] Inject `PasswordEncoder` into `AdminService` and fix `saveAdmin()`/`updateAdmin()` to hash the password like `PadreService`/`EducadorService` already do (backend 1.5).
- [ ] Move `jwt.secret` and the datasource credentials out of `application.properties` into environment variables / an untracked profile file, rotate the exposed JWT secret, and confirm `application.properties` (or a new secrets file) is `.gitignore`d (backend 1.6).
- [ ] Externalize the hardcoded `/Users/victorlopez/PequenosPasos/images/` path in `WebConfig` into a configurable property with a sane default (backend 1.7).

### 🟡 Phase 2 — Improvements

- [ ] Introduce response DTOs for the remaining entities one at a time (`Nino`, `Usuario`/`Padre`/`Educador`/`Admin`, `Clase`, `Asistencia`, `Actividad`, `Evento`, `ActividadNinos`, `EventoNinos`, `PadresHijos`), following the pattern already used for `Comida`/`Higiene`/`Siesta`/`Mensaje`/`Notificacion` — this is the durable fix behind the `@JsonIgnore` patch above and the biggest structural change in the backend, so budget real time for it (backend 1.4).
- [ ] Add Bean Validation annotations (`@NotBlank`, `@Size`, `@Email`, etc.) to request DTOs and add `@Valid` on controller method parameters, now that DTOs exist for validation to attach to (backend 2.3).
- [ ] Replace `new RuntimeException("...")` throughout services with a small hierarchy of specific exceptions (e.g. `ResourceNotFoundException`, `BusinessRuleException`), give `PadreNotFoundException` (and the new ones) their own `@ExceptionHandler` entries, and delete the string-matching logic in `GlobalExceptionHandler.handleRuntimeException` (backend 2.2).
- [ ] Wire real `Pageable` support into the list endpoints that currently call `findAll()`/`Pageable.unpaged()`, using `EventoRepository`/`EventoService` as the existing template (backend 2.4).
- [ ] Remove the class-level `@CrossOrigin(origins = "*")` on `ClaseController` and rely solely on the global CORS bean in `SecurityConfig` (backend 2.5).
- [ ] Add `springdoc-openapi-starter-webmvc-ui` to `pom.xml` so the `/swagger-ui/**` and `/v3/api-docs/**` routes already permitted in `SecurityConfig` actually work (backend 2.6).
- [ ] Remove the unused `spring-boot-starter-data-jdbc` dependency from `pom.xml` (backend 2.7).
- [ ] Replace `System.out.println`/`System.err`-style logging in `JwtUtils`, `AuthController`, `PadresHijosService`, `ActividadNinosService` with SLF4J (backend 2.8).
- [ ] Switch every controller/service from field injection (`@Autowired` on fields) to constructor injection — do this alongside the DTO or Lombok pass since it touches the same files (backend 2.1).
- [ ] Add unit tests for the services touched by the fixes above (start with `AdminService`, `UsuarioService`, `ClaseService`) and at least one controller-level `@WebMvcTest` (backend 2.9).

### 🟢 Phase 3 — Polish

- [ ] Add Lombok (`@Getter`/`@Setter`/`@NoArgsConstructor`/`@AllArgsConstructor`) to entities and DTOs to remove hand-written boilerplate (backend 3.1).
- [ ] Clean up leftover emoji comments and inconsistent message tone in `AdminController`, `PadreController`, `AdminService` (backend 3.2).
- [ ] Add `spring-boot-starter-actuator` and expose `/actuator/health` (backend 3.3).

## Web frontend (Angular 19)

*Depends on the backend Phase 1 items above being done first — the guard fix and password-field removal both assume the backend's role literal and response shape are already fixed.*

### 🔴 Phase 1 — Critical

- [ ] Fix `authGuard`, `adminOnlyGuard`, and `SidebarComponent.setSidebarItems()` to check `tipoUsuario === 'ADMIN'` (matching the backend's real JWT claim) instead of `'ADMINISTRADOR'`, and verify by logging in as an admin end-to-end (web 1.1).
- [ ] Add an HTTP error interceptor (alongside the existing `AuthInterceptor`) that surfaces failed requests via the app's existing `MessageService` toast pattern, then remove the ad hoc `console.error`-only handlers in individual components (web 1.2).
- [ ] Replace `DashboardPageComponent.renderRankingChart()`'s hardcoded labels/data with a real API call (add a backend endpoint first if one doesn't exist for per-class activity counts) (web 1.3).
- [ ] Remove the `password: string` field from `Parent`/`Educator` TS interfaces once the backend DTO work (backend 1.4) means the API no longer returns it, and fix any compile errors that surface (web 1.4).

### 🟡 Phase 2 — Improvements

- [ ] Replace `any`/`any[]` usages with proper types — start with `SidebarComponent.items: MenuItem[]` (PrimeNG's own type) and `MensajesService`'s `any[]` return types (web 2.1).
- [ ] Add `isLoading`/`hasError` state to page components (start with `ChildrenPageComponent` and `ParentsPageComponent`) and show a spinner/skeleton + retry affordance, building on the interceptor from Phase 1 (web 2.2).
- [ ] Implement a real change-password flow: add the small backend endpoint it needs, then wire `ProfileComponent.cambiarContrasena()` to call it and use `MessageService` instead of `alert()` (web 2.3).
- [ ] Fill in `environment.prod.ts`'s `apiUrl` and document the deployment step (or CI variable) that sets it (web 2.4).

### 🟢 Phase 3 — Polish

- [ ] Move the remaining hardcoded Spanish strings (dashboard labels, a few toast messages) into the existing `es.json`/`en.json` translation files (web 3.1).
- [ ] Replace boilerplate `should create` specs with real tests, prioritizing the guards (now carrying the fixed role logic) and services (web 3.2).

## Mobile app (Kotlin / Jetpack Compose)

*Depends on the backend Phase 1 items above — the password-field removal assumes backend 1.3/1.4 are done; everything else in this section is independent of the backend and web work and can start any time.*

### 🔴 Phase 1 — Critical

- [ ] Move `RetrofitClient.BASE_URL` into a Gradle `BuildConfig` field (per build type/flavor), and update `MenuPrincipal.kt`'s hardcoded image URL to build from the same constant instead of its own literal (mobile 1.1).
- [ ] Rename `Comida.kt`, `Siesta.kt`, and their repositories from `com.pequenospasos.movil.data.model` to `com.example.pequenospasos.data.model`, fixing all import sites (mobile 1.2).
- [ ] Scope `cleartextTrafficPermitted` in `network_security_config.xml` to only the local dev IP's `<domain-config>`, remove the manifest's global `usesCleartextTraffic="true"`, and confirm the app talks HTTPS to anything outside local dev (mobile 1.3).
- [ ] Remove or gate the `Log.d`/`Log.e` calls that print JWTs and personal data (`LoginViewModel`, `MenuPrincipal`, `NotificacionesViewModel`, others) behind a debug-only logging wrapper so nothing sensitive reaches release Logcat (mobile 1.4).

### 🟡 Phase 2 — Improvements

- [ ] Replace `TokenManager`'s in-memory `var token` with `EncryptedSharedPreferences` (or DataStore + Jetpack Security) so login survives process death, and add a 401 interceptor that clears the session and routes back to the login screen (mobile 2.1).
- [ ] Remove the redundant explicit `@Header("Authorization")` parameters on `ApiService.getUsuarioAutenticado`/`getMisNinos` and their manual `"Bearer $token"` call sites in `LoginViewModel`, relying solely on the existing OkHttp interceptor (mobile 2.2).
- [ ] Replace the `try { ... } catch (e: Exception) { emptyList() }` pattern in `ComidaRepository`, `SiestaRepository`, `HigieneRepository`, `ActividadRepository` with a small `Result`/sealed `UiState` type so screens can distinguish "no data" from "request failed" (mobile 2.3).
- [ ] Standardize ViewModel construction: make `LoginViewModel`, `ComidaViewModel`, `HigieneViewModel` take their repository as a constructor parameter via a `Factory` (matching `ActividadViewModel`/`NotificacionesViewModel`) as an interim step, or introduce Hilt for the whole app as the fuller fix (mobile 2.4).
- [ ] Remove the `password` field from the mobile `Padre` data class once backend 1.3/1.4 mean `/api/usuarios/me` no longer returns it (mobile 2.5).

### 🟢 Phase 3 — Polish

- [ ] Add unit tests for `LoginViewModel` and the repositories, now that they're constructor-injected and testable without a real Retrofit client (mobile 3.1).
- [ ] Replace ad hoc `Color(0xFF...)` literals in `ChatScreen`, `MenuPrincipal`, etc. with references to the existing `ui/theme` definitions (mobile 3.2).
- [ ] Enable `isMinifyEnabled = true` with the existing ProGuard rules for the `release` build type (mobile 3.3).
