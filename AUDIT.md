# PequeñosPasos — Technical Audit

**Scope:** Spring Boot backend, Angular 19 web app, Kotlin/Jetpack Compose Android app.
**Purpose:** Identify what to fix, in what order, to turn a 9/10 academic project into a defensible professional portfolio piece. Every finding below was verified by reading the actual source — not inferred from patterns.

## Executive summary

The three biggest issues, all verified end-to-end, are:

1. **Authorization is decorative, not enforced.** Every controller is covered in `@PreAuthorize` annotations, but `SecurityConfig` never enables method security (no `@EnableMethodSecurity`). Spring Security silently ignores every one of those annotations. Any authenticated user — parent, educator, or admin — can currently call any endpoint.
2. **A role-name typo breaks admin access everywhere it matters.** The entity/JWT literal is `"ADMIN"`, but roughly half the `@PreAuthorize`/guard checks (backend *and* frontend) test for `"ADMINISTRADOR"`. Once #1 is fixed, real admin accounts would be locked out of their own admin-only screens and endpoints — and are already locked out of the Angular web app's main layout today.
3. **Password hashes are exposed over the API.** `Usuario.password` has no `@JsonIgnore`, and most endpoints return raw JPA entities instead of DTOs, so `GET /api/usuarios/me`, `/api/padres`, `/api/educadores`, etc. all leak the bcrypt hash to the client. The Angular and Kotlin models even had to model a `password: string` field to match it.

These three are the headline items for Phase 1 across all three codebases. The rest of this document details everything else found.

---

## 1. Backend (Spring Boot)

### 🔴 Phase 1 — Critical

**1.1 — `@PreAuthorize` is never actually enforced**
Every controller uses `@PreAuthorize`/`@PreAuthorize hasRole(...)`, but `SecurityConfig` has no `@EnableMethodSecurity` (or the legacy `@EnableGlobalMethodSecurity(prePostEnabled = true)`). Without it, Spring Security never evaluates those annotations — they're inert comments. Any authenticated user of any role can call any endpoint (list all users, delete another parent's child, read another family's messages, etc.).
*Why it matters in an interview:* this is the single most common "gotcha" in Spring Security setups, and being able to explain both the symptom and the one-line fix demonstrates real understanding of how method security wiring works (vs. copy-pasting annotations that "look right").
*Files:* `security/SecurityConfig.java`
*Complexity:* Low to fix, but must be paired with 1.2 and validated with security tests (see 1.7-equivalent in Phase 2 testing item) so the fix doesn't silently lock everyone out.

**1.2 — Role name mismatch: `"ADMIN"` vs `"ADMINISTRADOR"`**
`Admin.java` sets `tipoUsuario = "ADMIN"`, and `CustomUserDetails` builds the Spring authority as `"ROLE_" + tipoUsuario` → `ROLE_ADMIN`. But `AdminController`, `EducadorController`, `NinoController`, `PadreController`, `PadresHijosController`, `UsuarioController`, `SiestaController`, and `NotificacionController` all check `hasRole('ADMINISTRADOR')` / `hasAuthority('ADMINISTRADOR')` in various places — a string that is never produced anywhere in the system. Once 1.1 is fixed, these checks will always fail for real admins.
*Why it matters:* it's a concrete, easily-explained example of the class of bug that "the code compiles and looks correct" doesn't catch — only an end-to-end test (or a careful audit) does.
*Files:* all controllers listed above (grep for `ADMINISTRADOR` confirms ~15 occurrences)
*Complexity:* Low — standardize on one literal (`ADMIN`) everywhere, ideally via 1.6's enum.

**1.3 — Password hash leaks in every user-returning response**
`Usuario.password` has no `@JsonIgnore`/`@JsonProperty(access = WRITE_ONLY)`. Because most endpoints return `Usuario`/`Padre`/`Educador`/`Admin` entities directly (see 1.4), the bcrypt hash is serialized to JSON on `GET /api/usuarios`, `/api/usuarios/{id}`, `/api/usuarios/me`, `/api/padres`, `/api/educadores`, `/api/admins`, etc.
*Why it matters:* this is a textbook OWASP "sensitive data exposure" finding — trivial to demonstrate with a browser dev tools network tab, and trivial to fix, which makes it a great "I found and fixed a real security bug" interview story.
*Files:* `entity/Usuario.java`, and every controller that returns `Usuario`/subtypes
*Complexity:* Low (one annotation) as an immediate patch; the durable fix is 1.4.

