



package com.book.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.embeddable.EJBContainer;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.book.app.business.AppServices;
import com.book.app.business.ImageService;
import com.book.app.business.InfAppServices;
import com.book.test.tools.MockHelper;
import com.book.test.tools.TestEjbHelper;

import entities.Collection;
import entities.User;

public class CollectionServicesTest {
	
	
	
	private static final String TEST_USER_EMAIL = "qbit.player@gmail.com";
	
	@Inject
	private InfAppServices service; 
 
	/*@EJB
	private AppServices ejbServices;*/
	

   @Before
    public void before() throws NamingException{   
    	EJBContainer ejbContainer = TestEjbHelper.getEjbContainer();  	
    	 ejbContainer.getContext().bind("inject", this); 
    	
        service.removeAll(User.class); 
    }
   
   
  

     @Test
     public void  addCollection(){
    	  User user = MockHelper.mockUser("User Test",MockHelper.TEST_USER_EMAIL);     	 
    	  service.signUpUser(user); 
    	  
    	  Collection collection = MockHelper.mockCollection("Collection test"); 
		  service.addCollection(user.getId(),collection); 
		  
		  
		  User result = service.find(User.class, user.getId());  
		  
		  Assert.assertEquals(1, result.getCollections().size()); 
		  ArrayList<Collection> list =  new ArrayList<>(result.getCollections()); 		  
		  Assert.assertEquals(collection.getName(),list.get(0).getName() ); 
     }
     
     

     @Test
     public void  removeCollection(){
    	  User user = MockHelper.mockUser("User Test",MockHelper.TEST_USER_EMAIL);     	 
    	  Collection collection = MockHelper.mockCollection("Collection test");
    	  Collection collection2 = MockHelper.mockCollection("Collection test 2");
    	  user.getCollections().add(collection); 
    	  user.getCollections().add(collection2);
    	  collection.setUser(user); 
    	  collection2.setUser(user); 
    	  
    	  
    	  service.signUpUser(user);
    	  
    	  Assert.assertEquals(2,user.getCollections().size());
    	
    	  //service.remove(Collection.class,collection.getId()); 
		  service.removeCollection(collection.getId()); 
		  
		  User result = service.find(User.class, user.getId()); 
		  
		  //Assert.assertEquals(1,result.getCollections().size());
	    	
		  Assert.assertNotNull(user); 	
		  Set<Collection> list = result.getCollections();  		  
		  Assert.assertEquals(1,list.size()); 
		  
		  
		  Collection ressultC = service.find(Collection.class, collection.getId()); 
		  Assert.assertNull(ressultC); 		  
		  		
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
     
     


     

}
