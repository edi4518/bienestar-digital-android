# Guía de Assets y Recursos Visuales / Audiovisuales
**Minijuego 3: "Desmitificador: Mito o Verdad"**

Ubicación sugerida en proyecto Android: `app/src/main/res/`

## 🎨 Recursos Gráficos (`drawable/` y `mipmap/`)

| Asset Name | Formato / Tipo | Descripción / Especificación Visual |
| :--- | :--- | :--- |
| `bg_card_template.xml` | Vector Drawable / Shape | Fondo de tarjeta con esquinas redondeadas ($16\text{dp}$) y sombra elevación ($4\text{dp}$). |
| `ic_swipe_right_true.xml` | Vector Icon | Ícono de verificación / Check ($\checkmark$) en color Verde Menta (`#2ECC71`). |
| `ic_swipe_left_false.xml` | Vector Icon | Ícono de cruz / Mito ($\times$) en color Naranja Coral (`#E67E22`). |
| `badge_source_cht.xml` | Drawable Component | Etiqueta distintiva visual para indicar fuente "Center for Humane Technology". |
| `badge_source_sap.xml` | Drawable Component | Etiqueta distintiva visual para indicar fuente "Sociedad Argentina de Pediatría". |
| `badge_source_unicef.xml` | Drawable Component | Etiqueta distintiva visual para indicar fuente "UNICEF Argentina". |
| `img_archetype_desarmador.svg` | SVG / Vector | Ilustración de perfil: Personaje neutro ajustando la interfaz de un teléfono sobrio. |
| `img_archetype_navegante.svg` | SVG / Vector | Ilustración de perfil: Personaje observando notificaciones flotantes con calma. |
| `img_archetype_presa.svg` | SVG / Vector | Ilustración de perfil: Personaje rodeado de burbujas de notificaciones hiperestimulantes. |

## 🔊 Recursos de Audio (`raw/`)

| Asset Name | Formato | Duración | Uso / Evento Triggereado |
| :--- | :--- | :--- | :--- |
| `sfx_swipe_right.mp3` | Audio MP3 / WAV | $0.2\text{s}$ | Sonido suave de deslizamiento exitoso hacia la derecha. |
| `sfx_swipe_left.mp3` | Audio MP3 / WAV | $0.2\text{s}$ | Sonido suave de deslizamiento hacia la izquierda. |
| `sfx_card_flip.mp3` | Audio MP3 / WAV | $0.3\text{s}$ | Efecto sonoro de tarjeta girando (*flip card*). |
| `sfx_correct_match.mp3` | Audio MP3 / WAV | $0.5\text{s}$ | Tono sutil de acierto en la respuesta. |
| `sfx_game_over.mp3` | Audio MP3 / WAV | $1.2\text{s}$ | Tono alegre de cierre al finalizar los 60 segundos o completar el mazo. |

## 🔤 Estilos y Colores Recomendados (`values/colors.xml`)

```xml
<resources>
    <!-- Paleta Principal Bienestar Digital -->
    <color name="card_background">#FFFFFF</color>
    <color name="swipe_true_accent">#2ECC71</color>
    <color name="swipe_false_accent">#E67E22</color>
    <color name="text_primary_dark">#2C3E50</color>
    <color name="text_secondary_gray">#7F8C8D</color>
    <color name="timer_warning_red">#E74C3C</color>
</resources>