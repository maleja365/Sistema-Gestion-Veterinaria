let mascotas = [];
let editandoId = null;

document.querySelectorAll('[data-cerrar]').forEach(b =>
  b.addEventListener('click', () => b.closest('dialog').close()));

(async function iniciar() {
  const usuario = await exigirRol('PROPIETARIO');
  if (!usuario) return;
  pintarUsuario(usuario);
  iniciarMenu({ solicitar: cargarFormularioCita, citas: cargarCitas });
  await cargarMascotas();
})();

/* ---------------- Mascotas (registrar / actualizar / eliminar) ---------------- */
async function cargarMascotas() {
  try {
    mascotas = await api('/api/mascotas');
  } catch (e) { return aviso(e.message, true); }

  const cont = document.getElementById('lista-mascotas');
  if (!mascotas.length) {
    cont.innerHTML = vacio('Aún no has registrado mascotas. ¡Registra a la primera!', 'perro');
    return;
  }
  cont.innerHTML = mascotas.map(m => `
    <article class="mascota">
      <img src="${imgMascota(m.especie)}" alt="${esc(m.especie)}">
      <h4>${esc(m.nombre)}</h4>
      <div class="dato">${esc(m.especie)}${m.raza ? ' · ' + esc(m.raza) : ''}</div>
      <div class="dato">${edad(m.fechaNacimiento)}${m.sexo ? ' · ' + esc(m.sexo) : ''}</div>
      <div class="acciones">
        <button class="btn btn-sm btn-suave" onclick="verClinico(${m.id},'historial')">Historial</button>
        <button class="btn btn-sm btn-suave" onclick="verClinico(${m.id},'vacunas')">Vacunas</button>
        <button class="btn btn-sm btn-suave" onclick="editar(${m.id})">Editar</button>
        <button class="btn btn-sm btn-peligro" onclick="eliminar(${m.id})">Eliminar</button>
      </div>
    </article>`).join('');
}

const dlgMascota = document.getElementById('dlg-mascota');
const errMascota = document.getElementById('err-mascota');

document.getElementById('btn-nueva').addEventListener('click', () => {
  editandoId = null;
  document.getElementById('form-mascota').reset();
  actualizarOtra();
  document.getElementById('t-mascota').textContent = 'Registrar mascota';
  errMascota.hidden = true;
  dlgMascota.showModal();
});

const LISTA_ESPECIES = ['Perro', 'Gato', 'Conejo', 'Loro', 'Hámster', 'Pez', 'Iguana'];
const selEspecie = document.getElementById('m-especie');
const inpOtra = document.getElementById('m-otra');
const cajaOtra = document.getElementById('caja-otra');

function actualizarOtra() {
  const esOtro = selEspecie.value === 'Otro';
  cajaOtra.hidden = !esOtro;
  inpOtra.required = esOtro;
  if (!esOtro) inpOtra.value = '';
}
selEspecie.addEventListener('change', actualizarOtra);

function editar(id) {
  const m = mascotas.find(x => x.id === id);
  editandoId = id;
  document.getElementById('t-mascota').textContent = 'Actualizar mascota';
  document.getElementById('m-nombre').value = m.nombre;
  if (LISTA_ESPECIES.includes(m.especie)) {
    selEspecie.value = m.especie;
  } else {
    selEspecie.value = 'Otro';
    inpOtra.value = m.especie;
  }
  actualizarOtra();
  document.getElementById('m-raza').value = m.raza || '';
  document.getElementById('m-sexo').value = m.sexo || '';
  document.getElementById('m-nacimiento').value = m.fechaNacimiento || '';
  errMascota.hidden = true;
  dlgMascota.showModal();
}

document.getElementById('form-mascota').addEventListener('submit', async (e) => {
  e.preventDefault();
  errMascota.hidden = true;
  const cuerpo = {
    nombre: document.getElementById('m-nombre').value,
    especie: selEspecie.value === 'Otro' ? inpOtra.value.trim() : selEspecie.value,
    raza: document.getElementById('m-raza').value,
    sexo: document.getElementById('m-sexo').value,
    fechaNacimiento: document.getElementById('m-nacimiento').value || null
  };
  try {
    await api(editandoId ? `/api/mascotas/${editandoId}` : '/api/mascotas',
      { metodo: editandoId ? 'PUT' : 'POST', cuerpo });
    dlgMascota.close();
    aviso(editandoId ? 'Mascota actualizada.' : 'Mascota registrada.');
    cargarMascotas();
  } catch (err) {
    errMascota.textContent = err.message;
    errMascota.hidden = false;
  }
});

