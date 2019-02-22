package com.inventario.API;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.inventario.modelos.Categorias;
import com.inventario.modelos.Cuentas;
import com.inventario.modelos.Productos;

public class HibernateSession {
	
	public static SessionFactory factory = configurarSession();
	
	public static SessionFactory configurarSession() {
		SessionFactory fact;
		try {
	         fact = new Configuration().
	                   configure().
	                   addAnnotatedClass(Categorias.class).
	                   addAnnotatedClass(Productos.class).
	                   addAnnotatedClass(Cuentas.class).
	                   buildSessionFactory();
	      } catch (Throwable ex) { 
	         System.err.println("Failed to create sessionFactory object." + ex);
	         throw new ExceptionInInitializerError(ex); 
	      }
		return fact;
	}
}
