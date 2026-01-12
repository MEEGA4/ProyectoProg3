# DEUSTOFILM - Sistema de Gestión de Videoclub

Sistema completo de gestión para videoclub digital con interfaz gráfica moderna

---

## DESCRIPCIÓN

DEUSTOFILM es una aplicación de escritorio desarrollada en Java que implementa un sistema completo de gestión para un videoclub digital. Utiliza arquitectura MVC (Modelo-Vista-Controlador) con interfaz gráfica Swing y base de datos SQLite.

### CARACTERÍSTICAS PRINCIPALES

- Sistema de autenticación dual (Clientes y Trabajadores)
- Gestión completa de películas y series
- Perfiles múltiples por cliente (hasta 5 perfiles)
- **Planificador de Maratón con algoritmos recursivos** (combinaciones y variaciones)
- Reproducción de trailers de películas/series
- Interfaz moderna con tema oscuro y detalles dorados
- Base de datos SQLite con persistencia de datos
- Carga masiva de datos desde archivos CSV
- Operaciones CRUD completas para todas las entidades
- Sistema de búsqueda y filtrado en tablas
- **Perfil de trabajador con visualización de datos**
- **Carrusel de imágenes animado en panel de trabajador**
- Barra de progreso animada con pausado/reanudación

---

## TECNOLOGÍAS UTILIZADAS

- **Java JDK 11 o superior**: Lenguaje de programación principal
- **Swing**: Framework para interfaz gráfica
- **SQLite**: Base de datos embebida
- **JDBC**: Conectividad con base de datos (sqlite-jdbc-3.50.3.0.jar)
- **SLF4J**: Sistema de logging (slf4j-api-2.0.17.jar, slf4j-simple-2.0.17.jar)
- **CSV**: Formato de carga de datos inicial

---

## REQUISITOS DEL SISTEMA

- Sistema Operativo: Windows 10/11, macOS 10.14+, Linux (Ubuntu 18.04+)
- Java JDK: Versión 11 o superior
- Memoria RAM: Mínimo 4 GB (8 GB recomendado)
- Espacio en Disco: Mínimo 500 MB libres
- Resolución: Mínimo 1280x720 píxeles

### VERIFICAR INSTALACIÓN DE JAVA

```
java -version
```

Deberías ver: java version "11.0.x" o superior

Si no tienes Java instalado, descárgalo desde:
- Oracle Java: https://www.oracle.com/java/technologies/downloads/
- OpenJDK: https://openjdk.org/

---

## ESTRUCTURA DEL PROYECTO

