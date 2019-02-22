package com.inventario.API;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.springframework.stereotype.Service;

import com.inventario.modelos.Cuentas;
import com.inventario.seguridad.BCrypt;

@Service
public class ServicioCuentas {

	public static SessionFactory factory = HibernateSession.factory;
	
	public boolean seleccionarUsuario(String usuario) {
		boolean resultado = false;
		Session session = factory.openSession();
		Transaction tx = null;
	      try {
	    	 tx = session.beginTransaction();
	    	 Query query = session.createQuery("SELECT c.usuario FROM Cuentas c WHERE UPPER(c.usuario) = :usuario");
	    	 query.setParameter("usuario", usuario.toUpperCase().trim());
	    	 String nombre = query.getSingleResult().toString();
	    	 if(usuario.equalsIgnoreCase(nombre)) {
	    		 resultado = true;
	    	 }
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
		return resultado;
	}
		
	public boolean loginUsuario(Cuentas usuario) {
		List lista = new ArrayList();
		BCrypt encriptado = new BCrypt();
		boolean resultado = false;
		Session session = factory.openSession();
		Transaction tx = null;
	      try {
	    	 tx = session.beginTransaction();
	    	 Query query = session.createQuery("SELECT c.contraseña FROM Cuentas c WHERE UPPER(c.usuario) = :usuario");
	    	 query.setParameter("usuario", usuario.getUsuario().toUpperCase().trim());
	    	 lista = query.list();
	    	 
	    	 if(lista.isEmpty() == true) {
	    		 return false;
	    	 } else {
	    		 resultado = encriptado.verificarContraseña(usuario.getContraseña(), lista.get(0).toString());
	    	 }
	    	 	    	
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
		return resultado;
	}
		
	public boolean verificarContraseña(String usuario, String contraseña) {
		boolean resultado = false;
		BCrypt encriptado = new BCrypt();
		Session session = factory.openSession();
		Transaction tx = null;
	      try {
	    	 tx = session.beginTransaction();
	    	 Query query = session.createQuery("SELECT c.contraseña FROM Cuentas c WHERE UPPER(c.usuario) = :usuario");
	    	 query.setParameter("usuario", usuario.toUpperCase().trim());
	    	 String clave = query.uniqueResult().toString();
	    	 resultado = encriptado.verificarContraseña(contraseña, clave);
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
		return resultado;
	}
	
	public void cambiarContraseña(String usuario, String viejaContraseña, String nuevaContraseña) {
		if(verificarContraseña(usuario, viejaContraseña) == false) {
			System.out.println("Contraseña incorrecta!");
			return;
		}
		if(verificarContraseña(usuario, viejaContraseña) == true) {
			if(nuevaContraseña.length() < 12) {
				 System.out.println("La contraseña debe contener al menos 12 caracteres!");
	    		 return;
	    	 }
			Session session = factory.openSession();
			Transaction tx = null;
			BCrypt criptografia = new BCrypt();
			try {
		    	 tx = session.beginTransaction();
		    	 Query query = session.createQuery("SELECT c.id FROM Cuentas c WHERE UPPER(c.usuario) = :usuario");
		    	 query.setParameter("usuario", usuario.toUpperCase().trim());
		    	 int usuarioID = Integer.parseInt(query.uniqueResult().toString());
		    	 Cuentas objeto = (Cuentas) session.get(Cuentas.class, usuarioID); 
		    	 objeto.setContraseña(criptografia.encriptarContraseña(nuevaContraseña));
		    	 session.update(objeto);
		         tx.commit();
		      } catch (HibernateException e) {
		         if (tx!=null) tx.rollback();
		         e.printStackTrace(); 
		      } finally {
		         session.close(); 
		      }
		}
	}
		
	public void agregarUsuario(Cuentas usuario) {

		Session session = factory.openSession();
		 BCrypt encriptado = new BCrypt();
	      Transaction tx = null;
	      try {
	    	 if(usuario.getContraseña().length() < 12) {
	    		 System.out.println("La contraseña debe contener al menos 12 caracteres!");
	    		 return;
	    	 }
	         tx = session.beginTransaction();
	         Cuentas objeto = new Cuentas(usuario.getUsuario().trim(), usuario.getContraseña());
	         String contraseñaEncriptada = encriptado.encriptarContraseña(usuario.getContraseña());
	         objeto.setContraseña(contraseñaEncriptada);
	         session.save(objeto); 
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
	}
}
