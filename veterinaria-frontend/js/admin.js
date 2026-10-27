let veterinarios = [];
let editandoId = null;

document.querySelectorAll('[data-cerrar]').forEach(b =>
  b.addEventListener('click', () => b.closest('dialog').close()));

(async function iniciar() {
  const usuario = await exigirRol('ADMIN');
  if (!usuario) return;
  pintarUsuario(usuario);
  iniciarMenu({ vets: cargarVets, stats: cargarStats });
})();

/* ---------------- Gestionar veterinarios ---------------- */
async function cargarVets() {
  const cont = document.getElementById('tabla-vets');
  try {
    veterinarios = await api('/api/admin/veterinarios');
    if (!veterinarios.length) { cont.innerHTML = vacio('Aún no hay veterinarios registrados.', 'perro'); return; }
    cont.innerHTML = `<table><thead><tr><th>Nombre</th><th>Correo</th><th>Acciones</th></tr></thead><tbody>` +
      veterinarios.map(v => `<tr><td>🩺 ${esc(v.nombre)}</td><td>${esc(v.correo)}</td>
        <td><button class="btn btn-sm btn-suave" onclick="editar(${v.id})">Editar</button>
            <button class="btn btn-sm btn-peligro" onclick="eliminar(${v.id})">Eliminar</button></td></tr>`).join('') +
      `</tbody></table>`;
  } catch (e) { cont.innerHTML = `<div class="alerta alerta-error">${esc(e.message)}</div>`; }
}

const dlg = document.getElementById('dlg-vet');
const err = document.getElementById('err-vet');

document.getElementById('btn-nuevo').addEventListener('click', () => {
  editandoId = null;
  document.getElementById('form-vet').reset();
  document.getElementById('t-vet').textContent = 'Registrar veterinario';
  document.getElementById('l-pass').textContent = 'Contraseña (mínimo 8 caracteres)';
  document.getElementById('v-pass').required = true;
  err.hidden = true;
  dlg.showModal();
});

function editar(id) {
  const v = veterinarios.find(x => x.id === id);
  editandoId = id;
  document.getElementById('t-vet').textContent = 'Actualizar veterinario';
  document.getElementById('l-pass').textContent = 'Nueva contraseña (déjala vacía para no cambiarla)';
  document.getElementById('v-pass').required = false;
  document.getElementById('v-nombre').value = v.nombre;
  document.getElementById('v-correo').value = v.correo;
  document.getElementById('v-pass').value = '';
  err.hidden = true;
  dlg.showModal();
}

document.getElementById('form-vet').addEventListener('submit', async (e) => {
  e.preventDefault();
  err.hidden = true;
  const cuerpo = {
    nombre: document.getElementById('v-nombre').value,
    correo: document.getElementById('v-correo').value,
    password: document.getElementById('v-pass').value
  };
  try {
    await api(editandoId ? `/api/admin/veterinarios/${editandoId}` : '/api/admin/veterinarios',
      { metodo: editandoId ? 'PUT' : 'POST', cuerpo });
    dlg.close();
    aviso(editandoId ? 'Veterinario actualizado.' : 'Veterinario registrado.');
    cargarVets();
  } catch (ex) {
    err.textContent = ex.message;
    err.hidden = false;
  }
});

async function eliminar(id) {
  const v = veterinarios.find(x => x.id === id);
  if (!confirm(`¿Eliminar a ${v.nombre}?`)) return;
  try {
    await api(`/api/admin/veterinarios/${id}`, { metodo: 'DELETE' });
    aviso('Veterinario eliminado.');
    cargarVets();
  } catch (e) { aviso(e.message, true); }
}

/* ---------------- Estadísticas ---------------- */
async function cargarStats() {
  try {
    const s = await api('/api/admin/estadisticas');
    document.getElementById('kpis').innerHTML = [
      ['Propietarios', s.propietarios], ['Veterinarios', s.veterinarios],
      ['Mascotas', s.mascotas], ['Citas', s.citas]
    ].map(([t, n]) => `<div class="kpi"><b>${n}</b><span>${t}</span></div>`).join('');
    barras('barras-citas', s.citasPorEstado);
    barras('barras-especies', s.mascotasPorEspecie);
  } catch (e) { aviso(e.message, true); }
}

function barras(id, mapa) {
  const entradas = Object.entries(mapa);
  const max = Math.max(1, ...entradas.map(([, n]) => n));
  document.getElementById(id).innerHTML = entradas.length
    ? entradas.map(([k, n]) => `<div class="barra"><span>${esc(k)}</span>
        <div class="pista"><i style="width:${(n / max) * 100}%"></i></div><em>${n}</em></div>`).join('')
    : '<p class="sub">Sin datos todavía.</p>';
}