```
ProyectoDeustofilm/
├── src/
│   ├── db/
│   │   └── GestorBD.java                # Gestor de base de datos SQLite
│   ├── domain/
│   │   ├── Persona.java                 # Clase base para usuarios
│   │   ├── Cliente.java                 # Extensión para clientes
│   │   ├── Trabajador.java              # Extensión para trabajadores
│   │   ├── PerfilCliente.java           # Perfil visual del cliente (color, imagen)
│   │   ├── PerfilSecundario.java        # Perfiles adicionales por cliente
│   │   ├── Producto.java                # Clase base para contenido
│   │   ├── Pelicula.java                # Extensión para películas
│   │   └── Serie.java                   # Extensión para series
│   ├── gui/
│   │   ├── VentanaSeleccionar.java      # Selección de tipo de usuario
│   │   ├── VentanaCliente.java          # Panel principal del cliente
│   │   ├── VentanaTrabajador.java       # Panel principal del trabajador
│   │   ├── VentanaPeliculasSeries.java  # Catálogo de contenido
│   │   ├── VentanaMaraton.java          # Planificador de maratón (recursivo)
│   │   ├── VentanaPerfilTrabajador.java # Visualización perfil trabajador
│   │   ├── ProgressBar.java             # Barra de progreso animada
│   │   ├── AdminTableStyler.java        # Estilizador de tablas admin
│   │   ├── VentanaClientesTabla.java    # CRUD de clientes
│   │   ├── VentanaPeliculasTabla.java   # CRUD de películas
│   │   ├── VentanaSeriesTabla.java      # CRUD de series
│   │   ├── VentanaProductosTabla.java   # Vista combinada productos
│   │   ├── ClienteTableModel.java       # Modelo tabla clientes
│   │   ├── PeliculaTableModel.java      # Modelo tabla películas
│   │   ├── PeliculaTablaTableModel.java # Modelo tabla películas admin
│   │   ├── SerieTableModel.java         # Modelo tabla series
│   │   ├── SerieTablaTableModel.java    # Modelo tabla series admin
│   │   └── ProductoTableModel.java      # Modelo tabla productos
│   └── main/
│       └── Main.java                    # Punto de entrada
├── resources/
│   ├── db/
│   │   └── videoclub.db                 # Base de datos SQLite
│   ├── data/
│   │   ├── clientes.csv                 # Datos iniciales clientes
│   │   ├── trabajadores.csv             # Datos iniciales trabajadores
│   │   ├── peliculas.csv                # Datos iniciales películas
│   │   └── series.csv                   # Datos iniciales series
│   ├── images/
│   │   ├── logo.png                     # Logo de la aplicación
│   │   └── [películas/series].jpeg      # Portadas del contenido
│   ├── videos/
│   │   └── trailer_ejemplo.mp4          # Trailers de películas/series
│   └── lib/
│       ├── sqlite-jdbc-3.50.3.0.jar     # Driver SQLite
│       ├── slf4j-api-2.0.17.jar         # API de logging
│       └── slf4j-simple-2.0.17.jar      # Implementación logging
└── bin/                                 # Clases compiladas
```

---

## INSTALACIÓN Y EJECUCIÓN

### CLONAR EL REPOSITORIO

```
git clone https://github.com/MEEGA4/ProyectoProg3.git
cd ProyectoProg3
```

### COMPILAR (LÍNEA DE COMANDOS)

```
javac -d bin -sourcepath src -cp "resources/lib/*" src/main/Main.java
```

### EJECUTAR

```
java -cp "bin;resources/lib/*" main.Main
```

### USANDO UN IDE

Si usas Eclipse o IntelliJ IDEA:
1. Abre el proyecto
2. Añade las librerías de `resources/lib/` al classpath
3. Ejecuta la clase Main.java

---

## USO DE LA APLICACIÓN

### PRIMERA EJECUCIÓN

Al ejecutar por primera vez:
1. Se inicializa la conexión con SQLite
2. Se crean todas las tablas (incluyendo PERFIL_SECUNDARIO)
3. Se cargan datos desde CSV (si está habilitado en Main.java)
4. Aparece la ventana de selección de usuario

### DATOS DE PRUEBA

Usuarios de ejemplo (si has cargado los archivos CSV):

CLIENTES:
- Usuario: Juan | Contraseña: pass123
- Usuario: María | Contraseña: secure456

TRABAJADORES:
- Usuario: Carlos | Contraseña: worker789
- Usuario: Ana | Contraseña: admin2024

---

## FUNCIONALIDADES POR ROL

### CLIENTE

**GESTIÓN DE PERFILES:**
- Crear hasta 5 perfiles por cuenta
- Editar nombre, color o imagen de perfil
- Eliminar perfiles (excepto el principal)
- Los perfiles se guardan en la base de datos (tabla PERFIL_SECUNDARIO)

**CATÁLOGO DE CONTENIDO:**
- Navegar por TODO, PELÍCULAS o SERIES
- Carrusel con 6 elementos por fila con scroll infinito
- Visualización de portadas con efecto hover dorado
- **Click para reproducir trailer** (abre reproductor del sistema)

