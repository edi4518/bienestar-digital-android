# DOCUMENTACIÓN Y ENTREGA FINAL
## SEGUNDA ENTREGA: EVOLUCIÓN DE LA APP-JUEGO • BIENESTAR DIGITAL

* **Materia:** Desarrollo de Aplicaciones para Dispositivos Móviles
* **Institución:** IFTS N.º 18
* **Modalidad:** Trabajo Grupal
* **Repositorio GitHub:** [https://github.com/edi4518/bienestar-digital-android.git](https://github.com/edi4518/bienestar-digital-android.git)

---

### 1. Descripción de los Juegos, Mecánicas y Mejoras

La aplicación integra **tres juegos completos y funcionales** orientados a la concienciación y entrenamiento del hábito digital saludable:

1. **Decisiones Conectadas (Narrativa Interactiva):**
   * **Mecánica:** Juego de toma de decisiones en 7 capítulos sobre dilemas nocturnos de uso de pantallas, dopamina y descanso.
   * **Objetivo:** Tomar elecciones conscientes que equilibren los indicadores globales de energía, foco y ánimo del jugador.

2. **Caza de Notificaciones (Reflejos y Atención en Tiempo Real):**
   * **Mecánica:** Juego de acción rápida con velocidad y cadencia de aparición de notificaciones aceleradas progresivamente.
   * **Puntaje y Reglas:** Notificaciones prioritarias (+100 pts), notificaciones trampa ignoradas (+50 pts si expiran), notificaciones trampa tocadas (-50 pts y consumo de 1 de las 3 vidas/corazones).
   * **Finalización:** Se agota el tiempo de 60 segundos o el jugador pierde sus 3 vidas.

3. **El Desmitificador (Mito o Verdad Científico):**
   * **Mecánica:** 15 tarjetas con citas y evidencias empíricas respaldadas científicamente por la OMS, UNICEF, la Sociedad Argentina de Pediatría (SAP), la Universidad de Stanford y el Center for Humane Technology.
   * **Feedback Inmediato:** Barra de progreso segmentada que se pinta de **verde neón** al acertar o **rojo neón** al cometer un error, con tarjetas de evidencia justificativa compactas y 3 arquetipos de perfil digital finales.

* **Mejoras Destacadas:**
  * Rediseño de la barra de estadísticas del jugador con porcentajes (`Energía 30%`, `Foco 40%`, `Ánimo 100%`).
  * Sistema dinámico de racha de días consecutivos de conexión con fuego (`🔥`).
  * Indicador de capítulo guardado (`Cap. X/7`) en el menú principal (Hub).
  * Selector interactivo de perfil con foto real desde la galería del teléfono y 8 avatares presets.

---

### 2. Base de Datos Local (Room / SQLite) y Ranking en Línea (Firebase)

* **Almacenamiento Local (Room sobre SQLite):**
  * Implementado con patrón Singleton `BienestarDatabase` y soporte de migración destructiva preventiva.
  * **`AuditoriaDiariaEntity`**: Almacena el resumen diario de consumo en pantalla, tiempo en redes sociales y cumplimiento de metas.
  * **`UserProfileEntity`**: Persiste el perfil, Gamertag, foto de avatar y niveles de energía del jugador.
  * **`MinijuegoRecordEntity`**: Guarda el historial de puntuaciones máximas jugadas offline.

* **Ranking Global en la Nube (Firebase Cloud Firestore):**
  * Configurado mediante `google-services.json` y el SDK `firebase-firestore-ktx`.
  * Al finalizar cualquier partida, los puntajes se envían automáticamente a la nube.
  * **Pestaña GLOBAL:** Agrupa por usuario y **suma todos los puntos acumulados** de ambos minijuegos.
  * **Pestañas Específicas (`Caza Notis` / `Mitos`):** Muestra el récord máximo por usuario en dicho juego.
  * Muestra el Podio Top 3 con medallas (🥇 Oro, 🥈 Plata, 🥉 Bronce).

---

### 3. Uso de Sensores y Hardware del Dispositivo Android

* Se integró la API de hardware y sensores de actividad en el dispositivo `UsageStatsManager` a través del permiso `android.permission.PACKAGE_USAGE_STATS`.
* La aplicación consulta directamente el sensor y registro de estado del teléfono para medir el uso real en pantalla (redes sociales, juegos, streaming) y el tiempo de desconexión.
* En el *Organizador de las 24 Horas*, la función **"Analizar Uso Real del Dispositivo"** impacta directamente en el estado del juego recalculando la batería diurna, la claridad cognitiva y el cansancio del avatar.

---

### 4. Arquitectura y Patrón MVVM

La aplicación cumple rigurosamente con la arquitectura **Model-View-ViewModel**:

* **Model (Modelo):** Entidades de Room, Data Sources (`json/assets`) y Repositorios (`RankingRepository`, `UsageStatsRepository`, `UserProgressRepository`).
* **View (Vista):** Interfaz declarativa construida con Jetpack Compose (`HubScreen`, `CazaNotificacionesScreen`, `DesmitificadorScreen`, `Organizador24hView`).
* **ViewModel (Estado y Lógica):** Manejo unireccional de estados reactivos con `StateFlow` (`HubViewModel`, `CazaNotificacionesViewModel`, `Organizador24hViewModel`), ejecutando corrutinas de manera segura en `viewModelScope`.

---

### 5. Enlaces y Design System

* **Repositorio de GitHub:** [https://github.com/edi4518/bienestar-digital-android.git](https://github.com/edi4518/bienestar-digital-android.git)
* **Design System Neo-Brutalista Dark:**
  * **Canvas Oscuro:** `#0F0F14`
  * **Tarjetas:** `#1B1B24`
  * **Bordes Estratégicos:** `#2E2E3E`
  * **Acento Lima:** `#CCFF00`
  * **Acento Cian:** `#00F5D4`
  * **Acento Rosa / Danger:** `#FF3366` / `#F87171`
  * Layouts en Bento Grid, tipografía bold y retroalimentación hática/sonora (`SoundPlayer`).

---

### 6. Tabla de Integrantes, Roles y Tareas Realizadas

| Integrante | Rol Principal | Tareas Realizadas |
| :--- | :--- | :--- |
| **Eduardo** | Lead Developer & Creacion y desarrrollador | Implementación de Room/SQLite, Firebase Firestore, UsageStatsManager, y Minijuego 1 Deciciones Conectadas. |
| **Gonzalo** | Diseñador UI/UX & Creacion y desarrollo | Diseño del Design System Neo-Brutalista, Bento Grid Layouts y prototipo de pantallas y desarrollo de Organizador 24 hs y de la pantalla de Home. |
| **Leandro** | Arquitectura y creacion de contenido Minijuego | Logica y desarrollo del Minijuego Caza de Notificaciones y pruebas de unidad y desarrollo del diseño de pantalla de creacion de perfil. |
| **Franco** | Arquitectura y creacion de contenido Minijuego | Desarrollo de Nimijuego de Mito o Verdad y desarrollo de logica de la creacion del perfil y pruebas de unidad. |

---

### 7. Instrucciones para Ejecutar la Aplicación

1. Clonar el repositorio: `git clone https://github.com/edi4518/bienestar-digital-android.git`
2. Abrir el proyecto en Android Studio (Ladybug / Koala o superior con JDK 17).
3. Verificar que el archivo `app/google-services.json` esté presente en el módulo app.
4. Compilar y ejecutar en un dispositivo físico o emulador con Android 9.0 (API 28) o superior.
5. Otorgar el permiso de Acceso a Datos de Uso cuando el minijuego Organizador 24h lo solicite para habilitar la auditoría real.