async function eliminar(id) {
  const m = mascotas.find(x => x.id === id);
  if (!confirm(`¿Eliminar a ${m.nombre}? También se borrarán sus citas, historial y vacunas.`)) return;
  try {
    await api(`/api/mascotas/${id}`, { metodo: 'DELETE' });
    aviso('Mascota eliminada.');
    cargarMascotas();
  } catch (e) { aviso(e.message, true); }
}

/* ---------------- Consultar historial médico y vacunas ---------------- */
async function verClinico(id, tipo) {
  const m = mascotas.find(x => x.id === id);
  document.getElementById('cl-img').src = imgMascota(m.especie);
  document.getElementById('cl-titulo').textContent =
    `${tipo === 'historial' ? 'Historial médico' : 'Vacunas'} de ${m.nombre}`;
  const cont = document.getElementById('cl-contenido');
  cont.innerHTML = '<p class="vacio">Cargando…</p>';
  document.getElementById('dlg-clinico').showModal();
  try {
    const datos = await api(`/api/mascotas/${id}/${tipo}`);
    if (!datos.length) {
      cont.innerHTML = vacio(tipo === 'historial' ? 'Todavía no hay diagnósticos.' : 'Todavía no hay vacunas registradas.', 'gato');
      return;
    }
    cont.innerHTML = tipo === 'historial'
      ? datos.map(d => `<div class="registro"><strong>${esc(d.descripcion)}</strong><br>
          ${d.tratamiento ? 'Tratamiento: ' + esc(d.tratamiento) + '<br>' : ''}
          <small>${fmtFechaHora(d.fecha)} · Dr(a). ${esc(d.veterinarioNombre)}</small></div>`).join('')
      : datos.map(v => `<div class="registro vac"><strong>${esc(v.nombre)}</strong><br>
          Aplicada: ${fmtFecha(v.fechaAplicacion)}${v.proximaDosis ? ' · Próxima dosis: ' + fmtFecha(v.proximaDosis) : ''}<br>
          <small>Dr(a). ${esc(v.veterinarioNombre)}</small></div>`).join('');
  } catch (e) { cont.innerHTML = `<div class="alerta alerta-error">${esc(e.message)}</div>`; }
}

/* ---------------- Solicitar cita ---------------- */
async function cargarFormularioCita() {
  const selM = document.getElementById('c-mascota');
  const selV = document.getElementById('c-vet');
  try {
    const vets = await api('/api/veterinarios');
    selM.innerHTML = mascotas.length
      ? mascotas.map(m => `<option value="${m.id}">${esc(m.nombre)} (${esc(m.especie)})</option>`).join('')
      : '<option value="">Primero registra una mascota</option>';
    selV.innerHTML = vets.length
      ? vets.map(v => `<option value="${v.id}">Dr(a). ${esc(v.nombre)}</option>`).join('')
      : '<option value="">No hay veterinarios disponibles</option>';
  } catch (e) { aviso(e.message, true); }
}

document.getElementById('form-cita').addEventListener('submit', async (e) => {
  e.preventDefault();
  const err = document.getElementById('err-cita');
  err.hidden = true;
  try {
    await api('/api/citas', {
      metodo: 'POST',
      cuerpo: {
        mascotaId: Number(document.getElementById('c-mascota').value) || null,
        veterinarioId: Number(document.getElementById('c-vet').value) || null,
        fechaHora: document.getElementById('c-fecha').value,
        motivo: document.getElementById('c-motivo').value
      }
    });
    e.target.reset();
    aviso('Cita solicitada. El veterinario la confirmará.');
    document.querySelector('.menu[data-vista="citas"]').click();
  } catch (ex) {
    err.textContent = ex.message;
    err.hidden = false;
  }
});

/* ---------------- Mis citas ---------------- */
async function cargarCitas() {
  const cont = document.getElementById('tabla-citas');
  try {
    const citas = await api('/api/citas');
    if (!citas.length) { cont.innerHTML = vacio('No tienes citas todavía.', 'conejo'); return; }
    cont.innerHTML = `<table><thead><tr><th>Mascota</th><th>Veterinario</th><th>Fecha</th><th>Motivo</th><th>Estado</th></tr></thead><tbody>` +
      citas.map(c => `<tr>
        <td><img class="mini" src="${imgMascota(c.mascotaEspecie)}" alt="">${esc(c.mascotaNombre)}</td>
        <td>Dr(a). ${esc(c.veterinarioNombre)}</td>
        <td>${fmtFechaHora(c.fechaHora)}</td><td>${esc(c.motivo)}</td>
        <td><span class="estado estado-${c.estado}">${c.estado}</span></td></tr>`).join('') +
      `</tbody></table>`;
  } catch (e) { cont.innerHTML = `<div class="alerta alerta-error">${esc(e.message)}</div>`; }
}