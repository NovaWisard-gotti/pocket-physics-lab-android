package com.kidslab.pocketphysics.data.local.seed

import com.kidslab.pocketphysics.data.local.entity.Badge
import com.kidslab.pocketphysics.data.local.entity.Challenge
import com.kidslab.pocketphysics.data.local.entity.SensorExperiment
import com.kidslab.pocketphysics.domain.model.SensorType

/**
 * Datos iniciales (seed) de la base de datos: 17 experimentos guiados
 * (más de los 15 mínimos requeridos), 6 desafíos y 6 insignias.
 * Se insertan una única vez, en la creación de la base de datos.
 */
object SeedData {

    fun experiments(): List<SensorExperiment> = listOf(
        // --- Laboratorio de movimiento (acelerómetro) ---
        SensorExperiment(
            "mov_quieto", SensorType.ACCELEROMETER.name, "movimiento",
            "El teléfono en reposo",
            "¿Qué valores marca el acelerómetro cuando el teléfono está completamente quieto sobre la mesa?",
            1
        ),
        SensorExperiment(
            "mov_lateral", SensorType.ACCELEROMETER.name, "movimiento",
            "Movimiento suave lateral",
            "¿Cómo cambian los valores X e Y al mover el teléfono despacio de izquierda a derecha?",
            2
        ),
        SensorExperiment(
            "mov_pasos", SensorType.ACCELEROMETER.name, "movimiento",
            "Caminar cinco pasos",
            "¿Qué patrón se repite en el acelerómetro al caminar cinco pasos con el teléfono en la mano?",
            3
        ),
        SensorExperiment(
            "mov_mesa_vs_mano", SensorType.ACCELEROMETER.name, "movimiento",
            "Mesa contra mano",
            "¿Hay diferencia entre los valores del acelerómetro al dejar el teléfono en la mesa y al sostenerlo con la mano quieta?",
            4
        ),
        // --- Laboratorio de rotación (giroscopio) ---
        SensorExperiment(
            "rot_quieto", SensorType.GYROSCOPE.name, "rotacion",
            "Sin girar",
            "¿Qué marca el giroscopio cuando el teléfono está completamente quieto?",
            5
        ),
        SensorExperiment(
            "rot_lento", SensorType.GYROSCOPE.name, "rotacion",
            "Giro lento",
            "¿Qué ocurre con la velocidad de rotación al girar el teléfono muy despacio?",
            6
        ),
        SensorExperiment(
            "rot_90", SensorType.GYROSCOPE.name, "rotacion",
            "Giro de 90 grados",
            "¿Cómo se ve en el giroscopio un giro de aproximadamente 90 grados?",
            7
        ),
        SensorExperiment(
            "rot_dos_manos", SensorType.GYROSCOPE.name, "rotacion",
            "Una mano o dos manos",
            "¿Cambia la medición del giroscopio si giras el teléfono con ambas manos en vez de una sola?",
            8
        ),
        // --- Laboratorio de sonido (micrófono) ---
        SensorExperiment(
            "son_silencio", SensorType.MICROPHONE.name, "sonido",
            "El silencio del ambiente",
            "¿Qué amplitud se mide cuando nadie habla y el ambiente está en silencio?",
            9
        ),
        SensorExperiment(
            "son_aplauso", SensorType.MICROPHONE.name, "sonido",
            "Un aplauso suave",
            "¿Qué amplitud produce un aplauso suave dado a una distancia prudente del teléfono?",
            10
        ),
        SensorExperiment(
            "son_hablar", SensorType.MICROPHONE.name, "sonido",
            "Hablar con voz normal",
            "¿Cómo se ve la onda de sonido cuando hablas con tu voz normal?",
            11
        ),
        SensorExperiment(
            "son_sostenido", SensorType.MICROPHONE.name, "sonido",
            "Sonido sostenido",
            "¿Qué forma tiene la onda de un sonido sostenido, como decir 'aaaah'?",
            12
        ),
        SensorExperiment(
            "son_suave_vs_fuerte", SensorType.MICROPHONE.name, "sonido",
            "Suave contra fuerte",
            "¿Cuánto cambia la amplitud entre un sonido suave y uno fuerte?",
            13
        ),
        SensorExperiment(
            "son_grave_vs_agudo", SensorType.MICROPHONE.name, "sonido",
            "Grave contra agudo",
            "¿Cuánto cambia la frecuencia aproximada entre un sonido grave y uno agudo?",
            14
        ),
        // --- Laboratorio de luz (opcional) ---
        SensorExperiment(
            "luz_habitacion", SensorType.LIGHT.name, "luz",
            "Luz de la habitación",
            "¿Cuántos lux aproximados hay en la habitación con la luz encendida?",
            15
        ),
        SensorExperiment(
            "luz_sombra_mano", SensorType.LIGHT.name, "luz",
            "Sombra con la mano",
            "¿Cuánto baja la luz al hacer sombra sobre el sensor con la mano, sin tocarlo ni taparlo del todo?",
            16
        ),
        SensorExperiment(
            "luz_comparar_lugares", SensorType.LIGHT.name, "luz",
            "Comparar dos lugares",
            "¿Cuál es la diferencia de luz entre dos lugares distintos de la misma habitación?",
            17
        )
    )

