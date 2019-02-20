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

import com.inventario.modelos.Productos;

@Service
public class ServicioProductos {
	
	public static SessionFactory factory = HibernateSession.factory;
	
	public static void main(String[] args) {
		
	}
	
	public int contadorProductos() {
		int contador = 0;
		Session session = factory.openSession();
	      Transaction tx = null;
	      try {
	         tx = session.beginTransaction();
	         String hql = "SELECT Count(*) FROM Productos";
	         Query query = session.createQuery(hql);
	         contador = Integer.parseInt(query.getSingleResult().toString());
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
	      return contador;
	}
	
	public Productos seleccionarProducto(int id) {
		Productos producto = new Productos();
		Session session = factory.openSession();
	      Transaction tx = null;
	      
	      try {
	         tx = session.beginTransaction();
	         String hql = "FROM Productos WHERE id = :id";
	         Query query = session.createQuery(hql);
	         query.setParameter("id", id);
	         List resultados = query.list();
	         for (Iterator iterator = resultados.iterator(); iterator.hasNext();){
	            producto = (Productos) iterator.next(); 
	         }
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
		return producto;
	}
	
	public Productos seleccionarProducto(String nombre) {
		Productos producto = new Productos();
		Session session = factory.openSession();
	      Transaction tx = null;

	      try {
	         tx = session.beginTransaction();
	         String hql = "FROM Productos WHERE UPPER(nombreProducto) = :nombre";
	         Query query = session.createQuery(hql);
	         query.setParameter("nombre", nombre.trim().toUpperCase());
	         List resultados = query.list();
	         for (Iterator iterator = resultados.iterator(); iterator.hasNext();){
	            producto = (Productos) iterator.next(); 
	         }
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
		return producto;
	}
	
	public List<Productos> paginacionProductos(int pag){
		List<Productos> lista = new ArrayList<Productos>();
		Session session = factory.openSession();
	      Transaction tx = null;
		try {
	         tx = session.beginTransaction();
	         String sql = "SELECT nombreProducto, unidadesProducto, cid.nombreCategoria FROM Productos Order by cid.nombreCategoria Asc";
	         Query query = session.createQuery(sql).setFirstResult((pag - 1) * 10).setMaxResults(10);
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

	public List<Productos> listaProductos(int FK){
		List<Productos> lista = new ArrayList<Productos>();
		Session session = factory.openSession();
	      Transaction tx = null;
		try {
	         tx = session.beginTransaction();
	         String sql = "SELECT id, p_nombre, p_unidades FROM productos WHERE FK_categoria = :FK";
	         NativeQuery query = session.createSQLQuery(sql);
	         query.setParameter("FK", FK);
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
	
	public void agregarProducto(Productos producto) {
		 Session session = factory.openSession();
	      Transaction tx = null;
	      
	      try {
	         tx = session.beginTransaction();
	         Productos objeto = new Productos(producto.getcid().getId(), producto.getNombreProducto().trim(), producto.getUnidadesProducto());
	         session.save(objeto); 
	         String SQL = "UPDATE Categorias SET c_cantidad = c_cantidad + 1 WHERE id = :id";
	         NativeQuery<Productos> query = session.createSQLQuery(SQL);
	         query.setParameter("id", producto.getcid().getId());
	         query.executeUpdate();
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
	}
	
	public void actualizarProducto(Integer ProductoID, Productos producto) {
		Session session = factory.openSession();
	      Transaction tx = null;
	      
	      try {
	         tx = session.beginTransaction();
	         Productos objeto = (Productos) session.get(Productos.class, ProductoID); 
	         
	         if(producto.getcid().getId() != objeto.getcid().getId()) {
	        	 
	        	 String SQL = "UPDATE Productos SET c_cantidad = c_cantidad - 1 WHERE id = :id";
		         NativeQuery<Productos> query = session.createSQLQuery(SQL);
		         query.setParameter("id", objeto.getcid().getId());
		         query.executeUpdate();
		         SQL = "UPDATE Productos SET c_cantidad = c_cantidad + 1 WHERE id = :id";
		         query = session.createSQLQuery(SQL);
		         query.setParameter("id", producto.getcid().getId());
		         query.executeUpdate();
		         
	         }
	         
	         objeto.setcid(producto.getcid());
	         objeto.setNombreProducto(producto.getNombreProducto().trim());
	         objeto.setUnidadesProducto(producto.getUnidadesProducto());
			 session.update(objeto); 
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
	}
	
	public void eliminarProducto(Integer ProductoID) {
		Session session = factory.openSession();
	      Transaction tx = null;
	      
	      try {
	         tx = session.beginTransaction();
	         Productos producto = (Productos) session.get(Productos.class, ProductoID); 
	         String SQL = "UPDATE Categorias SET c_cantidad = c_cantidad - 1 WHERE id = :id";
	         NativeQuery<Productos> query = session.createSQLQuery(SQL);
	         query.setParameter("id", producto.getcid().getId());
	         query.executeUpdate();
	         session.delete(producto); 
	         tx.commit();
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
	}

}

