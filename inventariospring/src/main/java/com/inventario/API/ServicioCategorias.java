package com.inventario.API;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Service;

import com.inventario.modelos.Categorias;

@Service
public class ServicioCategorias {
	
	public static SessionFactory factory = HibernateSession.factory;
	
	public List<Categorias> seleccionarCategorias() {
		List<Categorias> lista = new ArrayList<Categorias>();
		Session session = factory.openSession();
	      Transaction tx = null;
	      
	      try {
	    	  tx = session.beginTransaction();
		         String hql = "SELECT c.id, c.nombreCategoria, c.cantidadProductos FROM Categorias c";
		         Query query = session.createQuery(hql);
		         lista = query.list();
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
		return lista;
	}
		
	public Categorias seleccionarCategoria(int id) {
		Categorias categoria = new Categorias();
		Session session = factory.openSession();
	      Transaction tx = null;
	      
	      try {
	         tx = session.beginTransaction();
	         String hql = "FROM Categorias WHERE id = :id";
	         Query query = session.createQuery(hql);
	         query.setParameter("id", id);
	         List resultados = query.list();
	         for (Iterator iterator = resultados.iterator(); iterator.hasNext();){
	            categoria = (Categorias) iterator.next(); 
	         }
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
		return categoria;
	}
	
	public Categorias seleccionarCategoria(String nombre) {
		Categorias categoria = new Categorias();
		Session session = factory.openSession();
	      Transaction tx = null;
	      
	      try {
	         tx = session.beginTransaction();
	         String hql = "FROM Categorias WHERE UPPER(nombreCategoria) = :categoria";
	         Query query = session.createQuery(hql);
	         query.setParameter("categoria", nombre.toUpperCase().trim());
	         List resultados = query.list();
	         for (Iterator iterator = resultados.iterator(); iterator.hasNext();){
	            categoria = (Categorias) iterator.next(); 
	         }
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
		return categoria;
	}
	
	public void agregarCategoria(Categorias categoria) {
		 Session session = factory.openSession();
	      Transaction tx = null;
	      
	      try {
	         tx = session.beginTransaction();
	         Categorias objeto = new Categorias(categoria.getNombreCategoria().trim(), categoria.getCantidadProductos());
	         session.save(objeto); 
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
	}
	
	public void actualizarCategoria(Integer CategoriaID, Categorias categoria) {
		Session session = factory.openSession();
	      Transaction tx = null;
	      
	      try {
	         tx = session.beginTransaction();
	         Categorias objeto = (Categorias) session.get(Categorias.class, CategoriaID); 
	         objeto.setNombreCategoria(categoria.getNombreCategoria().trim());
	         objeto.setCantidadProductos(categoria.getCantidadProductos());
			 session.update(objeto); 
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
	}
	
	public void eliminarCategoria(Integer CategoriaID, Integer nuevaCategoriaID) {
		Session session = factory.openSession();
	      Transaction tx = null;
	      
	      try {
	         tx = session.beginTransaction();
	         String HQL = "SELECT c.cantidadProductos FROM Categorias c WHERE c.id = :id";
	         Query query = session.createQuery(HQL);
	         query.setParameter("id", CategoriaID);
	         int cantidad = Integer.parseInt(query.getSingleResult().toString());
	         Categorias categoria = (Categorias) session.get(Categorias.class, CategoriaID); 
	         String SQL = "UPDATE Productos SET FK_categoria = :FK WHERE FK_categoria = :viejaFK";
	         NativeQuery actualizacion = session.createSQLQuery(SQL);
	         actualizacion.setParameter("FK", nuevaCategoriaID);
	         actualizacion.setParameter("viejaFK", CategoriaID);
	         actualizacion.executeUpdate();
	         SQL = "UPDATE Categorias SET c_cantidad = c_cantidad + :unidades WHERE id = :id";
	         actualizacion = session.createSQLQuery(SQL);
	         actualizacion.setParameter("unidades", cantidad);
	         actualizacion.setParameter("id", nuevaCategoriaID);
	         actualizacion.executeUpdate();
	         session.delete(categoria); 
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
	}

}