**1.4 — Raw JPA entities returned directly from most controllers**
Only 5 of the ~14 domain entities have a response DTO (`Comida`, `Higiene`, `Siesta`, `Mensaje`, `Notificacion`). Everything else — `Nino`, `Usuario`/`Padre`/`Educador`/`Admin`, `Clase`, `Asistencia`, `Actividad`, `Evento`, `ActividadNinos`, `EventoNinos`, `PadresHijos` — is serialized straight from the entity. This causes the password leak above, risks lazy-init exceptions on bidirectional relations, and couples the API contract to the database schema (a DB refactor becomes a breaking API change).
*Why it matters:* "why do you need a DTO layer" is a very common interview question; having consistently applied it (or having a documented before/after) is a strong signal.
*Files:* `dto/*`, and controller/service pairs across the whole `controller`/`service` packages
*Complexity:* High — this is the single biggest structural change in the backend, best done incrementally, entity by entity.

**1.5 — Admin passwords are stored in plaintext**
`AdminService` never injects `PasswordEncoder`. `saveAdmin()` saves `admin.getPassword()` as-is, and `updateAdmin()` does the same on update — unlike `PadreService`/`EducadorService`, which both correctly call `passwordEncoder.encode(...)`.
*Why it matters:* it's a real, silent inconsistency between three near-identical services — exactly the kind of bug that survives because "it works" in manual testing (login still succeeds, since `BCryptPasswordEncoder.matches()` would just fail for a plaintext hash... actually login would break for admins entirely, since the stored value isn't a valid bcrypt hash). Worth verifying and calling out explicitly as a fixed bug.
*Files:* `service/AdminService.java`
*Complexity:* Low.

**1.6 — Secrets committed to source control**
`application.properties` contains the live JWT signing secret (`jwt.secret=PasswordPequenosPASOS2025JWTAdmin!`) and DB credentials (`spring.datasource.password=victor25`) in plaintext, tracked in git.
*Why it matters:* "how do you manage secrets" is asked in nearly every backend interview; showing you moved secrets to environment variables / a `.env` + `.gitignore` pattern (and rotated the exposed secret) is a concrete, easy win.
*Files:* `src/main/resources/application.properties`
*Complexity:* Low — externalize via env vars or Spring profiles, add to `.gitignore`, rotate the JWT secret (all existing tokens will invalidate, which is fine).

**1.7 — Hardcoded absolute local filesystem path**
`WebConfig` serves `/images/**` from `file:/Users/victorlopez/PequenosPasos/images/` — a path that only exists on the original developer's machine. This 404s in CI, on any other dev's machine, and in any real deployment.
*Why it matters:* it's an immediate, visible "this doesn't run outside my laptop" red flag — the kind of thing that undermines confidence in an otherwise solid demo.
*Files:* `config/WebConfig.java`
*Complexity:* Low — externalize via a config property (`app.images.path`) with an environment-appropriate default, or move to classpath/cloud storage.

### 🟡 Phase 2 — Improvements

**2.1 — Universal field injection instead of constructor injection**
Every controller and service uses `@Autowired` on fields rather than constructor parameters. Spring's own documentation recommends constructor injection: it makes dependencies explicit, enables `final` fields, and — critically — allows unit tests to instantiate the class without a Spring context. (`EducadorServiceTest` already works around this with Mockito's `@InjectMocks`, which happens to support field injection, but it's fighting the design rather than benefiting from it.)
*Why it matters:* "field vs. constructor injection" is a standard Spring interview question with a clear right answer.
*Files:* every `@RestController` and `@Service` class
*Complexity:* Medium — mechanical, but touches every file; do it alongside another pass (e.g., DTO or Lombok work) rather than as a standalone PR.

