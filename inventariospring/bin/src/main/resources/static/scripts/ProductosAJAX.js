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

var mapaProductos = new Map();

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
	var nombre = document.getElementById("campoNombre").value.trim();
	var unidades = document.getElementById("campoUnidades").value.trim();
	var patron = new RegExp("^[a-zA-Z0-9 ]*$");	
	var numeros = new RegExp("[0-9]");	
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
		parrafoErrores.innerText = "Debe escribir un nombre para el producto!";
		return false;
	}
	if(nombre.trim() != "" && nombre.length < 3) {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "El nombre del producto debe contener como minimo 3 caracteres!";
		return false;
	}
	
	if(nombre.trim().length > 3 && patron.test(nombre) == false) {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "El nombre solo acepta letras y espacios!";
		return false;
	}
	
	if(unidades.trim() == "") {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "Debe introducir una cantidad de productos!";
		return false;
	}
	if(unidades.trim().length > 0 && numeros.test(unidades) == false) {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "El campo unidades solo acepta números!";
		return false;
	}
	if(unidades < 0) {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "El campo unidades solo acepta números mayores o iguales a 0!";
		return false;
	}
	
	$.ajax({
        url: '/inventario/producto/nombre/' + nombre,
        type: 'GET',
        data: $("#forma-agregar-producto").serialize(),
        success: function( data, textStatus, jQxhr ){
        	if(data.nombreProducto == null) {
        		crearProducto();
        	} else {
        		parrafoErrores.style.display = "inline";
        		parrafoErrores.innerText = "Este producto ya existe!";
        	}
    		return false;
        },
        error: function(jqxhr, status, exception) {
            return false;
        }
    });
	
	return false;
}

