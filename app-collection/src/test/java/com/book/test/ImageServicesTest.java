package com.book.test;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.book.app.business.ImageService;
import com.book.test.tools.TestEjbHelper;

import entities.Image;


/**
 *  to see http://tomee.apache.org/examples-trunk/transaction-rollback/README.html
 * @author campino
 *
 */

public class ImageServicesTest { 

	
	@EJB
	private ImageService service; 
 
    
    @Before
    public void before() throws NamingException{   
    	EJBContainer ejbContainer = TestEjbHelper.getEjbContainer();  	
    	 ejbContainer.getContext().bind("inject", this);    	
    	 service.deleteAll(); 	
    }
    
  
    @Test
    public void insertImage() throws Exception {     
    	
    	byte[] bytImage = new byte[]{1,2,3,4,5,6,7,8,9}; 
    	
    	 Image image = service.insert(bytImage);
    	 
    	 Image results = service.find(image.getUrl());  
    	
    	Assert.assertArrayEquals(bytImage, results.getBytes());    	   	
    }
    
    
   
    
    
}
