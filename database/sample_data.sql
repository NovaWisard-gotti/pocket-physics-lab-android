-- ============================================================
-- Física de Bolsillo — Datos de ejemplo (para pruebas manuales, no producción)
-- ============================================================

INSERT INTO user_profile (id, name, avatarKey, createdAt) VALUES
    (1, 'Ada', 'atomo', 1735689600000);

INSERT INTO sensor_experiment (experimentKey, sensorType, labKey, titulo, pregunta, ordenSugerido) VALUES
    ('mov_quieto', 'ACCELEROMETER', 'movimiento', 'El teléfono en reposo', '¿Qué valores marca el acelerómetro cuando el teléfono está completamente quieto sobre la mesa?', 1),
    ('mov_lateral', 'ACCELEROMETER', 'movimiento', 'Movimiento suave lateral', '¿Cómo cambian los valores X e Y al mover el teléfono despacio de izquierda a derecha?', 2),
    ('rot_90', 'GYROSCOPE', 'rotacion', 'Giro de 90 grados', '¿Cómo se ve en el giroscopio un giro de aproximadamente 90 grados?', 7),
    ('son_aplauso', 'MICROPHONE', 'sonido', 'Un aplauso suave', '¿Qué amplitud produce un aplauso suave dado a una distancia prudente del teléfono?', 10),
    ('luz_habitacion', 'LIGHT', 'luz', 'Luz de la habitación', '¿Cuántos lux aproximados hay en la habitación con la luz encendida?', 15);

INSERT INTO experiment_session (id, profileId, experimentKey, fecha, durationMs) VALUES
    (1, 1, 'mov_quieto', 1735690000000, 5000),
    (2, 1, 'son_aplauso', 1735690500000, 3000);

INSERT INTO prediction (sessionId, textoPrediccion) VALUES
    (1, 'Creo que el acelerómetro marcará casi cero cambio'),
    (2, 'Creo que el aplauso hará subir mucho la amplitud');

INSERT INTO measurement_summary (sessionId, sensorType, valorMinimo, valorMaximo, valorPromedio, duracionMs, muestrasAnalizadas) VALUES
    (1, 'ACCELEROMETER', 9.75, 9.90, 9.81, 5000, 120),
    (2, 'MICROPHONE', 120, 8200, 1450, 3000, 24);

INSERT INTO experiment_result (sessionId, prediccionCorrecta, textoResultado, textoConclusion) VALUES
    (1, 1, 'El teléfono se mantuvo prácticamente quieto: la magnitud varió muy poco alrededor de la gravedad.', 'Promedio de la sesión: 9.81 m/s² en 120 muestras.'),
    (2, 1, 'Amplitud promedio: 1450. Frecuencia dominante aproximada: 850 Hz.', 'Recuerda: esto es una comparación educativa, no una medición de laboratorio profesional.');

INSERT INTO challenge (challengeKey, titulo, descripcion, sensorType, badgeKey) VALUES
    ('desafio_mas_quieto', 'Encuentra el momento más quieto', 'Consigue el valor de movimiento más bajo posible dejando el teléfono totalmente en reposo.', 'ACCELEROMETER', 'badge_explorador_quieto'),
    ('desafio_dos_sonidos', 'Produce dos sonidos con diferente intensidad', 'Registra un sonido suave y uno fuerte, y compara sus amplitudes.', 'MICROPHONE', 'badge_dj_de_sonidos');

INSERT INTO challenge_completion (profileId, challengeKey, sessionId, completedAt) VALUES
    (1, 'desafio_mas_quieto', 1, 1735690100000);

INSERT INTO badge (badgeKey, titulo, descripcion, iconKey) VALUES
    ('badge_explorador_quieto', 'Explorador de la quietud', 'Encontraste el momento más quieto con el acelerómetro.', 'leaf'),
    ('badge_dj_de_sonidos', 'DJ de sonidos', 'Comparaste sonidos suaves, fuertes, graves y agudos.', 'wave');

INSERT INTO user_badge (profileId, badgeKey, earnedAt) VALUES
    (1, 'badge_explorador_quieto', 1735690100000);
