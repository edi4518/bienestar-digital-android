# Minijuego 3: Desmitificador - Mito o Verdad

## 📌 Descripción General
El **Minijuego 3 ("Desmitificador")** forma parte de la *Suite Gamificada de Bienestar Digital*. Es un juego de clasificación binaria ágil (*Swipe Cards*) diseñado para adolescentes de 13 a 16 años (escuela secundaria). Su objetivo es desarticular falacias populares y mitos sobre la multitarea digital, el descanso nocturno, el uso del celular y la atención, promoviendo la alfabetización digital mediante evidencia científica directa.

## 🛠 Especificaciones Técnicas (Android / Kotlin)
- **Vista / Interfaz:** Layouts nativos basados en `ConstraintLayout` / Jetpack Compose (`CardStackView` o gestos de `pointerInput` para Swipe).
- **Mecánica:** Tarjetas deslizables (*Swipe Left* = FALSO / MITO; *Swipe Right* = VERDADERO).
- **Temporizador:** `CountDownTimer` de 60 segundos ($60000\text{ ms}$).
- **Feedback:** Animación *Flip Card* de 180° sobre el eje Y o Dialog Modal liviano con la justificación pedagógica antes de cargar la siguiente tarjeta.
- **Puntuación y Sistema de Bonus:**
    - Acierto: $+100$ pts.
    - Error: $0$ pts (sin penalización ni reseteo punitivo para priorizar la lectura formativa).
    - Bonus final por tiempo: $+10$ pts por cada segundo remanente en el reloj al completar la baraja.

## 📂 Estructura del Banco de Datos (JSON / Data Model)
El contenido de las 15 tarjetas se consume mediante una colección serializada (`List<CardModel>`):

```json
[
  {
    "id": "CARD_01",
    "statement": "Responder mensajes de WhatsApp mientras resuelves la tarea de matemáticas no afecta tu rendimiento escolar.",
    "correctDirection": "LEFT",
    "classification": "MITO",
    "feedback": "El cerebro no procesa dos tareas complejas a la vez; realiza switch tasking, aumentando hasta un 40% el tiempo necesario para terminar la tarea.",
    "source": "CHT"
  }
]