**2.2 — Generic `RuntimeException` everywhere + fragile string-matching exception handler**
Nearly every service/controller throws bare `new RuntimeException("...")`. `GlobalExceptionHandler.handleRuntimeException` then decides the HTTP status by checking whether `ex.getMessage()` *contains* specific hardcoded Spanish substrings (`"Debe asignar un educador"`, `"Comida no encontrada"`, etc.) — a change to any of those messages silently changes the response's status code. The one custom exception that does exist (`PadreNotFoundException`) isn't even given its own `@ExceptionHandler`.
*Why it matters:* demonstrates understanding of proper exception-hierarchy design (`ResourceNotFoundException`, `BusinessRuleException`, etc.) over ad hoc string matching — a clean, explainable refactor.
*Files:* `exception/GlobalExceptionHandler.java`, `exception/PadreNotFoundException.java`, and throw sites across all services
*Complexity:* Medium.

**2.3 — No Bean Validation on any request body**
No DTO or entity uses `@NotNull`, `@Size`, `@Email`, etc., and no controller method uses `@Valid`. `@Column(nullable = false)` only enforces at the database level, so a malformed request currently fails with a raw 500 (`DataIntegrityViolationException`) instead of a clean 400 — even though `GlobalExceptionHandler` already has a ready `MethodArgumentNotValidException` handler that is effectively dead code today.
*Why it matters:* validation is one of the most visible "did they think about the API contract" signals in a code review.
*Files:* `dto/*`, `entity/*` (where used directly as request bodies), controllers
*Complexity:* Medium.

**2.4 — No pagination on list endpoints**
`getAllNinos`, `getAllUsuarios`, `getAllComidas`, etc. all return unbounded `List<T>` via `findAll()`. `EventoRepository`/`EventoService` are the one place `Pageable` is actually wired up — but the service always calls `Pageable.unpaged()`, so even that capability is unused today.
*Why it matters:* pagination is a standard scalability question; showing you both *understand* it (the `Pageable` plumbing already exists) and *use* it is stronger than either alone.
*Files:* most repositories/services/controllers; `service/EventoService.java` as the starting template
*Complexity:* Medium.

**2.5 — Inconsistent, contradictory CORS configuration**
`SecurityConfig` defines a global CORS bean scoped to `http://localhost:4200`, but `ClaseController` additionally declares `@CrossOrigin(origins = "*")` at the class level — opening just that one controller to every origin, silently overriding the intent of the global config.
*Why it matters:* a small but real security inconsistency, easy to spot and fix, good example of "read the whole codebase before assuming one config file is authoritative."
*Files:* `controller/ClaseController.java`, `security/SecurityConfig.java`
*Complexity:* Low.

**2.6 — Swagger/OpenAPI referenced but never added**
`SecurityConfig` already `permitAll()`s `/swagger-ui/**` and `/v3/api-docs/**`, but `springdoc-openapi-starter-webmvc-ui` is never added to `pom.xml` — those routes 404. This is clearly unfinished intent, not an oversight to hide.
*Why it matters:* API documentation is table stakes for a portfolio backend; this is a five-minute win that also makes the rest of the audit's DTO work self-documenting.
*Files:* `pom.xml`, `security/SecurityConfig.java`
*Complexity:* Low.

**2.7 — Duplicate/unused persistence starter**
`pom.xml` includes both `spring-boot-starter-data-jdbc` and `spring-boot-starter-data-jpa`. Only JPA/Hibernate is actually used anywhere in the codebase.
*Why it matters:* small, but "why is this dependency here" is exactly the kind of question a careful reviewer asks — better to have removed it yourself.
*Files:* `pom.xml`
*Complexity:* Low.

**2.8 — `System.out.println` used instead of a logger**
`JwtUtils.validateToken`, `AuthController.login`, `PadresHijosService`, `ActividadNinosService` all print directly to stdout instead of using SLF4J (already on the classpath via Spring Boot).
*Why it matters:* proper logging (levels, structured output, redirectable sinks) is a baseline production-readiness expectation.
*Files:* `security/JwtUtils.java`, `controller/AuthController.java`, `service/PadresHijosService.java`, `service/ActividadNinosService.java`
*Complexity:* Low.

