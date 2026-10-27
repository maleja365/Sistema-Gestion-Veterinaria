/* Utilidades de interfaz compartidas */
const esc = (t) => String(t ?? '').replace(/[&<>"']/g, c =>
  ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));

const IMAGEN_ESPECIE = {
  perro: 'perro', gato: 'gato', conejo: 'conejo',
  loro: 'loro', hamster: 'hamster', pez: 'pez', iguana: 'iguana'
};
function imgMascota(especie) {
  const limpia = (especie || '').normalize('NFD').replace(/[\u0300-\u036f]/g, '').toLowerCase().trim();
  const clave = IMAGEN_ESPECIE[limpia] || 'otro';
  return `img/${clave}.svg`;
}

function edad(fecha) {
  if (!fecha) return 'Edad no registrada';
  const n = new Date(fecha + 'T00:00:00'), h = new Date();
  let meses = (h.getFullYear() - n.getFullYear()) * 12 + h.getMonth() - n.getMonth();
  if (h.getDate() < n.getDate()) meses--;
  if (meses < 1) return 'Recién nacido';
  if (meses < 12) return `${meses} ${meses === 1 ? 'mes' : 'meses'}`;
  const a = Math.floor(meses / 12);
  return `${a} ${a === 1 ? 'año' : 'años'}`;
}

const fmtFechaHora = (iso) => new Date(iso).toLocaleString('es-CO',
  { dateStyle: 'medium', timeStyle: 'short' });
const fmtFecha = (iso) => iso
  ? new Date(iso + (iso.length === 10 ? 'T00:00:00' : '')).toLocaleDateString('es-CO', { dateStyle: 'medium' })
  : '—';

let temporizadorAviso;
function aviso(texto, esError = false) {
  let el = document.getElementById('aviso');
  if (!el) { el = document.createElement('div'); el.id = 'aviso'; document.body.appendChild(el); }
  el.textContent = texto;
  el.className = esError ? 'error' : '';
  el.hidden = false;
  clearTimeout(temporizadorAviso);
  temporizadorAviso = setTimeout(() => el.hidden = true, 4200);
}

/** Cambia de sección en los paneles (menú lateral). */
function iniciarMenu(alMostrar = {}) {
  const botones = document.querySelectorAll('.menu[data-vista]');
  const mostrar = (id) => {
    botones.forEach(b => b.classList.toggle('activo', b.dataset.vista === id));
    document.querySelectorAll('.vista').forEach(v => v.hidden = v.id !== 'vista-' + id);
    if (alMostrar[id]) alMostrar[id]();
  };
  botones.forEach(b => b.addEventListener('click', () => mostrar(b.dataset.vista)));
  mostrar(botones[0].dataset.vista);
  return mostrar;
}

function pintarUsuario(u) {
  document.getElementById('u-nombre').textContent = u.nombre;
  document.getElementById('u-avatar').textContent = u.nombre.charAt(0).toUpperCase();
  document.getElementById('btn-salir').addEventListener('click', cerrarSesion);
}

function vacio(texto, especie = 'otro') {
  return `<div class="vacio"><img src="img/${especie}.svg" alt=""><p>${esc(texto)}</p></div>`;
}