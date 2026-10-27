# VetCare · Guía rápida

Dos carpetas:

- `Veterinaria-backend/`  → Spring Boot (solo API REST + Swagger). Ya NO tiene HTML.
- `veterinaria-frontend/` → HTML/CSS/JS para abrir en Visual Studio Code.

## 1. Base de datos (hazlo una sola vez)
La tabla `mascotas` cambió (ahora tiene `propietario_id`) y desaparece el rol RECEPCIONISTA,
así que recrea la base:

    DROP DATABASE veterinaria_db;
    CREATE DATABASE veterinaria_db;

Al arrancar, Spring crea las tablas y el administrador: `admin@veterinaria.com` / `Admin123*`.

## 2. Backend
1. Reemplaza tu proyecto por `Veterinaria-backend` (o copia sobre el tuyo los archivos de `src/` y el `pom.xml`).
   Borra la carpeta vieja `src/main/resources/static`.
2. Maven → Update Project (Alt+F5 en Eclipse/STS) para bajar Swagger.
3. Ejecuta `VeterinariaApplication`.
4. Swagger: http://localhost:8080/swagger-ui.html

## 3. Frontend en Visual Studio Code
1. File → Open Folder → `veterinaria-frontend`.
2. Extensión "Live Server" (Ritwick Dey).
3. Clic derecho en `index.html` → "Open with Live Server" (http://127.0.0.1:5500 o localhost:5500).
4. NO abras el archivo con doble clic (file://): la sesión no funciona así.

El enlace está en `js/api.js`: `const API = http://<host>:8080`. El backend permite ese origen en
`SecurityConfig` (CORS con credenciales) y `app.cors.origenes` en `application.properties`.

## 4. Quién puede hacer qué
| Rol | Puede | NO puede |
|---|---|---|
| Propietario | Registrarse, iniciar sesión, registrar/actualizar/eliminar SUS mascotas, solicitar cita, consultar historial y vacunas de sus mascotas | Ver mascotas ajenas, registrar diagnósticos/vacunas |
| Veterinario | Iniciar sesión, consultar mascotas, gestionar sus citas (confirmar/atender/cancelar), registrar diagnóstico y vacuna | Crear mascotas o citas, gestionar usuarios |
| Administrador | Iniciar sesión, registrar/editar/eliminar veterinarios, consultar estadísticas | Crear mascotas ni citas |

## 5. Prueba sugerida
1. Entra como admin → registra un veterinario.
2. Cierra sesión → "Regístrate" como propietario → registra una mascota → solicita cita con el veterinario.
3. Entra como veterinario → confirma la cita → abre la mascota → registra diagnóstico y vacuna.
4. Entra como propietario → mira historial y vacunas.
