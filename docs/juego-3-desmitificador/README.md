# Juego 3: Desmitificador - Mito o Verdad

## Ficha Técnica
* **Género:** Swipe Cards / Clasificación binaria.
* **Duración por partida:** 60 segundos.
* **Plataforma:** Android (Kotlin / Jetpack Compose).
* **Mecánica principal:** Deslizamiento de tarjetas táctiles (Izquierda = Falso, Derecha = Verdadero) con retroalimentación inmediata (Modal/Flip) y justificación científica.

---

## Concepto y Objetivo
El usuario asume el rol de un **Analista de Verdad** que interactúa con **AURA 9** (una Inteligencia Artificial). El objetivo es clasificar correctamente la mayor cantidad de mitos tecnológicos antes de que expire el tiempo de 60 segundos, limpiando la red de la desinformación conocida como "El Ruido".

---

## Controles e Interacción
* **Swipe Right (Derecha):** Clasificar como **Verdadero**.
* **Swipe Left (Izquierda):** Clasificar como **Falso**.
* **Giro / Modal:** Tras cada acción, la tarjeta muestra la justificación científica basada en evidencia.

---

## Arquitectura de Software Sugerida (Android)
* **Patrón:** MVVM (Model-View-ViewModel).
* **UI:** Jetpack Compose (utilizando estados de desplazamiento `PointerInput` o `Draggable`).
* **Data:** JSON local cargado en un `Repository` para alimentar la lista de mitos.