**PLANIFICADOR DE MARATÓN:**
- Accesible desde el botón "PLANIFICAR MARATÓN"
- Configurar tiempo disponible (horas y minutos)
- Filtrar por tipo (películas, series, o ambos) y género
- Seleccionar número de títulos a ver
- **Algoritmos recursivos:**
  - Combinaciones sin repetición
  - Variaciones con repetición
- Ordenar resultados por tiempo sobrante, duración o alfabéticamente
- Configurar episodios por serie (45 min cada uno)

### TRABAJADOR (ADMINISTRADOR)

**PANEL PRINCIPAL:**
- Gestionar Clientes - CRUD completo
- Gestionar Películas - CRUD completo
- Gestionar Series - CRUD completo
- Todos los Productos - Vista combinada
- **Carrusel de imágenes animado** (cambia cada 5 segundos)
- **Botón MI PERFIL** para ver datos del trabajador

**PERFIL DE TRABAJADOR:**
- Visualizar nombre completo
- Ver edad, ubicación, puesto y salario
- Diseño modal consistente con el tema

**OPERACIONES DISPONIBLES:**
- Añadir nuevos registros
- Buscar y filtrar datos en tiempo real
- Eliminar registros (individual o múltiple)
- Ordenar por columnas (click en encabezado)

---

## ATAJOS DE TECLADO

### GESTIÓN DE CLIENTES

- CTRL + A: Añadir nuevo cliente
- CTRL + B: Eliminar cliente(s) seleccionado(s)

### GESTIÓN DE PELÍCULAS Y SERIES

- CTRL + K: Añadir nueva película/serie
- CTRL + T: Eliminar película/serie seleccionada

### GESTIÓN DE PRODUCTOS

- CTRL + A: Añadir nuevo producto
- CTRL + B: Eliminar producto(s) seleccionado(s)

### ATAJOS GLOBALES

- ESC: Cerrar ventana actual
- ALT + F4: Cerrar aplicación

---

## BASE DE DATOS

### TABLAS PRINCIPALES

**PERSONA:**
- Almacena datos comunes de usuarios
- Campos: id, nombre, apellido, edad, contraseña, ubicación

**CLIENTE:**
- Hereda de Persona (relación por ID)
- Campos adicionales: teléfono, email

**TRABAJADOR:**
- Hereda de Persona (relación por ID)
- Campos adicionales: puesto, salario

**PERFIL_SECUNDARIO:** (NUEVO)
- Perfiles adicionales asociados a clientes
- Campos: id, nombre, clienteId (FK a CLIENTE)
- Permite hasta 4 perfiles secundarios por cliente

**PRODUCTO:**
- Clase base para contenido multimedia
- Campos: id, nombre, descripción, precio, stock

**PELICULA:**
- Hereda de Producto (relación por ID)
- Campos adicionales: director, género, duración

**SERIE:**
- Hereda de Producto (relación por ID)
- Campos adicionales: género, temporadas, episodios

### OPERACIONES CRUD DE PERFILES

El sistema permite:
- **insertarPerfilSecundario()**: Crear nuevo perfil
- **obtenerPerfilesSecundarios(clienteId)**: Cargar perfiles del cliente
- **actualizarPerfilSecundario()**: Modificar nombre del perfil
- **eliminarPerfilSecundario()**: Borrar perfil de la BD

### REINICIAR BASE DE DATOS

1. Cerrar la aplicación
2. Eliminar el archivo: resources/db/videoclub.db
3. Descomentar en Main.java las líneas:
   - gestor.cargarClientesDesdeCSV(...)
   - gestor.cargarTrabajadoresDesdeCSV(...)
   - gestor.cargarPeliculasDesdeCSV(...)
   - gestor.cargarSeriesDesdeCSV(...)
4. Ejecutar la aplicación
5. Volver a comentar las líneas

ADVERTENCIA: Esto borrará todos los datos permanentemente.

---

## PLANIFICADOR DE MARATÓN

### DESCRIPCIÓN

El Planificador de Maratón permite crear combinaciones óptimas de películas y series para ver en un tiempo determinado. Utiliza **algoritmos recursivos** para calcular todas las posibilidades.

