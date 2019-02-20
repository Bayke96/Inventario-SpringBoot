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

function titleCase(str) {
	   var splitStr = str.toLowerCase().split(' ');
	   for (var i = 0; i < splitStr.length; i++) {
	       // You do not need to check if i is larger than splitStr length, as your for does that for you
	       // Assign it back to the array
	       splitStr[i] = splitStr[i].charAt(0).toUpperCase() + splitStr[i].substring(1);     
	   }
	   // Directly return the joined string
	   return splitStr.join(' '); 
}

function validarAdicion() {
	
	var parrafoErrores = document.getElementById("validaciones");
	var nombre = document.getElementById("nombreCategoria").value.trim();
	var patron = new RegExp('^[a-zA-ZÀ-ÿ\u00f1\u00d1 ]+(\s*[a-zA-ZÀ-ÿ\u00f1\u00d1 ]*)*[a-zA-ZÀ-ÿ\u00f1\u00d1 ]+$');	
	parrafoErrores.style.display = "none";
	parrafoErrores.style.color = "red";
	
	if(nombre.trim() == "") {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "Debe escribir una categoría!";
		return false;
	}
	if(nombre.trim() != "" && nombre.length < 3) {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "La categoría debe contener como minimo 3 caracteres!";
		return false;
	}
	
	if(nombre.trim().length > 3 && patron.test(nombre) == false) {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "El campo solo acepta letras y espacios!";
		return false;
	}
	
	$.ajax({
        url: '/inventario/categoria/nombre/' + nombre,
        type: 'GET',
        data: $("#forma-registrar-categoria").serialize(),
        success: function( data, textStatus, jQxhr ){
        	console.log(data);
        	if(data.nombreCategoria == null) {
        		crearCategoria();
        	} else {
        		parrafoErrores.style.display = "inline";
            	parrafoErrores.innerText = "Esta categoría ya existe!";
        	}
    		return false;
        },
        error: function(jqxhr, status, exception) {
            return false;
        }
    });
	return false;
}

function validarModificacion() {
	
	var parrafoErrores = document.getElementById("validaciones");
	var nombre = document.getElementById("nuevoNombreCategoria").value.trim();
	var patron = new RegExp('^[a-zA-ZÀ-ÿ\u00f1\u00d1 ]+(\s*[a-zA-ZÀ-ÿ\u00f1\u00d1 ]*)*[a-zA-ZÀ-ÿ\u00f1\u00d1 ]+$');	
	var lista = document.getElementById("seleccionCategoria");
	var texto = lista.options[lista.selectedIndex].innerText;
	
	parrafoErrores.style.display = "none";
	parrafoErrores.style.color = "red";
	
	if(texto == "Seleccionar") {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "Debe seleccionar una categoría!";
		return false;
	}
	if(nombre.trim() == "") {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "Debe escribir un nuevo nombre!";
		return false;
	}
	if(nombre.trim() != "" && nombre.length < 3) {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "El nombre debe contener como minimo 3 caracteres!";
		return false;
	}
	if(nombre.trim().length > 3 && patron.test(nombre) == false) {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "El campo solo acepta letras y espacios!";
		return false;
	}
	if(nombre.toUpperCase() === texto.toUpperCase()) {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "El nuevo nombre no puede ser el mismo al anterior!";
		return false;
	}
	
	$.ajax({
        url: '/inventario/categoria/nombre/' + nombre,
        type: 'GET',
        data: $("#forma-editar-categoria").serialize(),
        success: function( data, textStatus, jQxhr ){
        	if(data.nombreCategoria != null && nombre.toUpperCase() != texto.toUpperCase()) {
        		parrafoErrores.style.display = "inline";
        		parrafoErrores.innerText = "Esta categoría ya existe!";
        	} 
        	if(data.nombreCategoria == null) {
        		editarCategoria();
        	}
    		return false;
        },
        error: function(jqxhr, status, exception) {
            return false;
        }
    });
	return false;
}

