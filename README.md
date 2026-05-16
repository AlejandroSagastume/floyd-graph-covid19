# FLOYD GRAFO COVID-19

Calculadora de rutas más cortas entre ciudades de Guatemala usando Floyd-Warshall en Java y Python.

## Descripción

Este proyecto implementa:
- **ADT Grafo** (Grafo Dirigido Ponderado) con Matriz de Adyacencia
- **Algoritmo Floyd-Warshall** para calcular rutas más cortas entre todos los pares de vértices
- **Cálculo del Centro del Grafo** basado en excentricidades
- **Dos implementaciones equivalentes:**
  - `Java` - Con ArrayList y matrices bidimensionales
  - `Python` - Con NetworkX
- **Programa interactivo** con menú para consultar rutas y modificar el grafo dinámicamente
- **Matriz de Adyacencia** mostrada en bloques para mejor visualización
- Operadores soportados: agregar/eliminar arcos, consultar rutas, identificar centro del grafo
- Manejo de errores: ciudades no encontradas, rutas inválidas, entrada inválida

## Estructura del Proyecto
```text
FLOYD-GRAPH-COVID19/
├── README.md
├── .gitignore
├── demo/                           ← TRABAJAR SIEMPRE DESDE AQUÍ (Java)
│   ├── pom.xml
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/io/
│   │   │   │       ├── Main.java
│   │   │   │       ├── Grafo.java
│   │   │   │       ├── Floyd.java
│   │   │   │       ├── CentroGrafo.java
│   │   │   │       └── LectorGrafo.java
│   │   │   └── resources/
│   │   │       └── grafo.txt
│   │   └── test/
│   │       └── java/
│   │           └── com/io/
│   │               ├── GrafoTest.java
│   │               ├── FloydTest.java
│   │               └── CentroGrafoTest.java
│   └── target/
│       └── classes/
├─ Python/                         ← TRABAJAR DESDE AQUÍ (Python)
   ├── main.py
   ├── floyd_grafo.py
   ├── centro_grafo.py
   ├── requirements.txt
   └── grafo.txt

```

## Requisitos

- **Java 17 o superior**
- **Maven 3.6+** (recomendado)
- **Python 3.8 o superior**
- **NetworkX 3.2**

## Instalación

### 1. Clonar el repositorio
```bash
git clone https://github.com/AlejandroSagastume/floyd-graph-covid19
cd floyd-graph-covid19
```

### 2. Verificar instalación de Java

```bash
java -version
```

Debe mostrar Java 17 o superior.

### 3. Instalación de Maven (Java)

```bash
cd demo
mvn clean install
```

### 4. Instalación de dependencias (Python)

```bash
cd python
pip install -r requirements.txt
```

## Compilación y Ejecución

### Opción 1: Java con Maven (Recomendado)

1. Ve a la carpeta demo

```bash
cd demo
```

2. Compila el proyecto

```bash
mvn clean compile
```

3. Ejecuta el programa

```bash
mvn exec:java -Dexec.mainClass="com.io.Main"
```

### Opción 2: Java sin Maven

1. Ve a la carpeta demo

2. Compila el código:

```bash
javac -d out src/main/java/com/io/*.java
```

3. Ejecuta el programa:

```bash
java -cp out com.io.Main
```

**Nota:** La opción sin Maven puede no funcionar correctamente si las dependencias no han sido descargadas previamente.

### Opción 3: Python

1. Ve a la carpeta python

```bash
cd python
```

2. Ejecuta el programa

```bash
python main.py
```

## Archivo de Datos

El programa lee conexiones desde `grafo.txt`

**Formato:**
- Ciudad origen, ciudad destino y distancia separados por espacios
- Una conexión por línea
- Las líneas que comienzan con `#` son comentarios

**Ejemplo:**
```text
Guatemala Mixco 15
Mixco Guatemala 15
Mixco Antigua 30
Antigua Mixco 30
Antigua Escuintla 25
Escuintla Antigua 25
Escuintla SantaLucia 15
SantaLucia Escuintla 15
```