### ALGORITMOS IMPLEMENTADOS

**COMBINACIONES SIN REPETICIÓN:**
```java
generarCombinacionesRecursivo(items, n, tiempoMax, inicio, combinacionActual)
```
- Genera todas las formas de seleccionar n elementos sin repetir
- Usa backtracking para explorar todas las opciones
- Poda ramas que exceden el tiempo máximo

**VARIACIONES CON REPETICIÓN:**
```java
generarVariacionesRecursivo(items, n, tiempoMax, variacionActual)
```
- Permite seleccionar el mismo título múltiples veces
- Útil para maratones de una serie específica

### OPCIONES DE CONFIGURACIÓN

| Opción | Descripción |
|--------|-------------|
| Tiempo disponible | Horas y minutos para el maratón |
| Tipo de contenido | Todo, Solo Películas, Solo Series |
| Género preferido | Filtrar por género específico o todos |
| Número de títulos | Cantidad fija de items a seleccionar (1-10) |
| Episodios por serie | Cuántos episodios ver de cada serie |
| Permitir repetidos | Combinaciones vs Variaciones |
| Ordenar por | Tiempo sobrante, duración, alfabético |

### LÍMITES

- Máximo 500 combinaciones mostradas para evitar sobrecarga
- Cálculo asíncrono (SwingWorker) para no bloquear la UI

---

## REPRODUCCIÓN DE TRAILERS

### FUNCIONAMIENTO

Al hacer clic en una portada de película/serie en el catálogo:
1. Se busca el archivo `resources/videos/[nombre].mp4`
2. Si no existe, se usa `resources/videos/trailer_ejemplo.mp4`
3. Se abre con el reproductor predeterminado del sistema
4. La reproducción es en un hilo separado (no bloquea la UI)

### AÑADIR TRAILERS

1. Nombrar el archivo MP4 igual que el nombre del producto
2. Colocarlo en `resources/videos/`
3. Ejemplo: Para "Batman", crear `resources/videos/Batman.mp4`

---

## RESOLUCIÓN DE PROBLEMAS

### LA APLICACIÓN NO ARRANCA

Soluciones:
- Verificar que Java esté instalado: java -version
- Asegurarse de tener JDK 11 o superior
- Comprobar que las librerías estén en resources/lib/
- Verificar permisos de lectura/escritura en videoclub.db
- Revisar que todas las librerías estén en el classpath

### NO PUEDO INICIAR SESIÓN

Soluciones:
- Verificar que hayas cargado los datos desde CSV
- Comprobar que usuario y contraseña sean correctos (sensible a mayúsculas)
- Revisar la consola para mensajes de error de conexión a BD
- Asegurarse de que la base de datos no esté corrupta

### LAS IMÁGENES NO SE MUESTRAN

Soluciones:
- Verificar que exista la carpeta resources/images/
- Comprobar que los archivos tengan extensión .jpeg o .jpg
- Asegurarse de que el nombre del archivo coincida con el nombre del producto
- Revisar los permisos de lectura de la carpeta

### ERROR AL AÑADIR O ELIMINAR REGISTROS

Soluciones:
- Verificar que la base de datos no esté en modo solo lectura
- Comprobar que todos los campos obligatorios tengan valores válidos
- Asegurarse de introducir números en formato correcto (punto para decimales)
- Revisar la consola para mensajes de error SQL específicos

### EL PLANIFICADOR DE MARATÓN NO ENCUENTRA COMBINACIONES

Soluciones:
- Aumentar el tiempo disponible
- Reducir el número de títulos requeridos
- Cambiar el filtro de género a "Todos"
- Verificar que haya contenido cargado en la base de datos

---

## EQUIPO DE DESARROLLO

Estadísticas basadas en análisis de commits (6,954+ líneas de código):

