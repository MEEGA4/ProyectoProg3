# DEUSTOFILM - Sistema de Gestión de Videoclub

Sistema completo de gestión para videoclub digital con interfaz gráfica moderna

---

## DESCRIPCIÓN

DEUSTOFILM es una aplicación de escritorio desarrollada en Java que implementa un sistema completo de gestión para un videoclub digital. Utiliza arquitectura MVC (Modelo-Vista-Controlador) con interfaz gráfica Swing y base de datos SQLite.

### CARACTERÍSTICAS PRINCIPALES

- Sistema de autenticación dual (Clientes y Trabajadores)
- Gestión completa de películas y series
- Perfiles múltiples por cliente (hasta 5 perfiles)
- Interfaz moderna con tema oscuro y detalles dorados
- Base de datos SQLite con persistencia de datos
- Carga masiva de datos desde archivos CSV
- Operaciones CRUD completas para todas las entidades
- Sistema de búsqueda y filtrado en tablas

---

## TECNOLOGÍAS UTILIZADAS

- Java JDK 11 o superior: Lenguaje de programación principal
- Swing: Framework para interfaz gráfica
- SQLite: Base de datos embebida
- JDBC: Conectividad con base de datos
- CSV: Formato de carga de datos inicial

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
│   │   └── GestorBD.java
│   ├── domain/
│   │   ├── Persona.java
│   │   ├── Cliente.java
│   │   ├── Trabajador.java
│   │   ├── Producto.java
│   │   ├── Pelicula.java
│   │   └── Serie.java
│   ├── gui/
│   │   ├── VentanaSeleccionar.java
│   │   ├── VentanaCliente.java
│   │   ├── VentanaTrabajador.java
│   │   ├── VentanaPeliculasSeries.java
│   │   ├── ProgressBar.java
│   │   └── [ventanas CRUD]
│   └── main/
│       └── Main.java
├── resources/
│   ├── db/
│   │   └── videoclub.db
│   ├── data/
│   │   ├── clientes.csv
│   │   ├── trabajadores.csv
│   │   ├── peliculas.csv
│   │   └── series.csv
│   └── images/
│       ├── logo.png
│       └── [películas/series].jpeg
└── lib/
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
javac -d bin -sourcepath src src/main/Main.java
```

### EJECUTAR

```
java -cp bin main.Main
```

### USANDO UN IDE

Si usas Eclipse o IntelliJ IDEA:
1. Abre el proyecto
2. Ejecuta la clase Main.java

---

## USO DE LA APLICACIÓN

### PRIMERA EJECUCIÓN

Al ejecutar por primera vez:
1. Se inicializa la conexión con SQLite
2. Se crean todas las tablas
3. Se cargan datos desde CSV (si está habilitado)
4. Aparece la ventana de inicio de sesión

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

GESTIÓN DE PERFILES:
- Crear hasta 5 perfiles por cuenta
- Editar nombre, color o imagen de perfil
- Eliminar perfiles (excepto el principal)

CATÁLOGO DE CONTENIDO:
- Navegar por TODO, PELÍCULAS o SERIES
- Carrusel con 6 elementos por fila
- Visualización de portadas

### TRABAJADOR (ADMINISTRADOR)

PANEL PRINCIPAL:
- Gestionar Clientes - CRUD completo
- Gestionar Películas - CRUD completo
- Gestionar Series - CRUD completo
- Todos los Productos - Vista combinada

OPERACIONES DISPONIBLES:
- Añadir nuevos registros
- Buscar y filtrar datos
- Eliminar registros (individual o múltiple)
- Ordenar por columnas

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

PERSONA:
- Almacena datos comunes de usuarios
- Campos: nombre, apellido, edad, contraseña, ubicación

CLIENTE:
- Hereda de Persona
- Campos adicionales: teléfono, email

TRABAJADOR:
- Hereda de Persona
- Campos adicionales: puesto, salario

PRODUCTO:
- Clase base para contenido multimedia
- Campos: nombre, descripción, precio, stock

PELICULA:
- Hereda de Producto
- Campos adicionales: director, género, duración

SERIE:
- Hereda de Producto
- Campos adicionales: género, temporadas, episodios

PERFIL SECUNDARIO:
- Perfiles adicionales asociados a clientes
- Campos: nombre, color, imagen

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

## RESOLUCIÓN DE PROBLEMAS

### LA APLICACIÓN NO ARRANCA

Soluciones:
- Verificar que Java esté instalado: java -version
- Asegurarse de tener JDK 11 o superior
- Comprobar permisos de lectura/escritura en videoclub.db
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

---

## EQUIPO DE DESARROLLO

Estadísticas basadas en análisis de commits (6,954 líneas de código):

MIKEL OYARZABAL RIAÑO:
- Commits: 30 (60%)
- Líneas de código: 4,823 (69.4%)
- Responsabilidad: GestorBD completo, VentanaCliente, VentanaTrabajador, ProgressBar, Modelo dominio, Tablas estilizadas

ASIER:
- Commits: 2 (4%)
- Líneas de código: 887 (12.8%)
- Responsabilidad: TableModels, VentanasTabla (Clientes, Productos), Persistencia perfiles

ALEJANDRO (MEEGA4 / zarza):
- Commits: 6 (12%)
- Líneas de código: 749 (10.8%)
- Responsabilidad: AdminTableStyler, TableModels, VentanasTabla (Películas, Series)

JON URKIDI:
- Commits: 12 (24%)
- Líneas de código: 495 (7.1%)
- Responsabilidad: VentanaSeleccionar, VentanaLogin, VentanaPeliculasSeries

TOTAL: 50 commits, 6,954 líneas de código

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

FASE 1 - Hasta E1 (17/11/2025):
- T1: Análisis de requisitos y diseño inicial (5h)
- T2: Modelo de dominio completo (11h)
- T3: VentanaCliente con perfiles múltiples (10h)
- T4: VentanaSeleccionar y login (9h)
- T5: VentanaTrabajador con HiloImagen (9h)
- T6: VentanaPeliculasSeries catálogo (7h)

FASE 2 - Hasta E2 (10/12/2025):
- T7: GestorBD: diseño y creación de tablas (11h)
- T8: GestorBD: operaciones CRUD completas (13h)
- T9: Carga masiva de datos desde CSV (7h)
- T10: Integración BD con todas las ventanas GUI (11h)
- T11: TableModels y gestión de tablas (8h)

FASE 3 - Post E2:
- T12: Ventanas de gestión CRUD editables (13h)
- T13: ProgressBar con animación (3h)
- T14: Estilizado AdminTableStyler y mejoras UI (9h)
- T15: Persistencia perfiles secundarios y cierre BD (6h)

---

## FLUJO DE TRABAJO

1. Ejecutar Main.java
2. Seleccionar tipo de usuario (Cliente o Trabajador)
3. Introducir credenciales de acceso
4. Esperar carga del sistema (barra de progreso)
5. Como Cliente: Seleccionar perfil y navegar catálogo
   Como Trabajador: Acceder al panel y gestionar datos
6. Realizar operaciones (CRUD, búsquedas, etc.)
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
3. El registro se guardará automáticamente

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

MODELO (domain):
- Clases de entidad: Persona, Cliente, Trabajador, Producto, Pelicula, Serie
- Encapsulan la lógica de negocio y datos

VISTA (gui):
- Interfaces gráficas Swing
- Presentación de datos al usuario
- Captura de eventos de usuario

CONTROLADOR (db):
- GestorBD: Gestión de base de datos
- Intermediario entre Modelo y Vista
- Manejo de operaciones CRUD

---

## CARACTERÍSTICAS TÉCNICAS

DISEÑO DE INTERFAZ:
- Tema oscuro con fondo gris carbón (RGB: 33, 37, 41)
- Elementos destacados en color dorado (RGB: 255, 193, 7)
- Ventanas sin decoración del sistema (undecorated)
- Bordes personalizados dorados
- Ventanas arrastrables desde la barra superior

GESTIÓN DE DATOS:
- Persistencia con SQLite
- Operaciones transaccionales
- Relaciones CASCADE para integridad referencial
- Índices para optimización de consultas

CARGA DE IMÁGENES:
- Las imágenes deben estar en resources/images/
- Formato: .jpeg o .jpg
- Nombre del archivo debe coincidir con el nombre del producto
- Si no hay imagen, se muestra "Sin imagen"

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
Versión 1.0 - Diciembre 2025
