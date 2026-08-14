---
title: "Física de Bolsillo — Memoria Descriptiva"
subtitle: "Pocket Physics Lab · versión 1.0.0"
lang: es
---

# Memoria Descriptiva

## 1. Presentación del proyecto

**Física de Bolsillo** (nombre técnico: *Pocket Physics Lab*) es una
aplicación educativa para Android dirigida a niñas y niños de **8 a 12
años**, cuyo objetivo es transformar el teléfono o la tableta familiar en un
pequeño laboratorio para explorar fenómenos físicos reales usando los
sensores que ya trae el dispositivo: acelerómetro, giroscopio, micrófono y,
si existe, sensor de luz.

La aplicación es **100 % offline**: no requiere conexión a Internet en
ningún momento, no envía datos a ningún servidor y todo lo que el niño o
niña mide y guarda permanece únicamente en su propio dispositivo.

- Nombre visible: **Física de Bolsillo**
- Identificador técnico: `com.kidslab.pocketphysics`
- Versión: **1.0.0**
- Idioma: español
- Edad recomendada: 8 a 12 años

## 2. Objetivo educativo

El propósito de la app es que el niño o niña experimente el **método
científico simplificado** en cada actividad:

1. **Pregunta** — se plantea algo que se puede observar con un sensor.
2. **Predicción** — el niño o niña escribe qué cree que va a pasar.
3. **Medición** — se usa el sensor correspondiente para observar la realidad.
4. **Resultado** — se muestra lo que efectivamente ocurrió.
5. **Conclusión** — una frase corta que conecta la medición con una idea física sencilla.

Este ciclo se repite en los 17 experimentos guiados incluidos en la
versión 1.0.0, agrupados en cuatro laboratorios: **movimiento**,
**rotación**, **sonido** y **luz**.

## 3. Seguridad física: principio rector del diseño

Como la app pide al niño o niña interactuar físicamente con el
dispositivo, el diseño excluye deliberadamente **cualquier experimento que
implique riesgo físico**. En ningún lugar de la aplicación, la
documentación o las instrucciones se sugiere:

- lanzar el teléfono al aire;
- dejarlo caer intencionalmente;
- mojarlo o exponerlo a líquidos;
- acercarlo al fuego o a fuentes de calor;
- conectarlo a electricidad de forma insegura;
- usarlo sobre una bicicleta u otro vehículo en movimiento;
- colocarlo en una carretera o zona de tránsito;
- subirlo a alturas peligrosas;
- golpearlo contra superficies.

Todos los experimentos se diseñaron para realizarse **sentado, caminando
despacio, o con el dispositivo apoyado con cuidado sobre una superficie
estable**. Los movimientos pedidos son siempre suaves (mover el teléfono
lateralmente, girarlo lentamente, caminar unos pocos pasos).

## 4. Sensores utilizados

| Sensor | Qué mide | Disponibilidad |
|---|---|---|
| Acelerómetro | Aceleración en los ejes X, Y, Z (m/s²) | Presente en casi todos los teléfonos Android |
| Giroscopio | Velocidad angular en los ejes X, Y, Z (rad/s) | Presente en la mayoría de los teléfonos de gama media/alta |
| Micrófono | Amplitud y frecuencia aproximada del sonido | Presente en todos los teléfonos con capacidad de llamada |
| Sensor de luz | Iluminación ambiente en lux | **Opcional**: no todos los dispositivos lo incluyen |

La aplicación **detecta en tiempo real** qué sensores existen en el
dispositivo del usuario. Si un sensor no está disponible, la pantalla
correspondiente muestra el mensaje *"Este dispositivo no tiene este
sensor"* y ofrece continuar con otro laboratorio que sí tenga sensor
disponible, en vez de bloquear la experiencia.

## 5. Permisos y privacidad

Física de Bolsillo solicita **un único permiso sensible**: `RECORD_AUDIO`,
necesario exclusivamente para el laboratorio de sonido. Este permiso:

- se solicita únicamente al entrar al laboratorio de sonido, nunca antes;
- se explica en pantalla, en español sencillo, antes de pedirlo al sistema;
- puede rechazarse sin que el resto de la app deje de funcionar.

El manifiesto de la aplicación **no declara el permiso de Internet**, por
lo que Android impide técnicamente que la app se conecte a la red. No hay
cámara, no hay ubicación, no hay contactos, no hay ningún otro dato
personal recolectado.

El audio capturado por el micrófono se procesa **en memoria** (amplitud,
forma de onda simplificada, frecuencia dominante aproximada) y se descarta
inmediatamente después. La aplicación nunca escribe un archivo de audio en
el disco del dispositivo.

## 6. Limitaciones de precisión

Los sensores de un teléfono de consumo **no son instrumentos científicos
calibrados**. Física de Bolsillo lo comunica explícitamente en varias
pantallas con el siguiente aviso:

> "Los sensores de un teléfono sirven para aprender y comparar, pero no
> sustituyen instrumentos científicos calibrados."

En particular:

- El acelerómetro y el giroscopio tienen ruido y una deriva (*drift*) que
  se acumula con el tiempo, especialmente al estimar ángulos por
  integración.
- El micrófono no está calibrado en decibelios; la app solo compara
  amplitudes relativas (más fuerte/más suave), no mide en dB oficiales.
- El sensor de luz varía mucho entre fabricantes y no equivale a un
  luxómetro profesional.

La app nunca afirma dar mediciones profesionalmente precisas: su objetivo
es despertar curiosidad científica y enseñar a comparar y razonar sobre
datos, no sustituir equipamiento de laboratorio.

## 7. Persistencia de datos

La aplicación guarda en una base de datos local (Room/SQLite) únicamente
**resúmenes** de cada sesión: valores mínimo, máximo, promedio y duración,
junto con la predicción escrita por el usuario y la conclusión mostrada.
Nunca se almacenan las muestras individuales de sensor (que pueden llegar a
cientos por segundo) ni el audio capturado. Ver
[`docs/BASE_DE_DATOS.md`](BASE_DE_DATOS.md) para el detalle completo del
esquema.

## 8. Alcance de la versión 1.0.0

- 9 pantallas: perfil, mis sensores, 4 laboratorios, experimentos guiados,
  registro científico y desafíos/logros.
- 17 experimentos guiados (mínimo pedido: 15).
- 6 desafíos y 6 insignias.
- Arquitectura MVVM con Jetpack Compose, Room, Coroutines/Flow.
- Pruebas unitarias ejecutables sin hardware real (JVM + Robolectric).