function validarEliminacion() {
	
	var parrafoErrores = document.getElementById("validaciones");
	var lista = document.getElementById("seleccionViejaCategoria");
	var texto = lista.options[lista.selectedIndex].innerText;
	
	var lista2 = document.getElementById("seleccionNuevaCategoria");
	var texto2 = lista2.options[lista2.selectedIndex].innerText;
		
	parrafoErrores.style.display = "none";
	parrafoErrores.style.color = "red";
	
	if(texto == "Seleccionar") {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "Debe seleccionar una categoría!";
		return false;
	}
	if(texto2 == "Seleccionar") {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "Debe seleccionar una nueva categoría!";
		return false;
	}
	if(texto === texto2){
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "La nueva categoría no puede ser la misma que la antigua!";
		return false;
	}

	eliminarCategoria();
	
	return false;
}

function crearCategoria() {
		
	var parrafoErrores = document.getElementById("validaciones");
	
	$.ajax({
        url: '/inventario/categoria',
        type: 'POST',
        data: $("#forma-registrar-categoria").serialize(),
        success: function( data, textStatus, jQxhr ){
        	parrafoErrores.style.display = "inline";
    		parrafoErrores.innerText = "Categoría creada";
    		parrafoErrores.style.color = "blue";
    		setTimeout(
    				function() {
    		    		$('#forma-registrar-categoria').trigger("reset");
    			    }, 1200);
    		setTimeout(
    				function() {
    					parrafoErrores.style.display = "none";
    			    }, 3000);
    		return false;
        },
        error: function(jqxhr, status, exception) {
            return false;
        }
    });
	
	return false;
}

function editarCategoria() {
	
	var parrafoErrores = document.getElementById("validaciones");
	var lista = document.getElementById("seleccionCategoria");
	var valor = lista.options[lista.selectedIndex].value;
	
	$.ajax({
        url: '/inventario/categoria/' + valor,
        type: 'PUT',
        data: $("#forma-editar-categoria").serialize(),
        success: function( data, textStatus, jQxhr ){
        	parrafoErrores.style.display = "inline";
    		parrafoErrores.innerText = "Categoría actualizada";
    		parrafoErrores.style.color = "blue";
    		lista.options[lista.selectedIndex].innerText = titleCase(document.getElementById("nuevoNombreCategoria").value.trim());
    		setTimeout(
    				function() {
    		    		$('#forma-editar-categoria').trigger("reset");
    			    }, 1200);
    		setTimeout(
    				function() {
    					parrafoErrores.style.display = "none";
    			    }, 3000);
    		return false;
        },
        error: function(jqxhr, status, exception) {
            return false;
        }
    });
	
	return false;
}

function eliminarCategoria() {
	
	var parrafoErrores = document.getElementById("validaciones");
	var lista = document.getElementById("seleccionViejaCategoria");	
	var lista2 = document.getElementById("seleccionNuevaCategoria");
	
	var valor = lista.options[lista.selectedIndex].value;
	var valor2 = lista2.options[lista2.selectedIndex].value;
	
	$.ajax({
        url: '/inventario/categoria/' + valor + '/' + valor2,
        type: 'DELETE',
        data: $("#forma-eliminar-categoria").serialize(),
        success: function( data, textStatus, jQxhr ){
        	parrafoErrores.style.display = "inline";
    		parrafoErrores.innerText = "Categoría eliminada";
    		parrafoErrores.style.color = "orange";
    		
    		for (var i=0; i<lista2.length; i++){
   			if (lista2.options[i].value === valor )
   				 lista2.remove(i);
   			}
    		lista.remove(lista.selectedIndex); 
    		
    		setTimeout(
    				function() {
    		    		$('#forma-eliminar-categoria').trigger("reset");
    			    }, 1200);
    		setTimeout(
    				function() {
    					parrafoErrores.style.display = "none";
    			    }, 3000);
    		return false;
        },
        error: function(jqxhr, status, exception) {
            return false;
        }
    });
	
	return false;
}
