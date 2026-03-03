# Digital Money House

Digital Money House es una billetera virtual desarrollada en React (Frontend provisto por D.H.) y la APIRest en Java 17 usando Spring Boot 3.
Se desarrolló el backend en una arquitectura de microservicios y la seguridad se implementó con Keycloak.
Además se utilizó otras tecnologías como: Git, MySQL, Docker, Postman.

## Arquitectura del proyecto
<br>

![Arquitectura del sistema](docs/arquitectura_proyecto.png)
<br>
<br>
## Como correr el proyecto Local

### 1. Base de Datos
1. Instalar **MySQL 8.0** y correr **MySQL Workbench**
    - Versión: 8.0.43 (build 5324808 CE)

### 2. Keycloak
2. Correr **Keycloak**
    - Ubicación: `../keycloak-25.0.6/bin`
    - Comando:
      ```bash
      ./kc.sh start-dev
      ```
    - Versión: 25.0.6

### 3. Backend - Spring Boot 3.1.10
3. Abrir el proyecto en **IntelliJ IDEA 2024.3.7 CE** y levantar los servicios en el siguiente orden:

    1. Lanzar **Eureka**
    2. Lanzar **Config Server**
    3. Lanzar **Gateway**
    4. Lanzar **Users Service**
    5. Lanzar **Accounts Service**
    6. Lanzar **Cards Service**
    7. Lanzar **Transactions Service**

### 4. Frontend
4. Lanzar el frontend:
    - Abrir el proyecto en **VSCode**
    - Ejecutar en la terminal dentro de la carpeta `frontend`:
      ```bash
      npm start

## Como correr el proyecto en Docker


### 1. Docker
1. Correr **Docker Desktop**
    - Ubicación dentro del directorio raiz del proyecto: `/backend/..`
    - Comando en terminal:
      ```bash
      docker-compose build --no-cache
      ```
    - luego:
      ```bash
      docker-compose up
      ```

## Variables de entorno

### Users Service

- CLIENT_ID=api-dh-money
- CLIENT_SECRET=XB3V2oeWqwZz9geABygfkfo2t2VxipSu
- DB_DRIVER=com.mysql.cj.jdbc.Driver
- DB_PASSWORD=8unkercordobA
- DB_URL=jdbc:mysql://localhost:3306/users_service_db?createDatabaseIfNotExist=true&serverTimezone=UTC
- DB_USER=root
- REALM_NAME=dh-money-users

### Accounts Service

- CLIENT_ID=api-dh-money
- CLIENT_SECRET=XB3V2oeWqwZz9geABygfkfo2t2VxipSu
- DB_DRIVER=com.mysql.cj.jdbc.Driver
- DB_PASSWORD=8unkercordobA
- DB_URL=jdbc:mysql://localhost:3306/accounts_service_db?createDatabaseIfNotExist=true&serverTimezone=UTC
- DB_USER=root
- REALM_NAME=dh-money-users

### Cards Service

- DB_DRIVER=com.mysql.cj.jdbc.Driver
- DB_PASSWORD=8unkercordobA
- DB_URL=jdbc:mysql://localhost:3306/cards_service_db?createDatabaseIfNotExist=true&serverTimezone=UTC
- DB_USER=root

### Transactions Service

- DB_DRIVER=com.mysql.cj.jdbc.Driver
- DB_PASSWORD=8unkercordobA
- DB_URL=jdbc:mysql://localhost:3306/transactions_service_db?createDatabaseIfNotExist=true&serverTimezone=UTC
- DB_USER=root

### Gateway

- SERVER_URL=http://localhost:8080/realms/dh-money-users/protocol/openid-connect/certs

### Config-server

- EUREKA_HOSTNAME=localhost
- EUREKA_URL=http://localhost:8761/eureka

1. Para el correcto funcionamiento del Config-server por favor bajar los archivos y copiarlos dentro del directorio ssh (../backend/ssh). Link: https://drive.google.com/drive/folders/1P-fr2x33Nb16U7vv09149yPYYeutR4fB?usp=sharing

## Documentación

- [Testing Manual](docs/Testing%20Manual%20con%20Postman.xlsx)
- [Request en Postman](  https://www.postman.com/flight-geoscientist-27537244/workspace/dhmoney)
## Funcionalidades Desarrolladas

 - Registro de usuarios (se persisten datos en las BD de Keycloak y users-service)
 - Login de usuarios
 - Autenticación por JWT token con Keycloak como proveedor IAM.
 - Recuperación de contraseña mediante enlace previamente enviado al email (Desarrollado en el back pero no integrado al frontend)
 - Obtener la información del usuario en el dashboard (saldo, avatar con su nombre y apellido, etc.)
 - Registro de actividades
 - Generación automática de alias y CVU para el usuario registrado
 - Creación de tarjetas de débito/crédito
 - Eliminación de tarjetas
 - Cargar dinero mediante tarjeta de débito/crédito
 - Cargar dinero mediante transferencia de otro usuario utilizando alias o CVU
 - Envío de dinero mediante transferencia a otro usuario utilizando alias o CVU
 - Descarga de comprobante de actividad
 - Listado de todas las actividades
 - Listado de las últimas 5 actividades
 - Ver detalle del movimiento

## Detalles de los Sprints

### Sprint I

**Historia de usuario:**  
*Como usuario, quiero registrarme en Home Banking para acceder y usar los servicios que ofrece.*

El servicio `users-service` gestiona el registro de usuarios, logueo y cierre de sesión.

**Endpoint de registro de usuarios:**  

`POST http://localhost:8084/register`

