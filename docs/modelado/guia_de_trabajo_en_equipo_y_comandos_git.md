# Guía Operativa de Desarrollo y Flujo de Trabajo en Git
**Proyecto:** Suite Gamificada de Bienestar Digital — Android / Kotlin  
**Paquete Raíz:** `com.example.bienestar_digital_android`  
**Rama por Defecto:** `develop`  
**Destinatarios:** Equipo de Desarrollo

---

## 1. Reglas Fundamentales del Repositorio

Para garantizar la estabilidad del proyecto y evitar la pérdida o sobreescritura de código, todo el equipo debe cumplir estrictamente con los siguientes principios:

1. **Ramas Protegidas:** Las ramas `main` y `develop` están protegidas por reglas de repositorio. Nadie puede ejecutar `git push` directo a ninguna de ellas. Todo cambio ingresa exclusivamente mediante un **Pull Request (PR)**.
2. **Aislamiento de Trabajo:** Todo desarrollo, pantalla, ajuste visual o corrección de bugs se realiza en una rama dedicada `feature/` creada a partir de la versión más reciente de `develop`.
3. **Revisión de Código Obligatoria (*Peer Review*):** Ningún PR puede integrarse a `develop` sin contar con al menos **una (1) aprobación formal** de otro miembro del equipo.
4. **Validación Previa:** Antes de subir cualquier cambio a GitHub, se debe compilar el proyecto localmente en Android Studio (`Build > Rebuild Project`) para asegurar que no contenga errores sintácticos ni de compilación.

---

## 2. Configuración Inicial del Entorno (Paso a Paso - Solo una vez)

Antes de comenzar a programar en la máquina local, cada miembro debe ejecutar los siguientes pasos:

