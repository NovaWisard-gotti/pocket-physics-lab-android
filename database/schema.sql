-- ============================================================
-- Física de Bolsillo (Pocket Physics Lab) — Esquema de base de datos
-- Motor: SQLite (a través de Room, en el propio dispositivo Android)
-- Todo el almacenamiento es LOCAL. No hay sincronización con ningún servidor.
-- Nunca se guardan muestras de sensor en bruto ni audio: solo resúmenes.
-- ============================================================

PRAGMA foreign_keys = ON;

-- Perfil del pequeño científico o científica.
CREATE TABLE IF NOT EXISTS user_profile (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    name        TEXT    NOT NULL,
    avatarKey   TEXT    NOT NULL,
    createdAt   INTEGER NOT NULL
);

-- Catálogo de experimentos guiados (sembrado una vez, mínimo 15 filas).
CREATE TABLE IF NOT EXISTS sensor_experiment (
    experimentKey   TEXT PRIMARY KEY,
    sensorType      TEXT NOT NULL CHECK (sensorType IN ('ACCELEROMETER','GYROSCOPE','MICROPHONE','LIGHT')),
    labKey          TEXT NOT NULL CHECK (labKey IN ('movimiento','rotacion','sonido','luz')),
    titulo          TEXT NOT NULL,
    pregunta        TEXT NOT NULL,
    ordenSugerido   INTEGER NOT NULL
);

-- Una sesión concreta en la que el usuario realizó un experimento.
CREATE TABLE IF NOT EXISTS experiment_session (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    profileId       INTEGER NOT NULL,
    experimentKey   TEXT NOT NULL,
    fecha           INTEGER NOT NULL,
    durationMs      INTEGER NOT NULL,
    FOREIGN KEY (profileId) REFERENCES user_profile(id) ON DELETE CASCADE,
    FOREIGN KEY (experimentKey) REFERENCES sensor_experiment(experimentKey) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_experiment_session_profile ON experiment_session(profileId);
CREATE INDEX IF NOT EXISTS idx_experiment_session_experiment ON experiment_session(experimentKey);

-- La predicción que el usuario escribe antes de medir (1:1 con la sesión).
CREATE TABLE IF NOT EXISTS prediction (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    sessionId       INTEGER NOT NULL UNIQUE,
    textoPrediccion TEXT NOT NULL,
    FOREIGN KEY (sessionId) REFERENCES experiment_session(id) ON DELETE CASCADE
);

-- Resumen estadístico de la medición: NUNCA las muestras individuales.
CREATE TABLE IF NOT EXISTS measurement_summary (
    id                  INTEGER PRIMARY KEY AUTOINCREMENT,
    sessionId           INTEGER NOT NULL UNIQUE,
    sensorType          TEXT NOT NULL,
    valorMinimo         REAL NOT NULL,
    valorMaximo         REAL NOT NULL,
    valorPromedio       REAL NOT NULL,
    duracionMs          INTEGER NOT NULL,
    muestrasAnalizadas  INTEGER NOT NULL,
    FOREIGN KEY (sessionId) REFERENCES experiment_session(id) ON DELETE CASCADE
);

-- Resultado y conclusión de la sesión (1:1 con la sesión).
CREATE TABLE IF NOT EXISTS experiment_result (
    id                  INTEGER PRIMARY KEY AUTOINCREMENT,
    sessionId           INTEGER NOT NULL UNIQUE,
    prediccionCorrecta  INTEGER NOT NULL CHECK (prediccionCorrecta IN (0,1)),
    textoResultado      TEXT NOT NULL,
    textoConclusion     TEXT NOT NULL,
    FOREIGN KEY (sessionId) REFERENCES experiment_session(id) ON DELETE CASCADE
);

-- Catálogo de desafíos.
CREATE TABLE IF NOT EXISTS challenge (
    challengeKey    TEXT PRIMARY KEY,
    titulo          TEXT NOT NULL,
    descripcion     TEXT NOT NULL,
    sensorType      TEXT NOT NULL,
    badgeKey        TEXT NOT NULL
);

-- Registro de que un perfil completó un desafío.
CREATE TABLE IF NOT EXISTS challenge_completion (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    profileId       INTEGER NOT NULL,
    challengeKey    TEXT NOT NULL,
    sessionId       INTEGER,
    completedAt     INTEGER NOT NULL,
    FOREIGN KEY (profileId) REFERENCES user_profile(id) ON DELETE CASCADE,
    FOREIGN KEY (challengeKey) REFERENCES challenge(challengeKey) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_challenge_completion_profile ON challenge_completion(profileId);
CREATE INDEX IF NOT EXISTS idx_challenge_completion_challenge ON challenge_completion(challengeKey);

-- Catálogo de insignias.
CREATE TABLE IF NOT EXISTS badge (
    badgeKey        TEXT PRIMARY KEY,
    titulo          TEXT NOT NULL,
    descripcion     TEXT NOT NULL,
    iconKey         TEXT NOT NULL
);

-- Insignias efectivamente ganadas por un perfil.
CREATE TABLE IF NOT EXISTS user_badge (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    profileId       INTEGER NOT NULL,
    badgeKey        TEXT NOT NULL,
    earnedAt        INTEGER NOT NULL,
    FOREIGN KEY (profileId) REFERENCES user_profile(id) ON DELETE CASCADE,
    FOREIGN KEY (badgeKey) REFERENCES badge(badgeKey) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_user_badge_profile ON user_badge(profileId);
