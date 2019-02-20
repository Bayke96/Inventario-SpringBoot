package com.inventario;

import org.hibernate.cfg.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.inventario.API.HibernateSession;

import com.inventario.modelos.Categorias;
import com.inventario.modelos.Cuentas;
import com.inventario.modelos.Productos;

@SpringBootApplication
@EnableWebSecurity

public class InventariospringApplication {
	
	public static void main(String[] args) {
		
		try {
	         HibernateSession.factory = new Configuration().
	                   configure().
	                   //addPackage("com.xyz") //add package if used.
	                   addAnnotatedClass(Categorias.class).
	                   addAnnotatedClass(Productos.class).
	                   addAnnotatedClass(Cuentas.class).
	                   buildSessionFactory();
	      } catch (Throwable ex) { 
	         System.err.println("Failed to create sessionFactory object." + ex);
	         throw new ExceptionInInitializerError(ex); 
	      }
		
		SpringApplication.run(InventariospringApplication.class, args);
	}

}
