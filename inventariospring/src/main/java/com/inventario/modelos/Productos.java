package com.inventario.modelos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Productos")
public class Productos {
	
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@ManyToOne
	@NotNull
	@JoinColumn(name = "FK_categoria")
	private Categorias cid;
	
	@Size(min = 3, max = 48)
	@Column(name = "p_nombre", unique = true)
	@NotNull
	private String nombreProducto;
	
	@Min(0)
	@NotNull
	@Column(name = "p_unidades")
	private int unidadesProducto;
	
	public Productos() { 
		this.unidadesProducto = 0;
	}
	
	public Productos(int categoria, String nombre, int unidades) {
		this.cid = new Categorias(categoria);
		this.nombreProducto = nombre;
		this.unidadesProducto = unidades;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Categorias getcid() {
		return this.cid;
	}
	
	public void setcid(Categorias cid) {
		this.cid = cid;
	}
	
	public String getNombreProducto() {
		return this.nombreProducto;
	}
	
	public void setNombreProducto(String nombre) {
		this.nombreProducto = nombre;
	}
	
	public int getUnidadesProducto() {
		return this.unidadesProducto;
	}
	
	public void setUnidadesProducto(int cantidades) {
		this.unidadesProducto = cantidades;
	}

}
