package com.book.test;

import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.inject.Inject;
import javax.naming.NamingException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.book.app.business.AppServices;
import com.book.app.business.ImageService;
import com.book.app.business.InfAppServices;
import com.book.test.tools.MockHelper;
import com.book.test.tools.TestEjbHelper;

import entities.Collection;
import entities.Item;
import entities.User;

public class ItemServicesTest {

	

	@Inject
	private InfAppServices service; 
 
    
    @Before
    public void before() throws NamingException{   
    	EJBContainer ejbContainer = TestEjbHelper.getEjbContainer();  	
    	 ejbContainer.getContext().bind("inject", this);    	
    	 service.removeAll(User.class);
    }
    
  
    @Test
    public void testAddItem() { 
    	// usuario
    	User user = MockHelper.mockUser("User Test",MockHelper.TEST_USER_EMAIL);     	 
   	 	service.signUpUser(user);
   	    // libreria
   	 	Collection collection = MockHelper.mockCollection("Collection test"); 
	    service.addCollection(user.getId(),collection);
	    // libro
   	 	Item item = MockHelper.mockItem("Jhon","Libro de familia","Familiar");     	 
   	    service.addItem(collection.getId(),item,null);  
    	
    	List<Item> list = service.getAll(Item.class);  
    	Assert.assertEquals(1, list.size());      
    	
    }
    
    
    @Test
    public void testupdateItem() { 
    	// usuario
    	User user = MockHelper.mockUser("User Test",MockHelper.TEST_USER_EMAIL);     	 
   	 	service.signUpUser(user);
   	    // libreria
   	 	Collection collection = MockHelper.mockCollection("Collection test"); 
	    service.addCollection(user.getId(),collection);
	    // libro
   	 	Item item = MockHelper.mockItem("Jhon","Libro de familia","Familiar");     	 
   	    service.addItem(collection.getId(),item,null);
   	    
   	    
   	      Item editedItem = new Item(); 
   	      editedItem.setId(item.getId()); 
   	      editedItem.setAuthor("Nuevo autor"); 
   	      editedItem.setDescription("Description nueva"); 
       	  editedItem.setTitle("nuevo titulo");
    	  service.updateItem(editedItem,null); 
   	    
    	  Item result  = service.find(Item.class,item.getId()); 


    	  Assert.assertEquals("Nuevo autor",result.getAuthor());
    	  Assert.assertEquals("Description nueva",result.getDescription());
     	  Assert.assertEquals("nuevo titulo",result.getTitle());
     	  
    	
    }
    
    @Test
    public void  updateCollection(){
   	  User user = MockHelper.mockUser("User Test",MockHelper.TEST_USER_EMAIL);     	 
   	  Collection collection = MockHelper.mockCollection("Collection test");
   	  user.getCollections().add(collection);  
   	  collection.setUser(user); 
   	  
   	  service.signUpUser(user); 
   	  
   	  
   	  Collection editedCollection = new Collection(); 
   	  editedCollection.setId(collection.getId()); 
   	  editedCollection.setName("Name edited"); 
   	  editedCollection.setDescription("Description edited"); 
   	  
		  service.updateCollection(editedCollection); 
		  
		  Collection result  = service.find(Collection.class,
				  					editedCollection.getId()); 
   	  
		  
		  Assert.assertEquals("Name edited",result.getName());
		  Assert.assertEquals("Description edited",result.getDescription());
			  
		
    }
    
    
    
    
//    @Test
//    public void  testRemoveItem(){
//    	// Creamos una coleccion 
//    	Collection collection = MockHelper.mockCollection("Collection_test");
//    	Item item = MockHelper.mockItem("Jhon","Libro de familia","Familiar");     	 
//   	    service.addItem(collection.getId(),item,null);  
//    	
   	    
//   	  User user = MockHelper.mockUser("User Test",MockHelper.TEST_USER_EMAIL);     	 
//   	  Collection collection = MockHelper.mockCollection("Collection test");
//   	  Collection collection2 = MockHelper.mockCollection("Collection test 2");
//   	  user.getCollections().add(collection); 
//   	  user.getCollections().add(collection2);
//   	  collection.setUser(user); 
//   	  collection2.setUser(user); 
//   	  
//   	  
//   	  service.signUpUser(user);
//   	  
//   	  Assert.assertEquals(2,user.getCollections().size());
//   	
//   	  //service.remove(Collection.class,collection.getId()); 
//		  service.removeCollection(collection.getId()); 
//		  
//		  User result = service.find(User.class, user.getId()); 
//		  
//		  //Assert.assertEquals(1,result.getCollections().size());
//	    	
//		  Assert.assertNotNull(user); 	
//		  Set<Collection> list = result.getCollections();  		  
//		  Assert.assertEquals(1,list.size()); 
//		  
//		  
//		  Collection ressultC = service.find(Collection.class, collection.getId()); 
//		  Assert.assertNull(ressultC); 		  
		  		
//    }
    
    
 
    
    
    
}