**2.9 — Near-zero test coverage**
Across ~19 services, 18 controllers, and 21 entities, there is exactly **one** real unit test (`EducadorServiceTest`, 2 test methods). `BackendApplicationTests` only checks that the context loads. `spring-security-test` and H2 are already test dependencies but are never used for integration/security tests.
*Why it matters:* "tell me about your test strategy" is asked in almost every backend interview; right now the honest answer is "there isn't one," which is worth fixing before it comes up.
*Files:* `src/test/**`
*Complexity:* High (ongoing) — but start with a security/integration test proving 1.1 and 1.2 are actually fixed; that single test also doubles as regression protection for the riskiest change in this audit.

### 🟢 Phase 3 — Polish

**3.1 — No Lombok**
Every entity and DTO hand-writes 50–100+ lines of getters/setters/constructors. This is pure boilerplate that Lombok (`@Getter`/`@Setter`/`@NoArgsConstructor`/`@AllArgsConstructor`) removes in one annotation, shrinking diffs and reducing copy-paste risk.
*Why it matters:* fast, visible, low-risk cleanup that immediately makes the codebase look more current/professional in a diff review.
*Files:* `entity/*`, `dto/*`
*Complexity:* Low (mechanical).

**3.2 — Leftover dev artifacts in production code**
Emoji comments (`🔹`) left in `AdminController`, `PadreController`, `AdminService`; inconsistent tone in exception messages (mixing formal/informal Spanish).
*Why it matters:* pure presentation polish, but visible in any code walkthrough.
*Files:* as listed
*Complexity:* Low.

**3.3 — No Actuator / health endpoint**
No `spring-boot-starter-actuator`, so there's no `/actuator/health` for uptime checks, container orchestration, or basic ops visibility.
*Why it matters:* trivial to add, and a natural thing to mention when discussing deployment/observability.
*Files:* `pom.xml`
*Complexity:* Low.

---

## 2. Web frontend (Angular 19)

### 🔴 Phase 1 — Critical

**1.1 — Same role-name bug, on the client**
`authGuard` (`canActivateChild` on the whole main layout) allows only `tipoUsuario === 'ADMINISTRADOR' || tipoUsuario === 'EDUCADOR'`; `adminOnlyGuard` and `SidebarComponent.setSidebarItems()` also check `=== 'ADMINISTRADOR'`. The JWT's `tipoUsuario` claim is always literally `"ADMIN"` (see backend 1.2) — it is never `"ADMINISTRADOR"` anywhere in the system. **Today, a real admin account cannot log into the web app at all**: `login()` succeeds, the token is stored, but the very next navigation to `/dashboard` fails `authGuard` and bounces back to `/login`.
*Why it matters:* this is a fully verified, end-to-end, cross-stack bug (backend issues `"ADMIN"`, frontend checks `"ADMINISTRADOR"`) — a strong, concrete story for "how do you debug across a full stack" in an interview.
*Files:* `guards/auth.guard.ts`, `guards/admin-only/admin-only.guard.ts`, `components/layout/sidebar/sidebar.component.ts`
*Complexity:* Low to fix the string; should be fixed together with backend 1.2 and tested end-to-end.

**1.2 — No global HTTP error handling**
There is an `AuthInterceptor` (attaches the bearer token) but no error interceptor. Every component handles HTTP errors individually via `error: (err) => console.error(...)`, and several (e.g. `children-page`) do nothing user-visible on failure at all — a failed request just looks like nothing happened.
*Why it matters:* a functional (if minimal) `MessageService` toast pattern already exists in the app (used in guards); not using it consistently for HTTP errors is an easy, high-value fix, and "centralized error handling" is a common frontend architecture question.
*Files:* `interceptors/auth.interceptor.ts` (add a sibling), most `*.component.ts` files
*Complexity:* Medium.

**1.3 — Hardcoded, fake dashboard data shipped as if real**
`DashboardPageComponent.renderRankingChart()` renders a bar chart with hardcoded labels (`'Pollitos'`, `'Ardillas'`, `'Renacuajos'`) and hardcoded values (`[10, 8, 5]`) — not backed by any service call. It's the first thing a user (or interviewer) sees after logging in.
*Why it matters:* a portfolio demo that appears to show live data but doesn't is worse than one that's honestly incomplete — this is a "will get caught immediately" risk.
*Files:* `pages/dashboard-page/dashboard-page.component.ts`
*Complexity:* Low–Medium — needs a backend endpoint (activity ranking by class) if one doesn't already exist, then wire it up like the other chart components.

