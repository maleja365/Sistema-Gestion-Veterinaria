/* ==========================================================
   Conexión con el backend Spring Boot
   - Usa el mismo "host" desde el que abriste el frontend
     (localhost o 127.0.0.1) para que la cookie de sesión funcione.
   - Si cambias el puerto del backend, cámbialo aquí.
   ========================================================== */
const API = `http://${location.hostname}:8080`;

const PAGINA_POR_ROL = {
  PROPIETARIO: 'propietario.html',
  VETERINARIO: 'veterinario.html',
  ADMIN: 'admin.html'
};

async function api(ruta, { metodo = 'GET', cuerpo } = {}) {
  let res;
  try {
    res = await fetch(API + ruta, {
      method: metodo,
      credentials: 'include',              // envía la cookie JSESSIONID
      headers: cuerpo ? { 'Content-Type': 'application/json' } : {},
      body: cuerpo ? JSON.stringify(cuerpo) : undefined
    });
  } catch (e) {
    throw new Error('No se pudo conectar con el servidor. ¿Está encendido Spring Boot en el puerto 8080?');
  }
  if (res.status === 204) return null;

  let datos = null;
  try { datos = await res.json(); } catch (e) { /* sin cuerpo */ }

  if (!res.ok) {
    throw new Error((datos && datos.mensaje) || 'Ocurrió un error inesperado.');
  }
  return datos;
}

/** Protege cada página: solo entra quien tenga sesión y el rol correcto. */
async function exigirRol(rolEsperado) {
  try {
    const usuario = await api('/api/auth/me');
    if (usuario.rol !== rolEsperado) {
      location.href = PAGINA_POR_ROL[usuario.rol];
      return null;
    }
    return usuario;
  } catch (e) {
    location.href = 'index.html';
    return null;
  }
}

async function cerrarSesion() {
  try { await api('/api/auth/logout', { metodo: 'POST' }); } catch (e) {}
  location.href = 'index.html?salida=1';
}
