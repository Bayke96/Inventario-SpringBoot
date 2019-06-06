package com.inventario.API.controladores;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inventario.API.ServicioCuentas;
import com.inventario.modelos.Cuentas;


@RestController
@RequestMapping("/inventario/usuario")
public class CuentasAPI {
		
	ServicioCuentas servicioCuentas = new ServicioCuentas();
	
	@RequestMapping(method = RequestMethod.GET, value = "/{nombre}")
	public boolean buscar(@PathVariable("nombre") String nombre) {
		return servicioCuentas.seleccionarUsuario(nombre);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> crear(Cuentas usuario) {
		servicioCuentas.agregarUsuario(usuario);
		return new ResponseEntity<>("Usuario creado", HttpStatus.CREATED);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/verificarcontraseña/{pass}")
	public boolean verificarContraseña(@PathVariable("pass") String pass, Principal principal) {
		return servicioCuentas.verificarContraseña(principal.getName(), pass.trim());
	}
	
}
