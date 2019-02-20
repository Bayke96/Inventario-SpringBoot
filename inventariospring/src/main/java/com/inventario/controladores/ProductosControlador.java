package com.inventario.controladores;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.inventario.modelos.Productos;

@Controller
public class ProductosControlador {
	
	CategoriasAPI api = new CategoriasAPI();
	ProductosAPI productosAPI = new ProductosAPI();
	
	@RequestMapping(value = "/productos/{pag}", method = RequestMethod.GET)
	public String inventario(Principal principal, Model modelo, @PathVariable("pag") int pagina) {
		String nombreUsuario = principal.getName();
		String nombre = nombreUsuario.substring(0,1).toUpperCase() + nombreUsuario.substring(1).toLowerCase();
		modelo.addAttribute("listaProductos", productosAPI.paginacionProductos(pagina));
		modelo.addAttribute("listaCategorias", api.seleccionarCategorias());
		modelo.addAttribute("cantidadProductos", productosAPI.contadorProductos());
		modelo.addAttribute("paginaActual", pagina);
		modelo.addAttribute("nombreUsuario", nombre);
		return "Inventario";
	}
	
	@RequestMapping(value = "/producto/crear", method = RequestMethod.GET)
	public String crearProducto(Model modelo) {
		modelo.addAttribute("producto", new Productos());
		modelo.addAttribute("listaCategorias", api.seleccionarCategorias());
		return "CrearProducto";
	}
	
	@RequestMapping(value = "/producto/editar", method = RequestMethod.GET)
	public String editarProducto(Model modelo) {
		modelo.addAttribute("editarProducto", new Productos());
		modelo.addAttribute("listaCategorias", api.seleccionarCategorias());
		return "EditarProducto";
	}
	
	@RequestMapping(value = "/producto/eliminar", method = RequestMethod.GET)
	public String eliminarProducto(Model modelo) {
		modelo.addAttribute("eliminarProducto", new Productos());
		modelo.addAttribute("listaCategorias", api.seleccionarCategorias());
		return "EliminarProducto";
	}

}
