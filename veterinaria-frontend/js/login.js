const params = new URLSearchParams(location.search);
if (params.has('salida')) document.getElementById('msg-salida').hidden = false;
if (params.has('registrado')) document.getElementById('msg-registro').hidden = false;

const msgError = document.getElementById('msg-error');

// Si ya hay sesión, lleva directo al panel del rol
api('/api/auth/me').then(u => location.href = PAGINA_POR_ROL[u.rol]).catch(() => {});

document.getElementById('form-login').addEventListener('submit', async (e) => {
  e.preventDefault();
  msgError.hidden = true;
  try {
    const usuario = await api('/api/auth/login', {
      metodo: 'POST',
      cuerpo: {
        correo: document.getElementById('correo').value,
        password: document.getElementById('password').value
      }
    });
    location.href = PAGINA_POR_ROL[usuario.rol];   // cada rol a su panel
  } catch (err) {
    msgError.textContent = err.message;
    msgError.hidden = false;
  }
});
