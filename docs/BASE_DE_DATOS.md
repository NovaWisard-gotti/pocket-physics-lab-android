# Base de datos — Física de Bolsillo

## 1. Motor y alcance

Física de Bolsillo usa **Room** sobre **SQLite**, guardado únicamente en el
almacenamiento local del dispositivo. No existe ningún servidor, ninguna
sincronización ni ninguna copia en la nube: todo vive en el teléfono o
tableta del niño o niña, y se borra si se desinstala la app.

**Principio de diseño clave:** la base de datos guarda *resúmenes*, nunca
datos crudos de sensor ni audio. Un experimento de movimiento puede generar
cientos de lecturas por segundo del acelerómetro, pero solo se guarda una
fila con mínimo, máximo, promedio y duración. Esto evita que la base de
datos crezca sin control y respeta la privacidad del usuario.

## 2. Diagrama entidad-relación (Mermaid)

```mermaid
erDiagram
    USER_PROFILE ||--o{ EXPERIMENT_SESSION : "realiza"
    USER_PROFILE ||--o{ CHALLENGE_COMPLETION : "completa"
    USER_PROFILE ||--o{ USER_BADGE : "gana"

    SENSOR_EXPERIMENT ||--o{ EXPERIMENT_SESSION : "es el tipo de"

    EXPERIMENT_SESSION ||--o| PREDICTION : "tiene"
    EXPERIMENT_SESSION ||--o| MEASUREMENT_SUMMARY : "tiene"
    EXPERIMENT_SESSION ||--o| EXPERIMENT_RESULT : "tiene"
    EXPERIMENT_SESSION ||--o{ CHALLENGE_COMPLETION : "puede cumplir"

    CHALLENGE ||--o{ CHALLENGE_COMPLETION : "se completa en"
    CHALLENGE ||--|| BADGE : "otorga"
    BADGE ||--o{ USER_BADGE : "se gana como"

    USER_PROFILE {
        long id PK
        string name
        string avatarKey
        long createdAt
    }

    SENSOR_EXPERIMENT {
        string experimentKey PK
        string sensorType
        string labKey
        string titulo
        string pregunta
        int ordenSugerido
    }

    EXPERIMENT_SESSION {
        long id PK
        long profileId FK
        string experimentKey FK
        long fecha
        long durationMs
    }

    PREDICTION {
        long id PK
        long sessionId FK
        string textoPrediccion
    }

    MEASUREMENT_SUMMARY {
        long id PK
        long sessionId FK
        string sensorType
        float valorMinimo
        float valorMaximo
        float valorPromedio
        long duracionMs
        int muestrasAnalizadas
    }

    EXPERIMENT_RESULT {
        long id PK
        long sessionId FK
        boolean prediccionCorrecta
        string textoResultado
        string textoConclusion
    }

    CHALLENGE {
        string challengeKey PK
        string titulo
        string descripcion
        string sensorType
        string badgeKey FK
    }

    CHALLENGE_COMPLETION {
        long id PK
        long profileId FK
        string challengeKey FK
        long sessionId FK
        long completedAt
    }

    BADGE {
        string badgeKey PK
        string titulo
        string descripcion
        string iconKey
    }

    USER_BADGE {
        long id PK
        long profileId FK
        string badgeKey FK
        long earnedAt
    }
```

## 3. Descripción de las tablas

| Tabla | Propósito |
|---|---|
| `user_profile` | El perfil del pequeño científico o científica (nombre y avatar). |
| `sensor_experiment` | Catálogo fijo de los 17 experimentos guiados (sembrado una vez). |
| `experiment_session` | Cada vez que el usuario ejecuta un experimento. |
| `prediction` | Lo que el usuario predijo antes de medir. |
| `measurement_summary` | Mínimo, máximo, promedio y duración — nunca las muestras crudas. |
| `experiment_result` | El resultado y la conclusión mostrados al usuario. |
| `challenge` | Catálogo fijo de desafíos. |
| `challenge_completion` | Qué desafíos completó cada perfil, y cuándo. |
| `badge` | Catálogo fijo de insignias. |
| `user_badge` | Qué insignias ganó cada perfil, y cuándo. |

## 4. Por qué no se guardan muestras individuales

Un experimento de movimiento a 50 Hz durante 10 segundos genera 500 lecturas
de X/Y/Z. Guardar cada una inflaría la base de datos rápidamente sin
aportar valor educativo adicional: lo que le importa al niño o niña es el
resumen (¿se movió mucho o poco?, ¿fue más fuerte o más suave?). Por eso
`measurement_summary` guarda solo cuatro números agregados por sesión.

## 5. Privacidad del audio

El laboratorio de sonido analiza el audio del micrófono **en memoria** y
descarta cada bloque inmediatamente después de calcular su amplitud y
frecuencia dominante. Ninguna tabla de esta base de datos contiene una
columna de audio, ni existe ningún archivo de audio guardado en el
dispositivo.

## 6. Archivos relacionados

- [`database/schema.sql`](../database/schema.sql) — sentencias `CREATE TABLE` completas.
- [`database/sample_data.sql`](../database/sample_data.sql) — datos de ejemplo para pruebas manuales.