**1.4 — `password` modeled as a required client-side field**
`Parent` and `Educator` TypeScript interfaces both declare `password: string` (non-optional) — a direct symptom of backend 1.3/1.4 (the API actually returns the hash, so the frontend had to model it).
*Why it matters:* fixing this cleanly requires the backend DTO work to land first — good example of tracing a frontend smell back to its backend root cause rather than patching around it.
*Files:* `models/parent.model.ts`, `models/educator.model.ts`
*Complexity:* Low, but blocked on backend 1.3/1.4.

### 🟡 Phase 2 — Improvements

**2.1 — `any` used in 13+ files despite `strict: true`**
`tsconfig.json` has `strict: true` plus `noImplicitReturns`, `strictTemplates`, etc. — a genuinely good baseline — but it's undermined by `any` in 24 places, including `SidebarComponent.items: any[]` (should be PrimeNG's `MenuItem[]`) and `MensajesService.getConversaciones(): Observable<any[]>`.
*Why it matters:* "you have strict mode on, why is this `any`" is a natural follow-up question in a code review; tightening these closes the gap between the *stated* and *actual* type safety of the app.
*Files:* see grep results in `components/`, `services/`, `pages/` (13 files, 24 occurrences)
*Complexity:* Medium.

**2.2 — No loading/error UI state in most page components**
`ChildrenPageComponent` and most other pages have no `isLoading`/`hasError` signal — the table is simply empty until data arrives, with no skeleton/spinner, and errors are swallowed into `console.error`.
*Why it matters:* pairs naturally with 1.2's interceptor work and is a very visible UX polish item.
*Files:* most `pages/*.component.ts`
*Complexity:* Medium.

