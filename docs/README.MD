# Suite Gamificada de Bienestar Digital

## Número de Grupo
* **Grupo N°:** 3

## Integrantes y Roles
* **Eduardo Incrocci** — Lead Developer / Arquitectura Android (Kotlin & Jetpack Compose)
* **Gonzalo Dominguez** — UI/UX Designer & Frontend Specialist (Estilo Neo-Brutalist)
* **Leandro Gomez** — Game Designer & Content Strategist (Lógica de minijuegos y respaldo científico)
* **Franco Moreno** — Game Designer & Content Strategist (Lógica de minijuegos y respaldo científico)

## Temática Elegida
**Pantallas y Redes Sociales:** Consumo problemático, diseño persuasivo (algoritmos de retención), FOMO (Fear of Missing Out) y bienestar socioemocional en adolescentes.

## Breve Descripción del Juego
Aplicación móvil nativa desarrollada para **Android (Kotlin)** bajo una arquitectura modular (Clean Architecture + MVVM). La app funciona como una suite contenedora de **4 minijuegos interactivos** dirigidos a adolescentes (13 a 16 años), diseñados para experimentar de forma lúdica las consecuencias del uso excesivo de pantallas, desarticular mitos cotidianos con base científica (UNICEF y SAP) y ejercitar hábitos de autorregulación del tiempo:
1. **Decisiones Conectadas:** Simulador narrativo de dilemas nocturnos y FOMO que impacta en las barras de Energía, Foco y Ánimo.
2. **Caza de Notificaciones:** Minijuego de reacción contrarreloj para filtrar alertas prioritarias frente a cebos algorítmicos vacíos.
3. **Mito o Verdad (Desmitificador):** Mecánica de tarjetas deslizables (*Swipe Cards*) para contrastar mitos populares con evidencia científica.
4. **Organizador de las 24 Horas:** Puzzle de distribución del tiempo diario que evidencia cómo el exceso de pantalla desplaza las horas esenciales de descanso y sueño.

## Tecnologías y Recursos Necesarios

### 1. Entorno de Desarrollo y Entorno de Ejecución
* **Android Studio:** Entorno de desarrollo integrado (IDE) oficial para la compilación, pruebas y gestión del proyecto.
* **Kotlin:** Lenguaje de programación principal utilizado para la lógica de negocio, ViewModels, gestión de estados y corrutinas.
* **Android SDK (API 31+):** Kit de desarrollo y sistema base de ejecución en emuladores (*Medium Phone*) o dispositivos físicos mediante *Device Mirroring*.

### 2. Arquitectura y Framework de Interfaz (UI)
* **Jetpack Compose:** Kit de herramientas declarativo moderno respaldado por su *Bill of Materials (BOM)* para el diseño dinámico de interfaces de usuario.
* **Jetpack Compose Material 3:** Sistema de diseño visual, componentes interactivos y paleta de colores.
* **Material Icons Extended:** Conjunto extendido de iconografía vectorial.
* **Patrón MVVM + UiState:** Arquitectura limpia basada en *Model-View-ViewModel* con una única fuente de verdad (`UiState`) por pantalla. Garantiza la separación de responsabilidades entre vista y lógica, asegurando la persistencia de datos ante cambios de configuración (como la rotación de pantalla).

### 3. Librerías y Dependencias de Proyecto (`build.gradle.kts`)
* **Jetpack Navigation Compose (`androidx.navigation.compose`):** Sistema de enrutamiento y navegación entre pantallas.
* **Lifecycle ViewModel & Runtime Compose (`androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4`):** Manejo del ciclo de vida y observación reactiva de estados en Compose.
* **Gson (`com.google.code.gson:gson:2.10.1`):** Deserialización y procesamiento de archivos de datos estructurados JSON.
* **Room KTX (`androidx.room.ktx`):** Persistencia de datos locales dentro de la aplicación.

### 4. Recursos de Datos y Sistema
Banco de datos y matriz narrativa del minijuego Decisiones Conectadas. Almacena los modos de jornada iniciales, los eventos aleatorios y los 7 capítulos semanales (de Lunes a Domingo con 5 franjas horarias de dilemas cada uno), los cuales simulan el impacto del consumo digital en las barras de Energía, Ánimo y Foco, culminando en perfiles de arquetipos con tácticas de autodefensa digital. 


## Enlace al Diseño Figma
* [Ver diseño y wireframes en Figma/Whimsical (Abierto y compartido)](https://www.figma.com/design/gE3TChhbK9qyFzihSiWQEy/Sin-t%C3%ADtulo?node-id=1-6&t=zU9kfgbvmdPeJBXT-1)
