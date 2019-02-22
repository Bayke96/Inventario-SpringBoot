package com.inventario.controladores;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.inventario.API.ServicioCuentas;
import com.inventario.modelos.Cuentas;

@Controller
public class CuentasControlador {
	
	CuentasAPI api = new CuentasAPI();
	ServicioCuentas servicio = new ServicioCuentas();
	
	@GetMapping(value = "/error-ingreso")
	public String errorLogin() {
		return "ErrorLogin";
	}

	@RequestMapping(method = RequestMethod.GET, value = { "", "/", "/login" })
	public String ingresar(Model modelo, Principal principal) {
		 	if (principal != null && ((Authentication) principal).isAuthenticated()) {
		        return "forward:/productos/1";
		    }
			modelo.addAttribute("usuario", new Cuentas());
			return "IngresarUsuario";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = { "", "/", "/login" })
	public ModelAndView loginToken(Cuentas usuario) {
		boolean login = servicio.loginUsuario(usuario);
		if(login == true) {
			return new ModelAndView("redirect:/productos/1");
		} else {
			return new ModelAndView("redirect:/error-ingreso");
		}
		
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/registrarse")
	public String registrarse(Model modelo) {
		modelo.addAttribute("usuario", new Cuentas());
		return "RegistrarUsuario";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/registrarse")
	public ModelAndView registrarUsuario(Cuentas usuario) {
		api.crear(usuario);
		return new ModelAndView("redirect:/login");
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/usuario/cambiarcontraseña")
	public String cambiarContraseña(Model modelo) {
		modelo.addAttribute("usuario", new Cuentas());
		return "CambiarContraseña";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/usuario/cambiarcontraseña")
	public ModelAndView actualizarContraseña(HttpServletRequest request, HttpServletResponse response, Principal principal, 
			@RequestParam String viejaContraseña, Cuentas cuenta) {
		
		servicio.cambiarContraseña(principal.getName(), viejaContraseña, cuenta.getContraseña());
		logOut(request, response);
		return new ModelAndView("redirect:/login");
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/salir")
	public ModelAndView logOut() {
		return new ModelAndView("redirect:/login");
	}
	
	private void logOut(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
          if (auth != null){    
             new SecurityContextLogoutHandler().logout(request, response, auth);
          }
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
