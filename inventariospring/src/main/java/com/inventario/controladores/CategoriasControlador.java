package com.inventario.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.inventario.API.controladores.CategoriasAPI;
import com.inventario.modelos.Categorias;

@Controller
public class CategoriasControlador {
	
	CategoriasAPI api = new CategoriasAPI();

	@RequestMapping(value = "/categoria/crear", method = RequestMethod.GET)
	public String nuevaCategoria(Model modelo) {
		modelo.addAttribute("categoria", new Categorias());
		return "CrearCategoria";
	}
	
	@RequestMapping(value = "/categoria/editar", method = RequestMethod.GET)
	public String editarCategoria(Model modelo) {
		modelo.addAttribute("actualizarCategoria", new Categorias());
		modelo.addAttribute("listaCategorias", api.seleccionarCategorias());
		return "EditarCategoria";
	}
	
	@RequestMapping(value = "/categoria/eliminar", method = RequestMethod.GET)
	public String eliminarCategoria(Model modelo) {
		modelo.addAttribute("eliminarCategoria", new Categorias());
		modelo.addAttribute("listaCategorias", api.seleccionarCategorias());
		return "EliminarCategoria";
	}
}