### 2.1. Aceptar la invitación en GitHub
1. Ingresar al correo electrónico asociado a la cuenta de GitHub o directamente a las notificaciones en [github.com](https://github.com/).
2. Aceptar la invitación de colaboración enviada por el administrador.
3. *Sin este paso no se dispondrá de permisos para subir ramas ni interactuar con el repositorio.*

### 2.2. Habilitar rutas largas en Windows (Crítico)
Debido a la anidación profunda de los paquetes de Clean Architecture en Android, Git para Windows puede fallar al indexar archivos con el error `Filename too long`. Para resolverlo permanentemente, abrir PowerShell o la terminal y ejecutar:

```bash
git config --global core.longpaths true
```

### 2.3. Clonar el repositorio
Abrir la terminal en el directorio de trabajo habitual y clonar el repositorio:

```bash
git clone https://github.com/TU_ADMIN/bienestar-digital-android.git
cd bienestar-digital-android
```

Al estar configurado el repositorio central, la rama activa inicial será **`develop`**. Se puede verificar ejecutando:

```bash
git branch
# Debe indicar: * develop
```

---

## 3. Mapeo de la Arquitectura: Dónde Ubicar tu Código

Para prevenir conflictos de integración (*merge conflicts*), cada integrante debe limitar sus modificaciones a los paquetes correspondientes a su tarea asignada:

```plaintext
com.example.bienestar_digital_android/
├── MainActivity.kt               <-- Contenedor principal de la aplicación y Theme
│
├── core/                         <-- Elementos transversales del sistema
│   ├── designsystem/             <-- Tokens globales (Color.kt, Type.kt, Shape.kt, Theme.kt)
│   ├── components/               <-- Componentes Neo-Brutalistas reutilizables
│   └── common/                   <-- Extensiones, utilitarios y Coroutine Dispatchers
│
├── data/                         <-- Capa de Persistencia Local
│   ├── local/                    <-- Entidades Room, DAOs o DataStore
│   └── repository/               <-- Implementación concreta de repositorios
│
├── domain/                       <-- Capa de Lógica de Negocio Pura (Independiente de UI)
│   ├── model/                    <-- Modelos de dominio
│   └── usecase/                  <-- Casos de uso de los minijuegos y perfil
│
└── feature/                      <-- Capa de Presentación (UI y ViewModels)
    ├── onboarding/               <-- Registro de perfil inicial (Alias y Edad)
    ├── hub/                      <-- Menú Bento Grid central
    ├── decisionesconectadas/     <-- Minijuego 1: Narrativa Interactiva
    ├── cazanotificaciones/       <-- Minijuego 2: Reacción y Discriminación de Alertas
    ├── desmitificador/           <-- Minijuego 3: Swipe Cards (Mito o Verdad)
    └── organizador24h/           <-- Minijuego 4: Balance de Tiempo y Sueño (SAP)
```

> **Documentación y Recursos:** En la raíz del repositorio se ubican las carpetas `/docs` (investigación teórica, guiones pedagógicos, fichas de casos de uso y modelos de dominio) y `/design` (diagramas UML exportados, tokens visuales e iconos).

---

## 4. Flujo de Trabajo Diario

### Paso 4.1: Sincronizar y crear rama de trabajo
Antes de escribir cualquier línea de código, situarse en `develop`, actualizarla con los cambios más recientes del equipo y derivar la rama:

```bash
# 1. Posicionarse en la rama de integración
git checkout develop

# 2. Descargar los últimos cambios aprobados
git pull origin develop

# 3. Crear y pasarse a la nueva rama según la nomenclatura oficial
git checkout -b feature/<nombre-feature>
```

#### Ramas oficiales por módulo:
* `feature/onboarding-profile` (Registro y datos del perfil inicial)
* `feature/hub-bento-grid` (Menú Bento Grid principal y navegación)
* `feature/game-decisiones-conectadas` (Minijuego 1)
* `feature/game-caza-notificaciones` (Minijuego 2)
* `feature/game-desmitificador` (Minijuego 3)
* `feature/game-organizador-24hs` (Minijuego 4)

---

### Paso 4.2: Guardar cambios usando Conventional Commits
Cada commit debe ser atómico (resolver una única cosa puntual) y seguir la sintaxis de commits convencionales:

$$\text{tipo(módulo): descripción clara en minúsculas y sin punto final}$$

#### Prefijos permitidos:
* **`feat:`** Nueva funcionalidad o interfaz visible.  
  *Ejemplo:* `git commit -m "feat(hub): add streak counter and daily hero challenge"`  
  *Ejemplo:* `git commit -m "feat(decisionesconectadas): implement dilemma card decision logic"`
* **`fix:`** Corrección de un fallo o error en la lógica de cálculo.  
  *Ejemplo:* `git commit -m "fix(organizador24h): prevent sleep hours from being reduced below zero"`
* **`style:`** Modificaciones estéticas (bordes, sombras Neo-Brutalistas, padding) sin alterar la lógica.  
  *Ejemplo:* `git commit -m "style(components): adjust BrutalistButton border width and drop shadow"`
* **`refactor:`** Reestructuración interna de código sin añadir funcionalidades ni reparar bugs.  
  *Ejemplo:* `git commit -m "refactor(cazanotificaciones): extract countdown timer to a reusable helper"`
* **`docs:`** Actualizaciones en documentación, diagramas o comentarios.  
  *Ejemplo:* `git commit -m "docs: update class diagram references"`

#### Comandos para guardar avances:
```bash
git add .
git commit -m "tipo(modulo): descripcion de la tarea"
```

---

### Paso 4.3: Subir la rama a GitHub
Una vez que la tarea esté finalizada y probada en el emulador:

```bash
git push origin feature/<nombre-feature>
```

---

### Paso 4.4: Crear el Pull Request (PR)
1. Entrar al repositorio en la web de GitHub.
2. Hacer clic en el botón verde **Compare & pull request** que aparecerá tras subir la rama.
3. **Verificar ramas de origen y destino:**
   * **base:** `develop` (Bajo ninguna circunstancia seleccionar `main`).
   * **compare:** `feature/<nombre-feature>`
4. **Título del PR:** Debe ser descriptivo, por ejemplo: `feat(onboarding): implementacion de selector de edad y validacion de alias`.
5. **Reviewers (Revisores):** En la columna lateral derecha, asignar obligatoriamente a un compañero de equipo para la revisión del código.
6. Presionar **Create pull request**.

---

### Paso 4.5: Proceso de Revisión por Pares (*Code Review*)
El integrante asignado como revisor debe:
1. Ingresar al PR y dirigirse a la pestaña **Files changed**.
2. Verificar que:
   * El código respete la arquitectura y los paquetes correspondientes.
   * Se utilicen los componentes del sistema de diseño (`core/components`) y no estilos directos aislados.
   * No existan ramas de código muertas o archivos innecesarios subidos.
3. Presionar **Review changes**:
   * Si todo está correcto, marcar **Approve** y enviar la reseña.
   * Si hay aspectos a corregir, marcar **Request changes** y detallar los puntos a ajustar.

---

### Paso 4.6: Merge y Limpieza de Ramas
1. Una vez obtenida la aprobación (tilde verde), presionar **Merge pull request** y confirmar.
2. Hacer clic en **Delete branch** en la web de GitHub para eliminar la rama remota ya fusionada.
3. En la máquina local, regresar a `develop` y borrar la rama local:
   ```bash
   git checkout develop
   git pull origin develop
   git branch -d feature/<nombre-feature>
   ```

---

## 5. Cierre de Release Oficial hacia `main`

La rama `main` refleja en todo momento la versión estable final, lista para generación de APK o evaluación formal.

Cuando todos los módulos (`onboarding`, `hub` y los 4 minijuegos) hayan sido integrados y validados en conjunto sobre `develop`:
1. El administrador del repositorio generará un Pull Request final:
   * **base:** `main`
   * **compare:** `develop`
2. El equipo realizará una revisión conjunta final.
3. Tras la aprobación, se ejecutará el merge definitivo hacia `main`.