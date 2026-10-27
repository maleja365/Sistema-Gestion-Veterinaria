# VetCare — Sistema de Gestión Veterinaria

## Integrantes

- María Alejandra Díaz Estrada
- Santiago Alexander Cárdenas

## 1. Descripción del proyecto

VetCare es una plataforma web para la gestión de información médica y administrativa de mascotas. Permite centralizar la información relacionada con usuarios, mascotas, citas veterinarias, diagnósticos, vacunas e historiales médicos.

El proyecto está desarrollado mediante una arquitectura que separa el frontend, encargado de la interfaz de usuario, del backend, responsable de la lógica del negocio y la comunicación con la base de datos.

## 2. Objetivo

Desarrollar una plataforma web que permita centralizar y gestionar la información médica y administrativa de las mascotas, facilitando el control de citas, historiales médicos, vacunas, usuarios y veterinarios.

## 3. Requerimientos funcionales

El sistema contempla las siguientes funcionalidades:

- Registrar e iniciar sesión de usuarios.
- Registrar, consultar y editar información de mascotas.
- Consultar el historial médico de las mascotas.
- Registrar diagnósticos y vacunas.
- Solicitar, aprobar y cancelar citas veterinarias.
- Gestionar usuarios y veterinarios.
- Controlar el acceso según los roles y permisos implementados.

## 4. Tecnologías utilizadas

### Frontend

- HTML5: estructura de las páginas web.
- CSS3: estilos y diseño visual.
- JavaScript: interactividad y comunicación con el backend.
- Visual Studio Code: editor utilizado para desarrollar el frontend.
- Live Server: servidor local utilizado para visualizar la aplicación durante el desarrollo.

### Backend

- Java: lenguaje de programación.
- Spring Boot: framework para desarrollar la aplicación y los servicios del backend.
- API REST: comunicación entre el frontend y el backend.

### Base de datos y control de versiones

- MySQL: sistema gestor de base de datos.
- Git: control de versiones.
- GitHub: alojamiento del repositorio del proyecto.

## 5. Arquitectura del sistema

VetCare está dividido en dos componentes principales:

**Frontend:** desarrollado con HTML, CSS y JavaScript en Visual Studio Code. Presenta las interfaces y permite que los usuarios interactúen con las funcionalidades del sistema.

**Backend:** desarrollado con Java y Spring Boot. Procesa las solicitudes del frontend, aplica la lógica del negocio y gestiona las operaciones de datos mediante los servicios implementados.

La comunicación entre ambos componentes se realiza mediante solicitudes HTTP a los endpoints disponibles en la API, según su configuración.

## 6. Roles del sistema

- **Propietario:** consulta y gestiona la información de sus mascotas, solicita citas y consulta la información médica autorizada.
- **Veterinario:** gestiona la atención veterinaria y registra diagnósticos y vacunas según los permisos implementados.
- **Administrador:** administra los usuarios y veterinarios de acuerdo con las funcionalidades disponibles.

## 7. Instalación y ejecución

### 7.1. Requisitos previos

Para ejecutar el proyecto se requiere:

- Visual Studio Code.
- Un navegador web.
- La extensión Live Server de Visual Studio Code.
- Java en la versión compatible con el proyecto.
- Maven o Maven Wrapper, según la configuración del backend.
- MySQL, si se utiliza como base de datos.
- Git, para clonar y actualizar el repositorio.

### 7.2. Obtener el código fuente

Clonar el repositorio de GitHub:

https://github.com/maleja365/Sistema-Gestion-Veterinaria 

Abrir el proyecto en Visual Studio Code y localizar las carpetas correspondientes al frontend y al backend.

### 7.3. Ejecutar el frontend

1. Abrir en Visual Studio Code la carpeta del frontend.
2. Localizar el archivo HTML principal.
3. Hacer clic derecho sobre el archivo.
4. Seleccionar **Open with Live Server**.
5. Abrir la dirección local que muestra el navegador.

Durante el desarrollo, el frontend se ejecuta en:

http://127.0.0.1:5500/

Este puerto corresponde al servidor local de Live Server y puede cambiar según la configuración del entorno.

### 7.4. Configurar la base de datos

1. Iniciar el servicio de MySQL.
2. Crear o importar la base de datos requerida por el proyecto.
3. Configurar las credenciales de conexión en el archivo de configuración correspondiente del backend.
4. Verificar que Spring Boot pueda conectarse correctamente a la base de datos.