- Endpoint sin autenticación
- Datos necesarios: `firstName`, `lastName`, `email`, `phone`, `dni`, `password`
- Respuesta: Status 201 con el usuario creado
- Los campos `cvu` y `alias` se generan automaticamente y en forma aleatoria

Los usuarios se registran en Keycloak y en la base de datos de users-service (no almacenan las contraseñas en la BD de users-service).

**Historias adicionales:**

- *Como usuario, quiero acceder a HomeBanking para realizar transferencias de fondos.*

Si el login es exitoso entonces Keycloak proporciona un token para la sesion aus podemos utilizar los servicios de la app.

### Sprint II

**Historia de usuario:**  
*Como usuario, necesito ver la cantidad de dinero disponible y los últimos 5 movimientos en mi billetera Home Banking.*

**Endpoint:**  
`GET http://localhost:8084/accounts/activities`

- Requiere autenticación (Token).
- Respuesta: Últimas 5 transacciones del usuario.

**Historia de usuario:**  
*Como usuario, quiero ver mi perfil para consultar los datos de mi Cuenta Virtual Uniforme (CVU) y alias provistos por Home Banking.*

**Endpoint:**  
`GET http://localhost:8084/accounts/user-information`

- Requiere autenticación (Token).
- Respuesta: Id, balance de la cuenta, CVU y alias.

**Historia de usuario:**  
*Como usuario me gustaría ver una lista de las tarjetas de crédito y débito que tengo disponibles para utilizar.*

**Endpoint:**  
`POST http://localhost:8084/accounts/register-card`

- Requiere autenticación (Token).
- Respuesta: Confirmación de creación de la tarjeta.

**Endpoint:**  
`GET http://localhost:8084/accounts/cards`

- Requiere autenticación (Token).
- Respuesta: Lista de tarjetas disponibles.

**Historia de usuario:**  
*Como usuario, me gustaría eliminar una tarjeta de débito o crédito cuando no quiera utilizarla más.*

**Endpoint:**  
`DELETE http://localhost:8084/accounts/delete-card/${cardId}`

- Requiere autenticación (Token).
- Respuesta: Confirmación de eliminación de la tarjeta.

### Sprint III - IV

**Historia de usuario:**  
*Como usuario, quiero ver toda la actividad realizada con mi billetera, desde la más reciente a la más antigua, para tener control de mis transacciones.*

**Endpoint:**  
`GET http://localhost:8084/accounts/activities`

- Requiere autenticación (Token).
- Respuesta: Lista de historial de actividades de la cuenta.

**Historia de usuario:**  
*Como usuario, necesito el detalle de una actividad específica.*

**Endpoint:**  
`GET http://localhost:8084/accounts/activity/${activityId}`

- Requiere autenticación (Token).
- Respuesta: Detalle de la actividad por Id de la transacción.

**Historia de usuario:**  
*Como usuario, me gustaría ingresar dinero desde mi tarjeta de crédito o débito a mi billetera Home Banking.*

**Endpoint:**  
`POST http://localhost:8084/accounts/register-transaction`

- Requiere autenticación (Token).
- Datos necesarios: Tarjeta para el depósito y monto.
- Respuesta: Confirmación del depósito.

**Historia de usuario:**  
*Como usuario, quiero poder enviar/transferir dinero a un CBU/CVU/alias desde mi saldo disponible en mi billetera.*

**Endpoint:**  
`POST http://localhost:8084/accounts/register-transaction`

- Requiere autenticación (Token).
- Datos necesarios: Alias/CVU destino y monto.
- Respuesta: Confirmación de la transferencia.



