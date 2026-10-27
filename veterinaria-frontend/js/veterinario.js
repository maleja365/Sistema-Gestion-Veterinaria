let mascotas = [];
let actual = null;      // { id, tipo: 'historial' | 'vacunas' }

document.querySelectorAll('[data-cerrar]').forEach(b =>
  b.addEventListener('click', () => b.closest('dialog').close()));

(async function iniciar() {
  const usuario = await exigirRol('VETERINARIO');
  if (!usuario) return;
  pintarUsuario(usuario);
  iniciarMenu({ citas: cargarCitas, mascotas: cargarMascotas });
})();

/* ---------------- Gestionar citas ---------------- */
async function cargarCitas() {
  const cont = document.getElementById('tabla-citas');
  try {
    const citas = await api('/api/citas');
    if (!citas.length) { cont.innerHTML = vacio('No tienes citas asignadas.', 'gato'); return; }
    cont.innerHTML = `<table><thead><tr><th>Mascota</th><th>Propietario</th><th>Fecha</th><th>Motivo</th><th>Estado</th><th>Acciones</th></tr></thead><tbody>` +
      citas.map(c => {
        const cerrada = c.estado === 'Atendida' || c.estado === 'Cancelada';
        return `<tr>
          <td><img class="mini" src="${imgMascota(c.mascotaEspecie)}" alt="">${esc(c.mascotaNombre)}</td>
          <td>${esc(c.propietarioNombre)}</td><td>${fmtFechaHora(c.fechaHora)}</td>
          <td>${esc(c.motivo)}</td>
          <td><span class="estado estado-${c.estado}">${c.estado}</span></td>
          <td>${cerrada ? '—' : `
            ${c.estado === 'Pendiente' ? `<button class="btn btn-sm btn-suave" onclick="estado(${c.id},'Confirmada')">Confirmar</button>` : ''}
            <button class="btn btn-sm btn-ok" onclick="estado(${c.id},'Atendida')">Atendida</button>
            <button class="btn btn-sm btn-peligro" onclick="estado(${c.id},'Cancelada')">Cancelar</button>`}
          </td></tr>`;
      }).join('') + `</tbody></table>`;
  } catch (e) { cont.innerHTML = `<div class="alerta alerta-error">${esc(e.message)}</div>`; }
}

async function estado(id, nuevo) {
  try {
    await api(`/api/citas/${id}/estado`, { metodo: 'PATCH', cuerpo: { estado: nuevo } });
    aviso(`Cita ${nuevo.toLowerCase()}.`);
    cargarCitas();
  } catch (e) { aviso(e.message, true); }
}

/* ---------------- Consultar mascotas ---------------- */
async function cargarMascotas() {
  const cont = document.getElementById('lista-mascotas');
  try {
    mascotas = await api('/api/mascotas');
    if (!mascotas.length) { cont.innerHTML = vacio('Todavía no hay mascotas registradas.', 'conejo'); return; }
    cont.innerHTML = mascotas.map(m => `
      <article class="mascota">
        <img src="${imgMascota(m.especie)}" alt="${esc(m.especie)}">
        <h4>${esc(m.nombre)}</h4>
        <div class="dato">${esc(m.especie)}${m.raza ? ' · ' + esc(m.raza) : ''}</div>
        <div class="dato">${edad(m.fechaNacimiento)} · Dueño: ${esc(m.propietarioNombre)}</div>
        <div class="acciones">
          <button class="btn btn-sm btn-suave" onclick="abrir(${m.id},'historial')">Historial</button>
          <button class="btn btn-sm btn-suave" onclick="abrir(${m.id},'vacunas')">Vacunas</button>
        </div>
      </article>`).join('');
  } catch (e) { cont.innerHTML = `<div class="alerta alerta-error">${esc(e.message)}</div>`; }
}

/* ---------------- Historial / vacunas (consultar + registrar) ---------------- */
async function abrir(id, tipo) {
  actual = { id, tipo };
  const m = mascotas.find(x => x.id === id);
  document.getElementById('cl-img').src = imgMascota(m.especie);
  document.getElementById('cl-titulo').textContent =
    `${tipo === 'historial' ? 'Historial médico' : 'Vacunas'} de ${m.nombre}`;
  document.getElementById('campos-diag').hidden = tipo !== 'historial';
  document.getElementById('campos-vac').hidden = tipo !== 'vacunas';
  document.getElementById('btn-reg').textContent =
    tipo === 'historial' ? 'Registrar diagnóstico' : 'Registrar vacuna';
  document.getElementById('err-reg').hidden = true;
  document.getElementById('form-registro').reset();
  document.getElementById('v-fecha').value = new Date().toISOString().slice(0, 10);
  document.getElementById('dlg-clinico').showModal();
  await refrescar();
}

async function refrescar() {
  const cont = document.getElementById('cl-contenido');
  try {
    const datos = await api(`/api/mascotas/${actual.id}/${actual.tipo}`);
    if (!datos.length) { cont.innerHTML = vacio('Aún no hay registros.', 'perro'); return; }
    cont.innerHTML = actual.tipo === 'historial'
      ? datos.map(d => `<div class="registro"><strong>${esc(d.descripcion)}</strong><br>
          ${d.tratamiento ? 'Tratamiento: ' + esc(d.tratamiento) + '<br>' : ''}
          <small>${fmtFechaHora(d.fecha)} · Dr(a). ${esc(d.veterinarioNombre)}</small></div>`).join('')
      : datos.map(v => `<div class="registro vac"><strong>${esc(v.nombre)}</strong><br>
          Aplicada: ${fmtFecha(v.fechaAplicacion)}${v.proximaDosis ? ' · Próxima dosis: ' + fmtFecha(v.proximaDosis) : ''}<br>
          <small>Dr(a). ${esc(v.veterinarioNombre)}</small></div>`).join('');
  } catch (e) { cont.innerHTML = `<div class="alerta alerta-error">${esc(e.message)}</div>`; }
}

document.getElementById('form-registro').addEventListener('submit', async (e) => {
  e.preventDefault();
  const err = document.getElementById('err-reg');
  err.hidden = true;
  try {
    if (actual.tipo === 'historial') {
      await api('/api/diagnosticos', { metodo: 'POST', cuerpo: {
        mascotaId: actual.id,
        descripcion: document.getElementById('d-desc').value,
        tratamiento: document.getElementById('d-trat').value } });
      aviso('Diagnóstico registrado.');
    } else {
      await api('/api/vacunas', { metodo: 'POST', cuerpo: {
        mascotaId: actual.id,
        nombre: document.getElementById('v-nombre').value,
        fechaAplicacion: document.getElementById('v-fecha').value || null,
        proximaDosis: document.getElementById('v-prox').value || null } });
      aviso('Vacuna registrada.');
    }
    e.target.reset();
    document.getElementById('v-fecha').value = new Date().toISOString().slice(0, 10);
    refrescar();
  } catch (ex) {
    err.textContent = ex.message;
    err.hidden = false;
  }
});
