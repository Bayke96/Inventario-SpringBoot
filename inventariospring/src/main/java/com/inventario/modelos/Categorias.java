package com.inventario.modelos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Categorias")
public class Categorias {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Size(min = 3, max = 32)
	@Column(name = "c_nombre", unique = true)
	@NotNull
	private String nombreCategoria;
	
	@Column(name = "c_cantidad")
	@Min(0)
	private int cantidadProductos = 0;
	
	public Categorias() { }
	
	public Categorias(int cid) {
		id = cid;
	}
		
	public Categorias(String nombre, int cantidades) {
		this.nombreCategoria = nombre;
		this.cantidadProductos = cantidades;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNombreCategoria() {
		return this.nombreCategoria;
	}
	
	public void setNombreCategoria(String nombre) {
		this.nombreCategoria = nombre;
	}
	
	public int getCantidadProductos() {
		return this.cantidadProductos;
	}
	
	public void setCantidadProductos(int cantidad) {
		this.cantidadProductos = cantidad;
	}
}
