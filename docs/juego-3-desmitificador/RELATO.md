### File 2: `RELATO.md`
```markdown
# Relato Pedagógico e Integración Interactiva
**Minijuego 3: "Desmitificador: Mito o Verdad"**

## 🎯 Contexto y Enfoque Narrativo
El micro-relato del minijuego transcurre en la vida cotidiana de un estudiante de secundaria promedio. Sin apelar a la ciencia ficción ni a sermones moralistas, la narrativa coloca al jugador frente a frases y creencias comunes que circulan en los recreos, en los grupos de estudio de WhatsApp o antes de dormir.

La experiencia visibiliza cómo el **diseño persuasivo** de las aplicaciones (notificaciones, estímulos visuales, recompensas variables) y la arquitectura biológica del cerebro interactúan en el día a día.

## 📜 Guion de la Experiencia de Usuario (UX Flow)

### 1. Pantalla de Bienvenida e Instrucciones (3 Segundos)
* **Texto en Pantalla:** "Desliza la tarjeta: ¿Creencia popular o dato real? Tenés 60 segundos para desarmar las tramas del diseño digital."
* **Acción de Inicio:** Botón directo "¡Empezar!" sin selección de nivel ni selector de dificultad.

### 2. Bucle Principal de Juego (Gameplay Loop)
* **Escenario:** Una pila de 15 tarjetas centradas en pantalla.
* **Mecánica:**
  * El jugador lee la frase (ej: *"Poner el celular en modo noche me permite usar Instagram en la cama sin afectar mi sueño"*).
  * **Interacción:** Desliza a la **Derecha** ($\rightarrow$) si cree que es Verdad, o a la **Izquierda** ($\leftarrow$) si cree que es un Mito.
* **Transición de Retroalimentación:**
  * Al deslizar, la tarjeta realiza una animación de giro (*Flip*).
  * Se muestra el sello verde de **¡CORRECTO!** o el sello naranja de **DATO REVISADO**, acompañado de la justificación técnica concisa (máximo 30 palabras) respaldada por CHT, SAP o UNICEF.
  * El jugador presiona "Siguiente" o la tarjeta se desvanece tras 1.5 segundos para no cortar el ritmo de la partida.

### 3. Pantalla Final: Evaluación y Táctica Real
Al expirar los 60 segundos o finalizar la tarjeta 15:
* Se muestra el total de aciertos alcanzados y el bonus por tiempo.
* Se revela el **Arquetipo de Perfil** (ej: *Navegante en Observación*).
* **Cierre Constructivo:** Se presenta un mensaje libre de juicios de valor junto a una **Táctica de Autodefensa Digital** ejecutable inmediatamente en los ajustes de su teléfono Android real.