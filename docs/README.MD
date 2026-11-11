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

Juegos funcionales:
Decisiones Conectadas: Simulador narrativo de dilemas que gestiona las barras de Energía, Ánimo y Foco.
Organizador de las 24 Horas: Puzzle de distribución del tiempo diario.

Estructura de la Aplicación: Creacion de Perfil → Inicio/Menú Principal → Selección de Minijuego (Decisiones Conectadas / Organizador 24hs) → Interacción / Decisiones → Resultado y Arquetipo final.

Implementación Técnica
MVVM + UiState: Arquitectura limpia con separación de responsabilidades y única fuente de verdad por pantalla.

Ciclo de vida: Manejo de corrutinas mediante viewModelScope y limpieza en onCleared().

Componentes y Librerías: Jetpack Compose, Material 3, Navigation Compose, Gson para parseo de datos y Room KTX.

## Enlace al Diseño Figma
* [Ver diseño y wireframes en Figma/Whimsical (Abierto y compartido)](https://www.figma.com/design/gE3TChhbK9qyFzihSiWQEy/Sin-t%C3%ADtulo?node-id=1-6&t=zU9kfgbvmdPeJBXT-1)
