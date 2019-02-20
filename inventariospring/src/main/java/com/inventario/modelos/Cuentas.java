package com.inventario.modelos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;


@Entity
@Table(name = "Cuentas")
public class Cuentas {
	
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@NotNull
	@Size(min = 3)
	@Column(name = "acc_usuario", length = 32, unique = true)
	private String usuario;
		
	@NotNull
	@Size(min = 12, max = 128)
	@Column(name = "acc_contraseña", length = 128)
	@JsonIgnore
	@JsonProperty(access = Access.WRITE_ONLY)
	private String contraseña;

	public Cuentas() { }
	
	public Cuentas(String usuario, String contraseña) {
		this.usuario = usuario;
		this.contraseña = contraseña;
	}
	
	public Cuentas(int id, String usuario, String contraseña) {
		this.id = id;
		this.usuario = usuario;
		this.contraseña = contraseña;
	}

	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUsuario() {
		return this.usuario;
	}
	
	public void setUsuario(String nombre) {
		this.usuario = nombre;
	}
	
	public String getContraseña() {
		return this.contraseña;
	}
	
	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

}