    fun challenges(): List<Challenge> = listOf(
        Challenge(
            "desafio_mas_quieto", "Encuentra el momento más quieto",
            "Consigue el valor de movimiento más bajo posible dejando el teléfono totalmente en reposo.",
            SensorType.ACCELEROMETER.name, "badge_explorador_quieto"
        ),
        Challenge(
            "desafio_dos_sonidos", "Produce dos sonidos con diferente intensidad",
            "Registra un sonido suave y uno fuerte, y compara sus amplitudes.",
            SensorType.MICROPHONE.name, "badge_dj_de_sonidos"
        ),
        Challenge(
            "desafio_giro_90", "Gira 90° aproximadamente",
            "Gira el teléfono lentamente hasta acercarte a un cuarto de vuelta completa.",
            SensorType.GYROSCOPE.name, "badge_maestro_giro"
        ),
        Challenge(
            "desafio_comparar_luz", "Compara dos niveles de luz",
            "Mide la luz en dos lugares distintos y anota cuál tiene más lux.",
            SensorType.LIGHT.name, "badge_cazador_de_luz"
        ),
        Challenge(
            "desafio_cinco_pasos", "Camina cinco pasos con cuidado",
            "Registra el patrón del acelerómetro al caminar cinco pasos despacio.",
            SensorType.ACCELEROMETER.name, "badge_explorador_quieto"
        ),
        Challenge(
            "desafio_tono", "Compara un sonido grave y uno agudo",
            "Registra dos sonidos de tono distinto y compara su frecuencia aproximada.",
            SensorType.MICROPHONE.name, "badge_dj_de_sonidos"
        )
    )

    fun badges(): List<Badge> = listOf(
        Badge("badge_primer_experimento", "Primer experimento", "Completaste tu primer experimento guiado.", "beaker"),
        Badge("badge_explorador_quieto", "Explorador de la quietud", "Encontraste el momento más quieto con el acelerómetro.", "leaf"),
        Badge("badge_dj_de_sonidos", "DJ de sonidos", "Comparaste sonidos suaves, fuertes, graves y agudos.", "wave"),
        Badge("badge_maestro_giro", "Maestro del giro", "Completaste un giro de aproximadamente 90 grados.", "spin"),
        Badge("badge_cazador_de_luz", "Cazador de luz", "Comparaste dos niveles distintos de luz.", "sun"),
        Badge("badge_cientifico_completo", "Científico completo", "Probaste los cuatro laboratorios de la app.", "star")
    )
}