**2.3 — Non-functional "change password" feature**
`ProfileComponent.cambiarContrasena()` compares the two password fields and, if they match, just `console.log`s the new value and resets the dialog — it never calls a backend endpoint (which doesn't exist yet either). It also uses a native `alert()` for the mismatch case, inconsistent with the app's own `MessageService` toast pattern used everywhere else.
*Why it matters:* a visible, clickable feature that silently does nothing is worse for a demo than not having the button at all.
*Files:* `pages/profile/profile.component.ts`
*Complexity:* Low–Medium — needs a small backend endpoint (`PUT /api/usuarios/{id}/password` or similar) plus wiring.

**2.4 — `environment.prod.ts` ships an empty API URL**
`apiUrl: ''` in production config, with only a comment (`// URL for production API`) and no documented deployment step that fills it in.
*Why it matters:* shows attention to the deploy story, not just "works on my machine."
*Files:* `src/environments/environment.prod.ts`
*Complexity:* Low.

### 🟢 Phase 3 — Polish

**3.1 — i18n mostly implemented, a few strings not translated**
`ngx-translate` with `es.json`/`en.json` is a genuine strength worth highlighting, but some strings (dashboard chart labels, a few guard toast messages) bypass the translation pipeline.
*Files:* `assets/i18n/*.json`, various components
*Complexity:* Low.

**3.2 — Test coverage is CLI-generated boilerplate only**
All ~30 `.spec.ts` files are essentially the default `ng generate` scaffold (`expect(component).toBeTruthy()`); none test actual behavior (form validation, guard logic, service error paths).
*Why it matters:* same story as backend 2.9 — worth fixing the guards/services first since they carry the real logic (and the bugs found in this audit).
*Files:* `src/app/**/*.spec.ts`
*Complexity:* High (ongoing).

---

## 3. Mobile app (Kotlin / Jetpack Compose)

### 🔴 Phase 1 — Critical

**1.1 — Base URL hardcoded to the Android emulator loopback alias, duplicated in two places**
`RetrofitClient.BASE_URL = "http://10.0.2.2:8080/"` only resolves inside the Android emulator, never on a physical device or against a deployed backend. The same literal is duplicated independently in `MenuPrincipal.kt` for building the child's photo URL (`"http://10.0.2.2:8080${nino.fotoUrl}"`) — two hardcoded copies that can silently drift apart.
*Why it matters:* "how do you configure environments per build variant" is a standard Android interview question; this is the clearest possible example of why not to hardcode it.
*Files:* `data/network/RetrofitClient.kt`, `ui/screens/MenuPrincipal.kt`
*Complexity:* Low–Medium — move to a `BuildConfig` field driven by Gradle build types/flavors, reference it from both places.

**1.2 — Package namespace inconsistency**
`Comida.kt`, `Siesta.kt`, and their repositories live under `com.pequenospasos.movil.data.model` / import from it, while the app's actual namespace (per `build.gradle.kts`) and every other file is `com.example.pequenospasos`. This is immediately visible to anyone opening the project in Android Studio and looks like an incomplete rename.
*Why it matters:* small, mechanical, but a real "attention to detail" fix that's easy to explain and easy to verify was done correctly.
*Files:* `data/model/Comida.kt`, `data/model/Siesta.kt`, `data/repository/ComidaRepository.kt`, `data/repository/SiestaRepository.kt`, and their import sites
*Complexity:* Low (rename + fix imports).

**1.3 — Cleartext HTTP permitted network-wide, not just for local dev**
`AndroidManifest.xml` sets `android:usesCleartextTraffic="true"`, and `network_security_config.xml`'s `<base-config cleartextTrafficPermitted="true" />` applies globally — it's not scoped to only the local dev IP that's separately declared in the same file. A release build would happily send credentials over plaintext HTTP to any host.
*Why it matters:* a concrete, fixable security finding with a well-known correct pattern (cleartext only for a debug-only domain override, HTTPS enforced elsewhere).
*Files:* `AndroidManifest.xml`, `res/xml/network_security_config.xml`
*Complexity:* Low — scope cleartext permission to the dev IP's `<domain-config>` only, remove the global `usesCleartextTraffic`/`base-config` override, and require HTTPS once the backend has TLS.

**1.4 — Sensitive data logged in plaintext, unguarded by build type**
`LoginViewModel.login()` logs the full JWT (`Log.d("LoginViewModel", "Token: $token")`), plus the user's name, email, and phone number, on every login. `MenuPrincipal`, `NotificacionesViewModel`, and others log similarly. None of this is gated behind `BuildConfig.DEBUG`, so it ships into release Logcat output.
*Why it matters:* a real, demonstrable privacy/security issue — "what would you change before shipping this" is a natural interview prompt this answers directly.
*Files:* `viewmodel/LoginViewModel.kt`, `ui/screens/MenuPrincipal.kt`, `viewmodel/NotificacionesViewModel.kt`, others found via `Log.d`/`Log.e` grep
*Complexity:* Low — remove or gate behind a debug-only logging wrapper (e.g., Timber with a release tree that no-ops).

### 🟡 Phase 2 — Improvements

**2.1 — Auth token is never persisted**
`TokenManager` is a plain in-memory singleton (`object TokenManager { var token: String = "" }`). It's never written to `EncryptedSharedPreferences`/Jetpack `DataStore`, so any process death or app restart forces the parent to log in again. There's also no handling of the 24h JWT expiry (no refresh, no "session expired" prompt) — the app will just start failing requests with 401s silently.
*Why it matters:* directly answers the audit's "missing token refresh" and "auth handling" concerns with a concrete file and a concrete fix.
*Files:* `data/network/TokenManager.kt`
*Complexity:* Medium — swap to `EncryptedSharedPreferences` (or DataStore + Jetpack Security), add a 401 interceptor that clears the session and routes back to login.

**2.2 — Redundant, inconsistent Authorization header handling**
`RetrofitClient`'s OkHttp interceptor already adds `Authorization: Bearer ${TokenManager.token}` to every request. Yet `ApiService.getUsuarioAutenticado()` and `getMisNinos()` additionally declare an explicit `@Header("Authorization")` parameter that callers fill in manually (`LoginViewModel` passes `"Bearer $token"` by hand) — two competing mechanisms for the same header, which only "work" today because they happen to agree.
*Why it matters:* small but real code-smell; consolidating onto the interceptor removes a class of future bugs (e.g., a caller passing a stale token).
*Files:* `data/network/ApiService.kt`, `viewmodel/LoginViewModel.kt`
*Complexity:* Low.

**2.3 — Network errors silently swallowed to empty lists**
`ComidaRepository`, `SiestaRepository`, `HigieneRepository`, `ActividadRepository` all wrap their Retrofit call in `try { ... } catch (e: Exception) { emptyList() }`. A network timeout, a 401, and "the child genuinely has no records" are all indistinguishable to the UI.
*Why it matters:* directly answers the audit's "no error handling on API calls" concern with concrete files; the fix (a sealed `Result`/`UiState` type) is a well-known, easily explained Android pattern.
*Files:* `data/repository/ComidaRepository.kt`, `SiestaRepository.kt`, `HigieneRepository.kt`, `ActividadRepository.kt`
*Complexity:* Medium.

**2.4 — Inconsistent ViewModel construction / no DI framework**
`LoginViewModel`, `ComidaViewModel`, `HigieneViewModel` are constructed with the no-arg `viewModel()` composable helper and instantiate their repository internally (`private val repository = ComidaRepository()`), making them impossible to unit test without a real Retrofit client. `ActividadViewModel` and `NotificacionesViewModel`, by contrast, take the repository as a constructor parameter via a hand-rolled `ViewModelProvider.Factory`. There's no consistent DI approach (Hilt or Koin) tying the app together.
*Why it matters:* "how would you introduce Hilt into this app" is a great guided-tour interview question once this inconsistency is pointed out and partially fixed.
*Files:* `viewmodel/*.kt`, `data/repository/*.kt`
*Complexity:* Medium–High — introducing Hilt end-to-end is the "big" fix; a smaller step is making every repository constructor-injected and every ViewModel use a Factory consistently.

**2.5 — `password` field present in the mobile `Padre` model**
Mirrors backend 1.3 — the mobile app receives and models a `password: String` field on `Padre` that it should never need, because `/api/usuarios/me` currently returns it.
*Files:* `data/model/Padre.kt`
*Complexity:* Low, blocked on backend 1.3/1.4.

### 🟢 Phase 3 — Polish

**3.1 — No real automated tests**
`ExampleUnitTest.kt` and `ExampleInstrumentedTest.kt` are the untouched Android Studio project templates (`assertEquals(4, 2 + 2)`). No ViewModel, repository, or Composable has any test.
*Files:* `app/src/test/**`, `app/src/androidTest/**`
*Complexity:* High (ongoing) — start with `LoginViewModel` and the repositories once 2.3/2.4 land, since testability is the direct payoff of those fixes.

**3.2 — Ad hoc raw `Color(0xFF...)` literals instead of the app's own theme**
`ui/theme/Color.kt` and `Theme.kt` define a Material 3 theme, but `ChatScreen`, `MenuPrincipal`, and others hardcode raw hex colors inline instead of referencing `MaterialTheme.colorScheme` or theme-defined values.
*Files:* `ui/screens/ChatScreen.kt`, `ui/screens/MenuPrincipal.kt`, others
*Complexity:* Low–Medium.

**3.3 — Release builds are not minified/obfuscated**
`isMinifyEnabled = false` in the `release` build type in `app/build.gradle.kts`, despite ProGuard files already being referenced.
*Files:* `app/build.gradle.kts`
*Complexity:* Low.

---

## Cross-cutting observations

- The **`ADMIN` vs `ADMINISTRADOR`** role-string bug (backend 1.2, web 1.1) is the single most valuable "war story" in this audit: it's a real, verified, end-to-end bug spanning two codebases, has a one-line fix once found, and demonstrates systematic debugging — worth leading with in an interview.
- The **password-exposure chain** (backend 1.3/1.4 → web 1.4 → mobile 2.5) is a good second story: one root cause (missing `@JsonIgnore`/DTOs) visibly propagating into both clients' type definitions, and one fix that cleans up all three.
- **Test coverage is near-zero in all three components.** Rather than treating this as one giant Phase-3 backlog item, the roadmap below front-loads a handful of targeted tests immediately after each critical security fix, so the fixes themselves are protected by regression tests rather than trusted on faith.
