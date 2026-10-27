// Agrega un botón "ojo" a todos los campos de contraseña para mostrarla u ocultarla.
document.querySelectorAll('input[type="password"]').forEach(input => {
  const envoltura = document.createElement('div');
  envoltura.className = 'campo-pass';
  input.parentNode.insertBefore(envoltura, input);
  envoltura.appendChild(input);

  const boton = document.createElement('button');
  boton.type = 'button';
  boton.className = 'ver-pass';
  boton.textContent = '👁';
  boton.title = 'Mostrar contraseña';
  boton.setAttribute('aria-label', 'Mostrar contraseña');
  envoltura.appendChild(boton);

  boton.addEventListener('click', () => {
    const visible = input.type === 'text';
    input.type = visible ? 'password' : 'text';
    boton.textContent = visible ? '👁' : '🙈';
    boton.title = visible ? 'Mostrar contraseña' : 'Ocultar contraseña';
    boton.setAttribute('aria-label', boton.title);
  });
});