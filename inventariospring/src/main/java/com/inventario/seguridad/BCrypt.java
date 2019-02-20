package com.inventario.seguridad;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCrypt {

	public String encriptarContraseña(String contraseña) {
		BCryptPasswordEncoder codificador = new BCryptPasswordEncoder(12);
		String contraseñaTransformada = codificador.encode(contraseña);
		return contraseñaTransformada;
	}
	
	public boolean verificarContraseña(String contraseñaA, String contraseñaB) {
		boolean resultado = false;
		BCryptPasswordEncoder codificador = new BCryptPasswordEncoder(12);
		resultado = codificador.matches(contraseñaA, contraseñaB);
		return resultado;
	}
}
