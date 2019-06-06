/**
 * 
 */

var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

$.ajaxSetup({
    beforeSend: function(xhr) {
    	xhr.setRequestHeader(header, token);
    }
});

function validarRegistro() {
	var parrafoErrores = document.getElementById("validaciones");
	var nombre = document.getElementById("campoUsuario").value.trim();
	var contraseña = document.getElementById("campoContraseña").value.trim();
	var patron = new RegExp("^[a-zA-Z0-9 ]*$");	
	parrafoErrores.style.display = "none";
	parrafoErrores.style.color = "red";
	
	if(nombre.trim() == "") {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "Debe escribir un nombre de usuario!";
		return false;
	}
	if(nombre.trim() != "" && nombre.length < 3) {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "El usuario debe contener como minimo 3 caracteres!";
		return false;
	}
	
	if(nombre.trim().length > 3 && patron.test(nombre) == false) {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "El campo solo acepta letras y espacios!";
		return false;
	}
	
	if(contraseña.trim() == "") {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "Debe introducir una contraseña!";
		return false;
	}
	if(contraseña.trim() != "" && contraseña.length < 12) {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "La contraseña debe contener un minimo de 12 caracteres!";
		return false;
	}
	
	$.ajax({
        url: '/inventario/usuario/' + nombre,
        type: 'GET',
        data: $("#forma-registrar-usuario").serialize(),
        success: function( data, textStatus, jQxhr ){
        	if(data == true) {
        		parrafoErrores.style.display = "inline";
        		parrafoErrores.innerText = "Este usuario ya existe!";
        		return false;
        	} 
    		return false;
    	;},
        error: function(jqxhr, status, exception) {
        	if(jqxhr.status == 500){
        		alert("Cuenta registrada exitosamente");
        		document.getElementById("forma-registrar-usuario").submit();
        		return true;
        	}
            return false;
        }
    });
	return false;
}

function ingresarUsuario() {
	
	var usuario = document.getElementById("campoUsuario").value.trim();
	var contraseña = document.getElementById("campoContraseña").value.trim();
	var patron = new RegExp("^[a-zA-Z0-9 ]*$");	
	var parrafoErrores = document.getElementById("validaciones");
	parrafoErrores.style.display = "none";
	parrafoErrores.style.color = "red";
	
	if(usuario.trim() == "") {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "Debe escribir un nombre de usuario!";
		return false;
	}
	if(usuario.trim() != "" && usuario.length < 3) {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "El usuario debe contener como minimo 3 caracteres!";
		return false;
	}
	
	if(usuario.trim().length > 3 && patron.test(usuario) == false) {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "El usuario solo acepta letras y espacios!";
		return false;
	}
	
	if(contraseña.trim() == "") {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "Debe introducir una contraseña!";
		return false;
	}
	if(contraseña.trim() != "" && contraseña.length < 12) {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "La contraseña debe contener un minimo de 12 caracteres!";
		return false;
	}
}

function validarContraseñas() {
	
	var viejaContraseña = document.getElementById("viejaContraseña").value.trim();
	var nuevaContraseña = document.getElementById("nuevaContraseña").value.trim();
	var repetirContraseña = document.getElementById("repetirContraseña").value.trim();
	var parrafoErrores = document.getElementById("validaciones");
	parrafoErrores.style.display = "none";
	parrafoErrores.style.color = "red";
	
	
	if(viejaContraseña == ""){
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "Debe introducir la antigua contraseña!";
		return false;
	}
	if(viejaContraseña != "" && viejaContraseña.length < 12){
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "La vieja contraseña debe contener al menos 12 carácteres!";
		return false;
	}
	
	if(nuevaContraseña == ""){
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "Debe introducir una nueva contraseña!";
		return false;
	}
	if(nuevaContraseña != "" && nuevaContraseña.length < 12){
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "La nueva contraseña debe contener al menos 12 carácteres!";
		return false;
	}
	if(nuevaContraseña === viejaContraseña) {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "La nueva contraseña no puede ser identica a la anterior!";
		return false;
	}
	
	if(repetirContraseña == ""){
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "Debe repetir la nueva contraseña!";
		return false;
	}
	if(repetirContraseña != "" && repetirContraseña.length < 12){
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "La contraseña repetida debe contener al menos 12 carácteres!";
		return false;
	}
	if(nuevaContraseña != repetirContraseña) {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "La contraseña en el campo repetir no es identica a la nueva contraseña!";
		return false;
	}
	
	$.ajax({
        url: '/inventario/usuario/verificarcontraseña/' + viejaContraseña,
        type: 'POST',
        data: $("#forma-cambiar-contraseña").serialize(),
        success: function( data, textStatus, jQxhr ){
        	if(data == true) {
        		document.getElementById("forma-cambiar-contraseña").submit();
        		alert("Su contraseña ha sido actualizada, ahora debe reconectarse usando la nueva contraseña");
        		return false;
        	}
        	if(data == false){
        		parrafoErrores.style.display = "inline";
        		parrafoErrores.innerText = "Contraseña incorrecta!";
        	}
    		return false;
    	;},
        error: function(jqxhr, status, exception) {
            return false;
        }
    });
	
	return false;
}