## Menú Interactivo

El programa permite:

- Consultar la ruta más corta entre ciudades
- Mostrar la matriz de adyacencia
- Agregar conexiones
- Eliminar conexiones
- Calcular el centro del grafo
- Modificar el grafo dinámicamente

## Cambiar Implementación

El programa permite ejecutar tanto la versión en Java como la versión en Python.

### Método 1: En Main.java

Agrega al inicio del método main:

```java
// Para usar versión Java
System.out.println("Versión Java cargada");

// Para usar versión Python
System.out.println("Versión Python cargada");
```

### Método 2: Desde línea de comandos

```bash
# Java
cd demo && mvn exec:java -Dexec.mainClass="com.io.Main"

# Python
cd python && python main.py
```

Después de cambiar, recompila:

```bash
cd demo
mvn clean install
cd ..
java -cp demo/target/classes com.io.Main
```

## Matriz de Adyacencia

El programa genera automáticamente un archivo `matriz_adyacencia.txt` en la raíz del proyecto con:

- **Tabla de ciudades** con todas las rutas calculadas
- **Distancias resaltadas** en diferentes colores
- **Resumen estadístico** (total, exitosos, errores)
- **Fecha y hora** de generación
- **Información del curso** y autores

**Ubicación del archivo generado:**

```text
FLOYD-GRAPH-COVID19/
└── matriz_adyacencia.txt
```

## Pruebas JUnit

Ejecutar todas las pruebas:

```bash
cd demo
mvn test
```

**Pruebas incluidas:**
- `GrafoTest.java` - Pruebas del ADT Grafo
- `FloydTest.java` - Pruebas del algoritmo Floyd-Warshall
- `CentroGrafoTest.java` - Pruebas del cálculo del centro del grafo

**Total:** 44 pruebas unitarias

## Dependencias

El proyecto utiliza las siguientes librerías (gestionadas automáticamente por Maven):

- **JUnit 5.9.2** - Framework de pruebas
- **Maven Exec Plugin 3.0.0** - Ejecución de clases Java
- **NetworkX 3.2** - Librería de grafos para Python

## Complejidad del Algoritmo

- **Floyd-Warshall:** O(n³) donde `n` es el número de vértices
- **Cálculo del Centro:** O(n²)
- **Consulta de Ruta:** O(1) usando matriz precalculada

## Solución de Problemas

### Error: "cannot find symbol" al compilar

**Solución:** Asegúrate de compilar con Maven para descargar las dependencias:

```bash
cd demo
mvn clean install
```

### El programa no se ejecuta

1. Verifica que compilaste con Maven: `mvn clean install`
2. Verifica que las dependencias se descargaron: `mvn dependency:tree`
3. Ejecuta desde la raíz del proyecto

### Error en Python: ModuleNotFoundError

**Solución:** Instala las dependencias necesarias:

```bash
cd python
pip install -r requirements.txt
```

## Tecnologías Utilizadas

- **Java 17** - Lenguaje de programación principal
- **Python 3.8+** - Lenguaje alternativo
- **Maven** - Gestión de dependencias y construcción
- **JUnit 5** - Pruebas unitarias
- **NetworkX** - Librería de grafos para Python
- **Git/GitHub** - Control de versiones

## Implementaciones de Grafo

### Grafo (Java)

- Usa `Map<String, Integer>` y `ArrayList<String>` internamente
- Matriz de adyacencia implementada con `double[][]`

### Floyd-Grafo (Python)

- Usa `NetworkX DiGraph` internamente
- Cálculo optimizado mediante algoritmos de grafos

Ambas implementan la misma interfaz conceptual, permitiendo intercambiarlas sin modificar la lógica de negocio mediante un patrón de diseño consistente.

## Autores

- **Alejandro Sagastume** - sag25257@uvg.edu.gt

## Curso

**CC2003 - Sección 20 - Algoritmos y Estructura de Datos**  
Universidad del Valle de Guatemala  
Hoja de Trabajo No. 9

