# SpeedFast - Semana 7

Actividad de **Desarrollo Orientado a Objetos II**:  
**Conectando aplicaciones Java con bases de datos mediante JDBC**.

## Objetivo

Conectar la interfaz gráfica de SpeedFast con una base de datos MySQL utilizando JDBC.

El proyecto permite:

- Registrar pedidos directamente en MySQL.
- Registrar repartidores directamente en MySQL.
- Consultar pedidos almacenados y mostrarlos en `JTable`.
- Consultar repartidores desde la base de datos.
- Registrar una entrega relacionando pedido y repartidor.
- Actualizar un pedido desde `PENDIENTE` a `EN_REPARTO`.
- Probar la conexión a MySQL desde la ventana principal.

## Estructura

```text
SpeedFast_Semana7/
├── database/
│   └── speedfast_db.sql
├── src/
│   ├── dao/
│   │   ├── ConexionBD.java
│   │   ├── EntregaDAO.java
│   │   ├── PedidoDAO.java
│   │   └── RepartidorDAO.java
│   ├── main/
│   │   └── Main.java
│   ├── modelo/
│   │   ├── Entrega.java
│   │   ├── EstadoPedido.java
│   │   ├── Pedido.java
│   │   ├── Repartidor.java
│   │   └── TipoPedido.java
│   └── vista/
│       ├── VentanaAsignarEntrega.java
│       ├── VentanaListaPedidos.java
│       ├── VentanaPrincipal.java
│       ├── VentanaRegistroPedido.java
│       └── VentanaRegistroRepartidor.java
├── .gitignore
├── pom.xml
└── README.md
```

## 1. Crear la base de datos

Abre MySQL Workbench y ejecuta:

```text
database/speedfast_db.sql
```

El script crea la base `speedfast_db`, las tablas `pedido`, `repartidor` y `entrega`,
además de las claves foráneas correspondientes.

## 2. Cargar MySQL Connector/J

El proyecto incluye `pom.xml` con MySQL Connector/J.

En IntelliJ:

1. Abre el proyecto.
2. Espera a que IntelliJ detecte Maven.
3. Presiona **Load Maven Changes** si aparece.
4. Verifica que `mysql-connector-j` aparezca en las dependencias.

Si prefieres configurarlo manualmente también puedes agregar MySQL Connector/J desde:

`File > Project Structure > Libraries`

## 3. Configurar usuario y contraseña

Por defecto se utiliza:

```text
URL: jdbc:mysql://localhost:3306/speedfast_db
Usuario: root
Contraseña: vacía
```

Si tu MySQL tiene contraseña, agrega en la configuración de ejecución de IntelliJ:

```text
SPEEDFAST_DB_USER=root
SPEEDFAST_DB_PASSWORD=tu_contraseña
```

También puedes cambiar la URL mediante:

```text
SPEEDFAST_DB_URL=jdbc:mysql://localhost:3306/speedfast_db
```

## 4. Ejecutar

Ejecuta:

```text
src/main/Main.java
```

Primero prueba el botón **Probar conexión a MySQL**.

## Flujo recomendado de prueba

1. Registrar dos repartidores.
2. Registrar tres pedidos.
3. Abrir **Listar pedidos** y comprobar que aparezcan.
4. Cerrar y volver a abrir la aplicación.
5. Abrir nuevamente **Listar pedidos** para verificar persistencia.
6. Abrir **Asignar repartidor / Iniciar entrega**.
7. Seleccionar un pedido pendiente y un repartidor.
8. Registrar la entrega.
9. Volver al listado y comprobar que el pedido quedó `EN_REPARTO`.

## DAOs implementados

### PedidoDAO

- `guardar(Pedido pedido)` con `PreparedStatement`.
- `listarTodos()` con `ResultSet`.
- `listarPendientes()`.
- `actualizarEstado(...)`.

### RepartidorDAO

- `guardar(Repartidor repartidor)`.
- `listarTodos()` con `ResultSet`.

### EntregaDAO

- `guardar(Entrega entrega)`.

## Nota sobre el script entregado en la pauta

En el ejemplo del documento aparece `CREATE DATABASE ... speedfast` y luego
`USE speedfast_db`. Este proyecto usa `speedfast_db` de forma consistente porque
ese es el nombre indicado posteriormente para la configuración y conexión JDBC.
