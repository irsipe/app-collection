package com.book.app.business;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

import entities.Collection;
import entities.Image;
import entities.Item;
import entities.User;

@Stateless
@SuppressWarnings("unchecked")
public class AppServices implements InfAppServices  {

	@PersistenceContext(unitName = "persistence-unit" )
    private EntityManager entityManager;
	
	
	@Override
	public void signUpUser(@NotNull User user) {

		 if(user==null || user.getEmail()==null){
			 throw new IllegalArgumentException("> "
			 		+ "El parametro User no debe ser null y debe tener un email valido "); 
		 }		
		 //TODO verificar formato correo 
		List<User> list = entityManager.createNamedQuery(User.QUERY_USER_BY_EMAIL) 
    			.setParameter("email",user.getEmail()).getResultList(); 		
		if(list!=null && list.size()>0){
			throw new EntityExistsException("El User tiene un email que esta registrado"); 
		}
		
		entityManager.persist(user); 
	}
	

	@Override
	public User signInUser(@NotNull String email) { 
		
		List<User> list = entityManager.createNamedQuery(User.QUERY_USER_BY_EMAIL) 
    			.setParameter("email",email).getResultList(); 		 
		//TODO verificar formato correo 		
		if(list==null || list.size()!=1){
			throw new EntityNotFoundException(""
					+ "No se encuentra un usuario con el email: " + email); 
		}
		
		return list.get(0); 		
	}

	@Override
	public void signOut() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCollection(@NotNull String userId, 
								@NotNull Collection collection) {
		
		User user = entityManager.find(User.class, userId); 
		if(user==null){
			throw new EntityNotFoundException(""
					+ "No se encuentra un usuario con el userId: " + userId); 
		}
		
		//entityManager.persist(collection); 
		
		user.getCollections().add(collection);
		collection.setUser(user); 	
	}
	
	@Override
	public void removeCollection(String collectionId) {			 
		 Collection collec = entityManager.find(Collection.class,collectionId);
		 User user = entityManager.find(User.class,collec.getUser().getId());		 
		 Set<Collection> list = user.getCollections();  
		 
		 for (Collection c : list) {
			if(c.equals(collec)){
				list.remove(c);
				break; 
			}
		}		 	
	}
	
	
	@Override
	public void updateCollection(Collection collection) {
		 String collectionId = collection.getId();
		 Collection collectionOld = entityManager.find(Collection.class,collectionId);	
		 String valor;
		 
		 valor=collection.getName();
		 if(compruebaValor(valor))
			 collectionOld.setName(valor);
		 
		 valor=collection.getDescription();
		 if(compruebaValor(valor))
			  collectionOld.setDescription(valor);

		 
	  
			
	}
	


	@Override
	public void addItem(String collectionId, Item item, byte[] bytes) {
		// buscamos la coleccion (libreria)
		Collection col = entityManager.find(Collection.class, collectionId);
		// si existe !!!!
		if(col==null){
			throw new EntityNotFoundException(""
					+ "No se encuentra la coleccion con el colecctionId: " + collectionId); 
		}		
		// añade un libro a la libreria
		col.getItems().add(item);
		// pone el ide de la coleccion al libro
		item.setCollection(col);		
		
		if(bytes!=null){
			Image image = new Image();
			image.setBytes(bytes);	
			item.setImage(image);
			entityManager.flush();
			String url = "image_" + image.getId() + ".jpg";
		    image.setUrl(url);  
		}
					
	}
	
 

	@Override
	public void updateItem(Item item, byte[] bytes) {
		  // buscamos el id del libro
	 	  String itemId = item.getId();
	 	  String valor;
		  // buscamos todos sus datos antiguos  
		  Item itemOld = entityManager.find(Item.class,itemId);	
		  // acabamos de modificar todo el registro
		  valor=item.getTitle();
		  if(compruebaValor(valor))
			 itemOld.setTitle(valor);
		  
		  valor=item.getAuthor();
		  if(compruebaValor(valor))
			 itemOld.setAuthor(valor);
		  
		  valor=item.getDescription();
		  if(compruebaValor(valor))
			 itemOld.setDescription(valor);
		  
 		// la imagen la sustituimos  
		if(bytes!=null){
			Image imageOld = entityManager.find(Image.class,itemId);	
	 		imageOld.setBytes(bytes);	
		}
		
	}

	
	@Override
	public void removeItem(String itemId) {
		// borrar el elemento, su imagen y la relación con la colección.
		Item item = entityManager.find(Item.class, itemId);
		// imagenes relacionadas con el libro
		Set<Image> img = (Set<Image>) entityManager.find(Image.class,  item.getId());
		// librerias relacionadas con el libro
		
		Set<Collection> col = (Set<Collection>) entityManager.find(Collection.class, item.getId());
		// borramos la coleccion a la que pertenece ese libro	
		 
		
		 for (Collection c : col) {
			if(c.equals(item)){
				col.remove(c);
				break; 
			}
		}	
		 for (Image c : img) {
				if(c.equals(img)){
					img.remove(c);
					break; 
				}
			}	
		 entityManager.remove(item);
		
		// TODO Auto-generated method stub
		
	}
	
	/** Services intented only for test  */
	
	//************************************************************************************
	
	@Override
	public <T> void remove(Class<T> clazz, Object id) {
	     T  entity=	entityManager.find(clazz,id);
	     if(entity!=null)
	    	 entityManager.remove(entity); 
	}
	
	

	@Override
	public <T> T find(Class<T> clazz, Object id) {		
		return entityManager.find(clazz, id); 
	}
	
	@Override
	public <T> List<T> getAll(Class<T> clazz) {
		String clasName = clazz.getSimpleName(); 
		return entityManager.createQuery("SELECT o FROM " + clasName  + " o")
				.getResultList(); 	
	}


	/**
	 * Remove all objets by use { @code entityManager.createQuery("DELETE FROM " +clasName) }
	 */
	@Override
	public <T> void deleteAll(Class<T> clazz) {
		String clasName = clazz.getSimpleName(); 
		entityManager.createQuery("DELETE FROM " +clasName).executeUpdate();
	}


	/**
	 * Remove all objets by user  { @code entityManager.remove(t); }
	 */
	@Override
	public <T> void removeAll(Class<T> clazz) {
		List<T> list = getAll(clazz);  
		//Cada elemento de la lista esta managed dado que se recupero con query
		for (T t : list) {
			entityManager.remove(t); 
		}		
	}


	@Override
	public void addImage(String itemId, Image image) {
		// TODO Auto-generated method stub
		
	}
	
	
	 public boolean compruebaValor(String valor){
		  if(valor!=null &&  !valor.equals("")){
			  return true;
		  }
		  return false;
			  
	  }

}