**MIKEL OYARZABAL RIAÑO:**
- Commits: 30 (60%)
- Líneas de código: 4,823 (69.4%)
- Responsabilidad: GestorBD completo, VentanaCliente, VentanaTrabajador, ProgressBar, Modelo dominio, Tablas estilizadas, VentanaMaraton

**ASIER:**
- Commits: 2 (4%)
- Líneas de código: 887 (12.8%)
- Responsabilidad: TableModels, VentanasTabla (Clientes, Productos), Persistencia perfiles

**ALEJANDRO (MEEGA4 / zarza):**
- Commits: 6 (12%)
- Líneas de código: 749 (10.8%)
- Responsabilidad: AdminTableStyler, TableModels, VentanasTabla (Películas, Series)

**JON URKIDI:**
- Commits: 12 (24%)
- Líneas de código: 495 (7.1%)
- Responsabilidad: VentanaSeleccionar, VentanaLogin, VentanaPeliculasSeries

TOTAL: 50 commits, 6,954+ líneas de código

---

## PLANIFICACIÓN DEL PROYECTO

DISTRIBUCIÓN DE HORAS (124h totales):
- Mikel Oyarzabal Riaño: 38 horas
- Alejandro: 30 horas
- Asier: 29 horas
- Jon Urkidi: 27 horas

PERÍODO DE DESARROLLO:
- Inicio: 26 de septiembre de 2025
- Finalización: 10 de diciembre de 2025
- Duración: 10 semanas

ENTREGAS:
- Entrega 1 (E1): 17 de noviembre de 2025
- Entrega 2 (E2): 10 de diciembre de 2025

FASES DEL PROYECTO:

**FASE 1 - Hasta E1 (17/11/2025):**
- T1: Análisis de requisitos y diseño inicial (5h)
- T2: Modelo de dominio completo (11h)
- T3: VentanaCliente con perfiles múltiples (10h)
- T4: VentanaSeleccionar y login (9h)
- T5: VentanaTrabajador con HiloImagen (9h)
- T6: VentanaPeliculasSeries catálogo (7h)

**FASE 2 - Hasta E2 (10/12/2025):**
- T7: GestorBD: diseño y creación de tablas (11h)
- T8: GestorBD: operaciones CRUD completas (13h)
- T9: Carga masiva de datos desde CSV (7h)
- T10: Integración BD con todas las ventanas GUI (11h)
- T11: TableModels y gestión de tablas (8h)

**FASE 3 - Post E2:**
- T12: Ventanas de gestión CRUD editables (13h)
- T13: ProgressBar con animación (3h)
- T14: Estilizado AdminTableStyler y mejoras UI (9h)
- T15: Persistencia perfiles secundarios y cierre BD (6h)
- T16: VentanaMaraton con algoritmos recursivos (8h)
- T17: VentanaPerfilTrabajador (2h)
- T18: Reproducción de trailers (3h)

---

## FLUJO DE TRABAJO

1. Ejecutar Main.java
2. Seleccionar tipo de usuario (Cliente o Trabajador)
3. Introducir credenciales de acceso
4. Esperar carga del sistema (barra de progreso con pausado)
5. Como Cliente: Seleccionar perfil y navegar catálogo
   Como Trabajador: Acceder al panel y gestionar datos
6. Realizar operaciones (CRUD, búsquedas, maratón, etc.)
7. Cerrar sesión o salir de la aplicación

---

## OPERACIONES CRUD

### AÑADIR CLIENTES

1. En VentanaClientesTabla, presiona CTRL+A
2. Introduce los datos solicitados:
   - Nombre del cliente
   - Apellido
   - Edad (número entero)
   - Contraseña
   - Ubicación
   - Teléfono
   - Email
3. El registro se guardará automáticamente en la BD

### AÑADIR PELÍCULAS

1. En VentanaPeliculasTabla, presiona CTRL+K
2. Introduce los datos:
   - Nombre de la película
   - Descripción
   - Director
   - Género
   - Precio (usar punto como separador decimal)
   - Stock (número entero)
   - Duración en minutos
