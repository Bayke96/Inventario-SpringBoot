package com.inventario.controladores;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.inventario.API.ServicioCategorias;
import com.inventario.excepciones.ExcepcionesPersonalizadas;
import com.inventario.modelos.Categorias;

@RestController
@CrossOrigin
@RequestMapping("/inventario/categoria")
public class CategoriasAPI {
	
	ServicioCategorias servicioCategoria = new ServicioCategorias();
	
	@RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<Categorias> seleccionarCategorias()
    {
		List<Categorias> lista = servicioCategoria.seleccionarCategorias();
        return lista;
    }

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public @ResponseBody Categorias seleccionar(@PathVariable("id") int id)
    {
		Categorias obj = servicioCategoria.seleccionarCategoria(id);
		Categorias categoria = new Categorias();
		
		if(obj.getId() == 0) {
			throw new ExcepcionesPersonalizadas("Categoria no encontrada!");
		}
		
        categoria.setId(obj.getId());
        categoria.setNombreCategoria(obj.getNombreCategoria());
        categoria.setCantidadProductos(obj.getCantidadProductos());
        return categoria;
    }
	
	@RequestMapping(method = RequestMethod.GET, value = "/nombre/{nombre}")
    public @ResponseBody Categorias seleccionar(@PathVariable("nombre") String nombre)
    {
		Categorias obj = servicioCategoria.seleccionarCategoria(nombre);
		Categorias categoria = new Categorias();
			
        categoria.setId(obj.getId());
        categoria.setNombreCategoria(obj.getNombreCategoria());
        categoria.setCantidadProductos(obj.getCantidadProductos());
        return categoria;
    }
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> crear(Categorias categoria) {
		servicioCategoria.agregarCategoria(categoria);
		return new ResponseEntity<>("Categoria creada", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	   public ResponseEntity<Object> actualizar(@PathVariable("id") int id, Categorias categoria) { 
			servicioCategoria.actualizarCategoria(id, categoria);
			return new ResponseEntity<>("Categoria actualizada", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/{nuevoid}", method = RequestMethod.DELETE)
	   public ResponseEntity<Object> eliminar(@PathVariable("id") int id, @PathVariable("nuevoid") int nuevoId) { 
		servicioCategoria.eliminarCategoria(id, nuevoId);
	      return new ResponseEntity<>("Categoria eliminada", HttpStatus.OK);
	}

}
