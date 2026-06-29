# HelpDesk Manager

HelpDesk Manager es una aplicacion de escritorio en Java Swing para gestionar tickets de soporte tecnico. El sistema permite que clientes creen incidencias, que tecnicos atiendan y cierren tickets, y que administradores gestionen usuarios, asignaciones, seguimientos y reportes.

La interfaz esta construida con Swing UI Designer de IntelliJ IDEA mediante archivos `.form`, por lo que las ventanas principales no se construyen manualmente desde cero en codigo Swing.

## Funcionalidades

### Cliente

- Inicio de sesion segun rol.
- Visualizacion y actualizacion de datos de perfil.
- Creacion de tickets con titulo, descripcion, categoria y prioridad.
- Listado de sus propios tickets.
- Consulta de seguimientos asociados a sus tickets.
- Asignacion automatica del ticket recien creado a un tecnico disponible.

### Tecnico

- Visualizacion y actualizacion de datos de perfil.
- Listado de tickets asignados.
- Cierre de tickets asignados.
- Registro de comentario de cierre en la tabla `seguimiento`.
- Consulta de seguimientos de sus tickets asignados.

### Administrador

- Registro, listado, busqueda, actualizacion y eliminacion de usuarios.
- Gestion de tickets: listar, buscar, cambiar estado/prioridad y eliminar.
- Gestion de asignaciones de tickets a tecnicos.
- CRUD de seguimientos.
- Reportes por rango de fechas:
  - total de tickets,
  - tickets abiertos y cerrados,
  - cantidad de seguimientos,
  - tiempo promedio de resolucion,
  - tickets asignados/cerrados por tecnico,
  - resumen por prioridad y categoria.

## Logica de asignacion automatica

Cuando un cliente crea un ticket, el sistema lo asigna automaticamente a un tecnico disponible.

La seleccion del tecnico usa `model.PriorityManager` con una `PriorityQueue`. La carga de cada tecnico se calcula con sus tickets asignados que aun no estan cerrados.

Pesos por prioridad:

```text
CRITICA = 5
ALTA    = 3
MEDIA   = 2
BAJA    = 1
```

El tecnico seleccionado es el que tenga:

1. menor carga ponderada,
2. menor cantidad de tickets abiertos en caso de empate,
3. menor ID de usuario si el empate continua.

## Estructura del proyecto

```text
src/
  Main.java
  config/
    database.properties
  dao/
    AsignacionDAO.java
    ConexionBD.java
    ReporteDAO.java
    SeguimientoDAO.java
    TicketDAO.java
    UsuarioDAO.java
  model/
    Asignacion.java
    PriorityManager.java
    Seguimiento.java
    Ticket.java
    Usuario.java
  view/
    Login.java / Login.form
    VentanaAdmin.java / VentanaAdmin.form
    VentanaAsignacion.java / VentanaAsignacion.form
    VentanaTecnico.java / VentanaTecnico.form
    VentanaUsuario.java / VentanaUsuario.form
```

## Requisitos

- JDK compatible con el proyecto.
- IntelliJ IDEA, recomendado por el uso de Swing UI Designer (`.form`).
- MySQL.
- Driver MySQL Connector/J configurado como libreria del proyecto.

## Configuracion de base de datos

El proyecto lee la conexion desde:

```text
src/config/database.properties
```

Formato esperado:

```properties
db.url=jdbc:mysql://localhost:3306/helpdeskmanager
db.user=usuario
db.password=password
```

Nota: este archivo esta incluido en `.gitignore` porque puede contener credenciales locales.

## Tablas esperadas

El sistema trabaja con las siguientes tablas principales:

### usuarios

```text
id_usuario
nombre
correo
password
rol
```

Roles usados:

```text
ADMIN
TECNICO
CLIENTE
```

### tickets

```text
id_ticket
titulo
descripcion
categoria
prioridad
estado
fecha_creacion
id_usuario
```

Estados usados:

```text
ABIERTO
CERRADO
```

Prioridades usadas:

```text
BAJA
MEDIA
ALTA
CRITICA
```

### asignaciones

```text
id_asignacion
id_ticket
id_tecnico
fecha_asignacion
```

### seguimiento

```text
id_seguimiento
comentario
fecha
id_ticket
id_usuario
```

## Ejecucion

La clase principal es:

```text
src/Main.java
```

Esta clase abre la ventana de login:

```java
Login login = new Login();
JFrame frame = new JFrame("HelpDesk Manager - Login");
frame.setContentPane(login.panel1);
```

Desde el login se redirige a la ventana correspondiente segun el rol del usuario autenticado.

## Reportes

La ventana de administrador incluye una pestana de reportes con filtros por fecha en formato:

```text
yyyy-MM-dd
```

Ejemplo:

```text
2026-01-01
2026-12-31
```

Los reportes usan `ReporteDAO`, que consulta informacion de tickets, asignaciones y seguimiento para calcular metricas operativas.

## Notas de desarrollo

- Las pantallas se gestionan con archivos `.form` y clases Java asociadas.
- Si se agregan componentes visuales nuevos, deben agregarse tambien al `.form` correspondiente.
- Evitar construir pantallas completas manualmente desde codigo Swing para mantener el mismo estilo del proyecto.
- Los DAOs encapsulan el acceso a base de datos.
- Los modelos representan entidades del dominio y estructuras auxiliares como `PriorityManager`.
