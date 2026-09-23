# Sistema de Inscripción a Webinar Corporativo — JDBC + MVC

Aplicación de consola en Java para gestionar inscripciones a un webinar corporativo, usando JDBC, PreparedStatement, transacciones y arquitectura MVC.

## Tecnologías

- Java + Maven
- MySQL (mysql-connector-j)
- JDBC con PreparedStatement
- Arquitectura MVC (Modelo / Vista / Controlador)

## Estructura del proyecto

```
src/main/java/
├── Controlador/
│   └── ControladorParticipante.java
├── Modelo/
│   ├── Clases/
│   │   └── Participante.java
│   └── Persistencia/
│       ├── ConexionDB.java
│       └── Operaciones.java
└── Vista/
    └── JavaDatabaseClase.java
```

## Script SQL

```sql
USE my_db;

CREATE TABLE IF NOT EXISTS participantes (
    idparticipante INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(120) NOT NULL UNIQUE,
    empresa VARCHAR(100) NOT NULL
);
```

## Configuración de conexión

En `Modelo/Persistencia/ConexionDB.java` ajustar según el entorno:

```java
url = "jdbc:mysql://localhost:3306/my_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
user = "root";
password = "TU_PASSWORD";
```

## Funcionalidades

1. Inscribir participante (nombre, correo, empresa)
2. Listar todos los participantes
3. Buscar participantes por empresa
4. Contar participantes inscritos
5. Eliminar participante por id
6. Salir

## Reglas de negocio implementadas

- Campos obligatorios: nombre, correo y empresa no pueden estar vacíos.
- No se permiten correos duplicados: se valida antes de insertar.
- La inscripción se maneja como transacción: `setAutoCommit(false)`, `commit()` si todo sale bien, `rollback()` si falla o el correo ya existe.
- Al eliminar un id inexistente, se informa al usuario sin detener el programa.

## Problema encontrado durante las pruebas y solución

**Error:**

Al ejecutar la primera prueba de conexión (`Conexión falló`), la consola mostró:

```
com.mysql.cj.exceptions.UnableToConnectException: Public Key Retrieval is not allowed
```

**Causa:**

MySQL 8+ usa por defecto el plugin de autenticación `caching_sha2_password`, que exige recuperar una clave pública del servidor para cifrar la contraseña en el handshake. El driver `mysql-connector-j` no permite esa recuperación automática a menos que se habilite explícitamente, y la URL de conexión original no traía ese parámetro ni deshabilitaba SSL para desarrollo local.

**Solución:**

Se modificó la URL de conexión en `ConexionDB.java` agregando los parámetros `allowPublicKeyRetrieval=true` y `useSSL=false` (además de `serverTimezone=UTC` para evitar problemas de zona horaria):

```java
url = "jdbc:mysql://localhost:3306/my_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
```

Tras el cambio, la conexión se estableció correctamente y el mensaje `"Base de datos conectada"` apareció en consola.