function crearProducto() {
	
	var parrafoErrores = document.getElementById("validaciones");
	
	$.ajax({
        url: '/inventario/producto',
        type: 'POST',
        data: $("#forma-agregar-producto").serialize(),
        success: function( data, textStatus, jQxhr ){
        	parrafoErrores.style.display = "inline";
    		parrafoErrores.innerText = "Producto agregado";
    		parrafoErrores.style.color = "blue";
    		setTimeout(
    				function() {
    		    		$('#forma-agregar-producto').trigger("reset");
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

function buscarProducto() {
	
	var lista = document.getElementById("viejaCategoria");
	var nuevaLista = document.getElementById("nuevaCategoria");
	var valor = lista.options[lista.selectedIndex].value;
	var campoNombre = document.getElementById("campoNombre");
	var campoUnidades = document.getElementById("campoUnidades");
	
	if(valor == 0) {
		document.getElementById("forma-editar-producto").reset();
		var i;
        for(i = nuevaLista.options.length - 1 ; i > 0 ; i--)
        {
        	nuevaLista.remove(i);
        }
		return false;
	}
	
	$.ajax({
        url: '/inventario/producto/lista/' + valor,
        type: 'GET',
        data: $("#forma-editar-producto").serialize(),
        success: function( data, textStatus, jQxhr ){
        	var i;
            for(i = nuevaLista.options.length - 1 ; i >= 0 ; i--)
            {
            	nuevaLista.remove(i);
            }

            var option = document.createElement("option");
    		option.text = "Seleccionar";
    		option.value = 0;
    		var select = document.getElementById("nuevaCategoria");
    		select.appendChild(option);
        	
        	for(var i = 0; i < data.length; i++){ 
        		option = document.createElement("option");
        		option.text = titleCase(data[i][1]);
        		option.value = data[i][0];
        		select = document.getElementById("nuevaCategoria");
        		select.appendChild(option);
        		mapaProductos.set(titleCase(data[i][1]), data[i][2]);
        	}

    		return false;
        },
        error: function(jqxhr, status, exception) {
            return false;
        }
    });
}

function cargarCampos() {
	
	var nuevaLista = document.getElementById("nuevaCategoria");
	var texto = nuevaLista.options[nuevaLista.selectedIndex].innerText;
		
	var campoNombre = document.getElementById("campoNombre");
	var campoUnidades = document.getElementById("campoUnidades");
	
	if(texto != "Seleccionar") {
		campoNombre.value = texto;
		campoUnidades.value = mapaProductos.get(texto);
	} else {
		campoNombre.value = "";
		campoUnidades.value = "";
		return false;
	}
	
}

function validarModificacion() {
	
	var lista = document.getElementById("viejaCategoria");
	var texto = lista.options[lista.selectedIndex].innerText;
	var nuevaLista = document.getElementById("nuevaCategoria");
	var nuevoTexto = nuevaLista.options[nuevaLista.selectedIndex].innerText;
	var parrafoErrores = document.getElementById("validaciones");
	var campoNombre = document.getElementById("campoNombre").value.trim();
	var campoUnidades = document.getElementById("campoUnidades").value.trim();
	var patron = new RegExp("^[a-zA-Z0-9 ]*$");	
	var numeros = new RegExp("[0-9]");	
	
	parrafoErrores.style.display = "none";
	parrafoErrores.style.color = "red";
	
	if(texto === "Seleccionar") {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "Debe seleccionar una categoría!";
		return false;
	}
	if(nuevoTexto === "Seleccionar"){
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "Debe seleccionar un producto!";
		return false;
	}
	if(campoNombre == ""){
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "Debe introducir un nombre!";
	}
	if(campoNombre != "" && campoNombre.length < 3){
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "El campo nombre debe contener al menos 3 caracteres!";
	}
	if(campoNombre.length >= 3 && patron.test(campoNombre) == false){
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "El campo nombre solo acepta letras y números!";
	}
	if(campoUnidades == ""){
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "Debe introducir una cantidad!";
	}
	if(campoUnidades != "" && numeros.test(campoUnidades) == false){
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "El campo unidades solo acepta números!";
	}
	if(campoUnidades < 0){
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "El campo unidades solo acepta numeros mayores o igual a 0!";
	}
	
	modificarProducto();
	
	return false;
}

function modificarProducto() {
	
	var nuevaLista = document.getElementById("nuevaCategoria");
	var valor = nuevaLista.options[nuevaLista.selectedIndex].value;
	var parrafoErrores = document.getElementById("validaciones");
	
	$.ajax({
        url: '/inventario/producto/' + valor,
        type: 'PUT',
        data: $("#forma-editar-producto").serialize(),
        success: function( data, textStatus, jQxhr ){
        	parrafoErrores.style.display = "inline";
    		parrafoErrores.innerText = "Producto actualizado";
    		parrafoErrores.style.color = "blue";
    		$('#forma-editar-producto').trigger("reset");
    		
    		var i;
            for(i = nuevaLista.options.length - 1 ; i > 0 ; i--)
            {
            	nuevaLista.remove(i);
            }
    		
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

function validarEliminacion() {
	
	var lista = document.getElementById("viejaCategoria");
	var texto = lista.options[lista.selectedIndex].innerText;
	var nuevaLista = document.getElementById("nuevaCategoria");
	var nuevoTexto = nuevaLista.options[nuevaLista.selectedIndex].innerText;
	var parrafoErrores = document.getElementById("validaciones");
	
	if(texto === "Seleccionar") {
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "Debe seleccionar una categoría!";
		return false;
	}
	if(nuevoTexto === "Seleccionar"){
		parrafoErrores.style.display = "inline";
		parrafoErrores.innerText = "Debe seleccionar un producto!";
		return false;
	}
	
	eliminarProducto();
	
	return false;
}

function eliminarProducto() {
	var nuevaLista = document.getElementById("nuevaCategoria");
	var valor = nuevaLista.options[nuevaLista.selectedIndex].value;
	var parrafoErrores = document.getElementById("validaciones");
	
	$.ajax({
        url: '/inventario/producto/' + valor,
        type: 'DELETE',
        data: $("#forma-editar-producto").serialize(),
        success: function( data, textStatus, jQxhr ){
        	parrafoErrores.style.display = "inline";
    		parrafoErrores.innerText = "Producto eliminado";
    		parrafoErrores.style.color = "orange";
    		$('#forma-editar-producto').trigger("reset");
    		
    		var i;
            for(i = nuevaLista.options.length - 1 ; i > 0 ; i--)
            {
            	nuevaLista.remove(i);
            }
    		
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