Las credenciales deben configurarse de manera segura y no publicarse en el repositorio.

### 7.5. Ejecutar el backend Spring Boot

1. Abrir la carpeta del backend en Visual Studio Code o en el entorno de desarrollo utilizado.
2. Verificar la configuración de Java y Maven.
3. Configurar la conexión a la base de datos.
4. Ejecutar la clase principal de Spring Boot o utilizar el comando compatible con el proyecto.

Si no dispone de Maven Wrapper, se puede utilizar Maven instalado:

```bash
mvn spring-boot:run
```

El puerto del backend depende de la configuración de Spring Boot y debe verificarse en sus archivos de configuración o en la consola de ejecución.


## 8. Diseño de la base de datos

La base de datos almacena la información necesaria para la gestión de usuarios, mascotas y procesos veterinarios.

Esta sección debe incluir:

- Diagrama entidad-relación.
- Tablas y campos implementados.
- Claves primarias y foráneas.
- Relaciones entre las entidades.
- Script SQL para crear o importar la base de datos, si está disponible.

**Diagrama de la base de datos:**



## 9. Operaciones CRUD

CRUD corresponde a las operaciones básicas de gestión de datos:

- **Create:** crear registros.
- **Read:** consultar registros.
- **Update:** actualizar registros.
- **Delete:** eliminar registros.

En esta sección se deben documentar los CRUD implementados en VetCare, indicando las entidades que los utilizan y las pruebas realizadas.

Para la entrega académica se debe demostrar al menos un CRUD por cada integrante del equipo, con evidencias de las operaciones realizadas.

## 10. Pruebas de funcionamiento

Se deben realizar pruebas para verificar:

- Registro e inicio de sesión.
- Gestión de mascotas.
- Gestión de usuarios y veterinarios.
- Solicitud y administración de citas.
- Registro y consulta de diagnósticos y vacunas.
- Comunicación entre el frontend y el backend.
- Conexión con la base de datos.
- Permisos correspondientes a cada rol.

**Documentación de la API:** agregar la dirección de Swagger si se encuentra configurado en el backend.

## 11. Evidencias del proyecto

Se deben recopilar las siguientes evidencias:

- Diagrama de la base de datos.
- <img width="673" height="857" alt="image" src="https://github.com/user-attachments/assets/de3716f8-0908-424f-a928-3138a19473a4" />

- Crud Mascotas
  <img width="1912" height="973" alt="image" src="https://github.com/user-attachments/assets/fc2ad827-3faf-4b66-bd87-c2c763a53552" />
  -Crud crear Veterinarios
<img width="1917" height="968" alt="image" src="https://github.com/user-attachments/assets/f93880d8-2229-407d-a999-0c5524139090" />
<img width="1917" height="907" alt="image" src="https://github.com/user-attachments/assets/384695d0-3819-4829-a367-1531a60dc717" />

-Crud Citas
<img width="1592" height="927" alt="image" src="https://github.com/user-attachments/assets/5cc3b2b7-7d01-47ec-98fc-a92a7715af60" />
<img width="1605" height="917" alt="image" src="https://github.com/user-attachments/assets/90098711-1b45-48cc-b7ef-1eee31dbac30" />



- Pruebas de funcionamiento del frontend y backend.
<img width="1912" height="1017" alt="image" src="https://github.com/user-attachments/assets/098e1f53-7c92-4141-9056-1773221ce7cc" />
<img width="1912" height="1015" alt="image" src="https://github.com/user-attachments/assets/183930f7-5fe5-4a62-adf5-5579879000fc" />

- Enlace al tablero de Jira.
- https://malejaestrada31.atlassian.net/jira/software/projects/SCRUM/boards/1?filter=&groupBy=assignee&atlOrigin=eyJpIjoiMzY2NDg0ODE1ZWU2NGY0N2JkODIzNDFkZjI3ZDMwMGUiLCJwIjoiaiJ9

## 12. Control de versiones

El proyecto utiliza Git y GitHub para almacenar el código fuente y registrar los cambios realizados durante el desarrollo.


**Repositorio:** https://github.com/maleja365/Sistema-Gestion-Veterinaria

## 13. Estado del proyecto

VetCare se encuentra en desarrollo. Las funcionalidades, pruebas y documentación se completarán progresivamente hasta cumplir los requisitos establecidos para la entrega final.