3. La película se añadirá a la base de datos

### AÑADIR SERIES

1. En VentanaSeriesTabla, presiona CTRL+K
2. Introduce los datos:
   - Nombre de la serie
   - Descripción
   - Género
   - Precio (usar punto como separador decimal)
   - Stock (número entero)
   - Temporadas
   - Episodios totales
3. La serie se guardará en la base de datos

### ELIMINAR REGISTROS

1. Selecciona uno o varios registros haciendo clic
2. Para seleccionar múltiples: mantén CTRL y haz clic
3. Para seleccionar un rango: selecciona la primera fila, mantén SHIFT y selecciona la última
4. Presiona el atajo correspondiente:
   - CTRL+B para clientes
   - CTRL+T para películas o series
5. Confirma la eliminación

ADVERTENCIA: La eliminación es permanente y no se puede deshacer.

### BUSCAR Y FILTRAR

1. Escribe en el campo de filtro (búsqueda en tiempo real)
2. La tabla mostrará solo las coincidencias
3. La búsqueda se realiza en la columna "Nombre"
4. No distingue mayúsculas de minúsculas
5. Borra el texto para mostrar todos los registros

### ORDENAR COLUMNAS

1. Haz clic en el encabezado de una columna para ordenar ascendente
2. Haz clic de nuevo para ordenar descendente
3. Puedes combinar ordenamiento con filtrado

---

## ARQUITECTURA DEL SISTEMA

**MODELO (domain):**
- Clases de entidad: Persona, Cliente, Trabajador, Producto, Pelicula, Serie
- Perfiles: PerfilCliente, PerfilSecundario
- Encapsulan la lógica de negocio y datos

**VISTA (gui):**
- Interfaces gráficas Swing (18 clases)
- Presentación de datos al usuario
- Captura de eventos de usuario
- Estilizado consistente con AdminTableStyler

**CONTROLADOR (db):**
- GestorBD: Gestión de base de datos (884 líneas)
- Intermediario entre Modelo y Vista
- Manejo de operaciones CRUD
- Gestión de perfiles secundarios

---

## CARACTERÍSTICAS TÉCNICAS

**DISEÑO DE INTERFAZ:**
- Tema oscuro con fondo gris carbón (RGB: 33, 37, 41)
- Elementos destacados en color dorado (RGB: 255, 193, 7)
- Ventanas sin decoración del sistema (undecorated)
- Bordes personalizados dorados
- Ventanas arrastrables desde la barra superior
- Efectos hover en botones y elementos interactivos

**GESTIÓN DE DATOS:**
- Persistencia con SQLite 3.50.3.0
- Operaciones transaccionales
- Relaciones CASCADE para integridad referencial
- Índices para optimización de consultas

**MULTITHREADING:**
- HiloImagen: Carrusel de imágenes en panel trabajador
- Counter: Hilo de la barra de progreso con pausado
- SwingWorker: Cálculo de combinaciones de maratón
- Hilo de reproducción: Trailers sin bloquear UI

**CARGA DE IMÁGENES:**
- Las imágenes deben estar en resources/images/
- Formato: .jpeg o .jpg
- Nombre del archivo debe coincidir con el nombre del producto
- Si no hay imagen, se muestra "Sin imagen"
- Escalado automático con Image.SCALE_SMOOTH

**VIDEOS:**
- Directorio: resources/videos/
- Formato: .mp4
- Se abren con el reproductor predeterminado del sistema

---

## LICENCIA

Este proyecto fue desarrollado como parte del curso de Programación 3 en la Universidad de Deusto.

---

## CONTACTO Y SOPORTE

Para soporte adicional o reportar errores:
- GitHub Issues: https://github.com/MEEGA4/ProyectoProg3/issues
- Contactar a través de la plataforma universitaria

---

DEUSTOFILM - Sistema de Gestión de Videoclub
Versión 2.0 - Enero 2026
