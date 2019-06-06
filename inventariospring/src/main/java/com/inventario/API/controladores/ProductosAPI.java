package com.inventario.API.controladores;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.inventario.API.ServicioProductos;
import com.inventario.modelos.Productos;

@RestController
@RequestMapping("/inventario/producto")
public class ProductosAPI {
	
	ServicioProductos servicioProducto = new ServicioProductos();
	
	@RequestMapping(method = RequestMethod.GET, value = "/conteo")
	public @ResponseBody int contadorProductos() {
		return servicioProducto.contadorProductos();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public @ResponseBody Productos seleccionar(@PathVariable("id") int id)
    {
		Productos obj = servicioProducto.seleccionarProducto(id);
		Productos producto = new Productos();

		producto.setId(obj.getId());
		producto.setNombreProducto(obj.getNombreProducto());
		producto.setUnidadesProducto(obj.getUnidadesProducto());
		
        return producto;
    }
	
	@RequestMapping(method = RequestMethod.GET, value = "/nombre/{nombre}")
    public @ResponseBody Productos seleccionar(@PathVariable("nombre") String nombre)
    {
		Productos obj = servicioProducto.seleccionarProducto(nombre);
		Productos producto = new Productos();

		producto.setId(obj.getId());
		producto.setNombreProducto(obj.getNombreProducto());
		producto.setUnidadesProducto(obj.getUnidadesProducto());
		
        return producto;
    }
	
	@RequestMapping(method = RequestMethod.GET, value = "/paginacion/{pagina}")
    public @ResponseBody List<Productos> paginacionProductos(@PathVariable("pagina") int pagina)
    {
		List<Productos> lista = servicioProducto.paginacionProductos(pagina);
        return lista;
    }
	
	@RequestMapping(method = RequestMethod.GET, value = "/lista/{FK}")
    public @ResponseBody List<Productos> listaProductos(@PathVariable("FK") int FK)
    {
		List<Productos> lista = servicioProducto.listaProductos(FK);
        return lista;
    }
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> crear(Productos producto) {
		servicioProducto.agregarProducto(producto);
		return new ResponseEntity<>("Producto creado", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	   public ResponseEntity<Object> actualizar(@PathVariable("id") int id, Productos producto) { 
		servicioProducto.actualizarProducto(id, producto);
			return new ResponseEntity<>("Producto actualizado", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	   public ResponseEntity<Object> eliminar(@PathVariable("id") int id) { 
		servicioProducto.eliminarProducto(id);
	      return new ResponseEntity<>("Producto eliminado", HttpStatus.OK);
	}

}
