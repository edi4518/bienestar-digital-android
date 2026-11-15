import os
from reportlab.lib.pagesizes import letter
from reportlab.lib import colors
from reportlab.platypus import SimpleDocTemplate, Paragraph, Table, TableStyle, HRFlowable
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.units import inch

def generar_pdf():
    pdf_filename = "DOCUMENTACION_ENTREGA_FINAL.pdf"
    doc = SimpleDocTemplate(
        pdf_filename,
        pagesize=letter,
        rightMargin=36, leftMargin=36, topMargin=36, bottomMargin=36
    )

    styles = getSampleStyleSheet()

    # Texto principal en #0F0F14 según indicación
    color_texto_principal = colors.HexColor('#0F0F14')

    title_style = ParagraphStyle(
        'DocTitle',
        parent=styles['Heading1'],
        fontName='Helvetica-Bold',
        fontSize=18,
        leading=22,
        textColor=color_texto_principal,
        alignment=1, # Center
        spaceAfter=6
    )

    subtitle_style = ParagraphStyle(
        'DocSubTitle',
        parent=styles['Normal'],
        fontName='Helvetica-Bold',
        fontSize=10.5,
        leading=13,
        textColor=color_texto_principal,
        alignment=1,
        spaceAfter=14
    )

    h1_style = ParagraphStyle(
        'H1',
        parent=styles['Heading2'],
        fontName='Helvetica-Bold',
        fontSize=12,
        leading=15,
        textColor=color_texto_principal,
        spaceBefore=10,
        spaceAfter=5
    )

    body_style = ParagraphStyle(
        'Body',
        parent=styles['BodyText'],
        fontName='Helvetica',
        fontSize=8.5,
        leading=11.5,
        textColor=color_texto_principal,
        spaceAfter=4
    )

    bullet_style = ParagraphStyle(
        'Bullet',
        parent=body_style,
        leftIndent=12,
        firstLineIndent=-8,
        spaceAfter=3
    )

    story = []

    # Título Principal
    story.append(Paragraph("DOCUMENTACIÓN Y ENTREGA FINAL", title_style))
    story.append(Paragraph("SEGUNDA ENTREGA: EVOLUCIÓN DE LA APP-JUEGO • BIENESTAR DIGITAL", subtitle_style))
    story.append(Paragraph("<b>Materia:</b> Desarrollo de Aplicaciones para Dispositivos Móviles | <b>Institución:</b> IFTS N.º 18", body_style))
    story.append(HRFlowable(width="100%", thickness=1.5, color=color_texto_principal, spaceBefore=4, spaceAfter=8))

    # Sección 1: Descripción de los Juegos y Mejoras
    story.append(Paragraph("1. Descripción de los Juegos, Mecánicas y Mejoras", h1_style))
    story.append(Paragraph("La aplicación integra tres juegos completos y funcionales orientados a la concienciación y entrenamiento del hábito digital:", body_style))

    story.append(Paragraph("• <b>Decisiones Conectadas (Narrativa Interactiva):</b> Juego de toma de decisiones en 7 capítulos sobre dilemas nocturnos de uso de pantallas y dopamina. El jugador elige respuestas que afectan sus indicadores de energía, foco y ánimo en tiempo real.", bullet_style))
    story.append(Paragraph("• <b>Caza de Notificaciones (Reflejos y Atención):</b> Minijuego en tiempo real con dinámicas de aceleración progresiva. El jugador debe seleccionar notificaciones prioritarias (+100 pts) y evitar trampas (-50 pts). Incluye un sistema de 3 vidas/corazones y temporizador de 60 segundos.", bullet_style))
    story.append(Paragraph("• <b>El Desmitificador (Mito o Verdad Científico):</b> Minijuego con 15 tarjetas respaldadas por evidencia empírica de organismos internacionales (OMS, UNICEF, SAP, Stanford, Center for Humane Tech). Cuenta con mezcla aleatoria de tarjetas, feedback inmediato con justificación científica, barra de progreso con distinción de aciertos (verde) y errores (rojo), y 3 arquetipos de perfil digital finales.", bullet_style))

    story.append(Paragraph("<b>Mejoras Destacadas en esta Entrega:</b> Integración de barra de estadísticas del jugador con porcentajes en el Hub, sistema dinámico de racha de días consecutivos (fuego 🔥), indicador de capítulo guardado (Cap. X/7) e interfaz de edición de perfil con avatar y foto de galería.", body_style))

    # Sección 2: Base de Datos Local Room/SQLite y Firebase
    story.append(Paragraph("2. Base de Datos Local (Room / SQLite) y Ranking en Línea (Firebase)", h1_style))
    story.append(Paragraph("<b>Almacenamiento Local (Room sobre SQLite):</b> Se implementó una arquitectura de persistencia con patrón Singleton <i>BienestarDatabase</i>, soporte de migración destructiva preventiva y tres entidades de datos principales:", body_style))
    story.append(Paragraph("• <i>AuditoriaDiariaEntity:</i> Almacena la foto diaria del consumo de pantalla, redes sociales, tiempo libre y metas cumplidas.", bullet_style))
    story.append(Paragraph("• <i>UserProfileEntity:</i> Persiste el perfil del jugador, Gamertag, foto/avatar y métricas globales.", bullet_style))
    story.append(Paragraph("• <i>MinijuegoRecordEntity:</i> Guarda los récords personales e historial de partidas jugadas offline.", bullet_style))

    story.append(Paragraph("<b>Ranking Global en la Nube (Firebase Cloud Firestore):</b> Integrado mediante el SDK de Firebase (<i>google-services.json</i> y <i>firebase-firestore-ktx</i>). Al finalizar cualquier partida, los puntajes se suben a la nube. En la pestaña <b>GLOBAL</b>, el repositorio agrupa las entradas por usuario y suma sus puntos totales acumulados en ambos juegos. En las pestañas de minijuego individual, muestra la mejor marca del jugador.", body_style))

    # Sección 3: Descripción del Sensor Utilizado
    story.append(Paragraph("3. Uso de Sensores y Hardware del Dispositivo Android", h1_style))
    story.append(Paragraph("Se incorporó la integración con la API de hardware y estado del sistema <i>UsageStatsManager</i> mediante el permiso <i>android.permission.PACKAGE_USAGE_STATS</i>. La aplicación mide directamente los sensores de actividad en pantalla del dispositivo para registrar el tiempo real consumido en redes sociales, streaming y tiempo de desconexión. En la pantalla <i>Organizador de las 24 Horas</i>, la función <i>'Analizar Uso Real del Dispositivo'</i> impacta de forma directa sobre la mecánica del juego recalculando la batería diurna, la claridad cognitiva y el cansancio del avatar.", body_style))

    # Sección 4: Arquitectura MVVM
    story.append(Paragraph("4. Arquitectura y Patrón MVVM", h1_style))
    story.append(Paragraph("La aplicación mantiene una separación estricta de responsabilidades bajo el patrón Model-View-ViewModel:", body_style))
    story.append(Paragraph("• <b>Model (Datos):</b> Repositorios (<i>RankingRepository</i>, <i>UsageStatsRepository</i>, <i>UserProgressRepository</i>) y fuentes de datos Room/Firebase.", bullet_style))
    story.append(Paragraph("• <b>View (Interfaz):</b> Pantallas declarativas construidas íntegramente con Jetpack Compose (<i>HubScreen</i>, <i>CazaNotificacionesScreen</i>, <i>DesmitificadorScreen</i>, <i>Organizador24hView</i>).", bullet_style))
    story.append(Paragraph("• <b>ViewModel (Estado y Lógica):</b> Manejo del estado reactivo mediante <i>StateFlow</i> (<i>HubViewModel</i>, <i>CazaNotificacionesViewModel</i>, <i>Organizador24hViewModel</i>) ejecutando tareas asíncronas en <i>viewModelScope</i>.", bullet_style))

    # Sección 5: Enlaces y Design System
    story.append(Paragraph("5. Enlaces al Repositorio y Design System", h1_style))
    story.append(Paragraph("• <b>Repositorio Público en GitHub:</b> <font color='#0066CC'><u>https://github.com/edi4518/bienestar-digital-android.git</u></font>", body_style))
    story.append(Paragraph("• <b>Design System Neo-Brutalista Dark:</b> Paleta de colores Canvas Dark (<i>#0F0F14</i>), Cards (<i>#1B1B24</i>), Acento Lima (<i>#CCFF00</i>), Acento Cian (<i>#00F5D4</i>), Acento Rosa (<i>#FF3366</i>) y Danger Red (<i>#F87171</i>). Diseño en Bento Grid con tipografía bold y microinteracciones sonoras.", body_style))

    # Sección 6: Tabla de Integrantes
    story.append(Paragraph("6. Tabla de Integrantes, Roles y Tareas Realizadas", h1_style))

    table_data = [
        [Paragraph("<b>Integrante</b>", body_style), Paragraph("<b>Rol Principal</b>", body_style), Paragraph("<b>Tareas Realizadas</b>", body_style)],
        [Paragraph("<b>Eduardo</b>", body_style), Paragraph("Lead Developer & Creación y desarrollador", body_style), Paragraph("Implementación de Room/SQLite, Firebase Firestore, UsageStatsManager, y Minijuego 1 Decisiones Conectadas.", body_style)],
        [Paragraph("<b>Gonzalo</b>", body_style), Paragraph("Diseñador UI/UX & Creación y desarrollo", body_style), Paragraph("Diseño del Design System Neo-Brutalista, Bento Grid Layouts, prototipo de pantallas, desarrollo de Organizador 24 hs y pantalla de Home.", body_style)],
        [Paragraph("<b>Leandro</b>", body_style), Paragraph("Arquitectura y creación de contenido Minijuego", body_style), Paragraph("Lógica y desarrollo del Minijuego Caza de Notificaciones, pruebas de unidad y desarrollo del diseño de pantalla de creación de perfil.", body_style)],
        [Paragraph("<b>Franco</b>", body_style), Paragraph("Arquitectura y creación de contenido Minijuego", body_style), Paragraph("Desarrollo de Minijuego de Mito o Verdad, desarrollo de lógica de la creación del perfil y pruebas de unidad.", body_style)]
    ]

    t = Table(table_data, colWidths=[1.0*inch, 2.0*inch, 4.2*inch])
    t.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), colors.HexColor('#0F0F14')),
        ('TEXTCOLOR', (0,0), (-1,0), colors.white),
        ('GRID', (0,0), (-1,-1), 0.5, colors.HexColor('#0F0F14')),
        ('VALIGN', (0,0), (-1,-1), 'MIDDLE'),
        ('TOPPADDING', (0,0), (-1,-1), 4),
        ('BOTTOMPADDING', (0,0), (-1,-1), 4),
    ]))
    story.append(t)

    # Sección 7: Instrucciones de Ejecución
    story.append(Paragraph("7. Instrucciones para Ejecutar la Aplicación", h1_style))
    story.append(Paragraph("1. Clonar el repositorio: <code>git clone https://github.com/edi4518/bienestar-digital-android.git</code>", bullet_style))
    story.append(Paragraph("2. Abrir el proyecto en Android Studio (Ladybug / Koala o superior con JDK 17).", bullet_style))
    story.append(Paragraph("3. Verificar que el archivo <code>app/google-services.json</code> esté presente en el módulo app.", bullet_style))
    story.append(Paragraph("4. Compilar y ejecutar en un dispositivo físico o emulador con Android 9.0 (API 28) o superior.", bullet_style))
    story.append(Paragraph("5. Otorgar el permiso de Acceso a Datos de Uso cuando el minijuego Organizador 24h lo solicite para habilitar la auditoría real.", bullet_style))

    doc.build(story)
    print("PDF generado con éxito con color de texto #0F0F14: DOCUMENTACION_ENTREGA_FINAL.pdf")

if __name__ == "__main__":
    generar_pdf()
