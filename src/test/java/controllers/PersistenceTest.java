package controllers;

import static org.junit.Assert.*;

import java.io.File;
import org.junit.Test;
import controllers.PacemakerAPI;
import models.Activity;
import models.Location;
import models.User;
import utils.BinarySerializer;
import utils.JSONSerializer;
import utils.Serializer;
import utils.XMLSerializer;

import static models.Fixtures.*;

public class PersistenceTest
{
	
	private static final String TESTDATASTORE_XML = "testdatastore.xml";
	private static final String TESTDATASTORE_JSON = "testdatastore.json";
	private static final String TESTDATASTORE_BINARY = "testdatastore.bin";
	PacemakerAPI pacemaker;
	
	void populate (PacemakerAPI pacemaker){
	
		for (User user : users)
		{
			pacemaker.createUser(user.firstName, user.lastName, user.email, user.password);
		}
		User user1 = pacemaker.getUserByEmail(users[0].email);
		Activity activity = pacemaker.createActivity(user1.id, activities[0].type, activities[0].location, activities[0].distance, activities[0].date);
		pacemaker.createActivity(user1.id, activities[1].type, activities[1].location, activities[1].distance, activities[1].date);
		User user2 = pacemaker.getUserByEmail(users[1].email);
		pacemaker.createActivity(user2.id, activities[2].type, activities[2].location, activities[2].distance, activities[2].date);
		pacemaker.createActivity(user2.id, activities[3].type, activities[3].location, activities[3].distance, activities[3].date);

		for (Location location : locations)
		{
			pacemaker.addLocation(activity.id, location.latitude, location.longitude);
		}
	}

	  @Test
	  public void testPopulate()
	  { 
		pacemaker = new PacemakerAPI(null);    
	    assertEquals(0, pacemaker.getUsers().size());
	    populate (pacemaker);

	    assertEquals(users.length, pacemaker.getUsers().size());
	    assertEquals(2, pacemaker.getUserByEmail(users[0].email).activities.size());
	    assertEquals(2, pacemaker.getUserByEmail(users[1].email).activities.size());   
	    
	    Long activityId = pacemaker.getUserByEmail(users[0].email).activities.keySet().iterator().next();
	    assertEquals(locations.length, pacemaker.getActivity(activityId).route.size());
	  }

	  void deleteFile(String fileName)
	  {
	    File datastore = new File (fileName);
	    if (datastore.exists())
	    {
	      datastore.delete();
	    }
	  }
	  
	  @Test
	  public void testXMLSerializer() throws Exception
	  { 
	    
	    deleteFile (TESTDATASTORE_XML);

	    Serializer serializer = new XMLSerializer(new File (TESTDATASTORE_XML));

	    pacemaker = new PacemakerAPI(serializer); 
	    populate(pacemaker);
	    pacemaker.store();

	    PacemakerAPI pacemaker2 =  new PacemakerAPI(serializer);
	    pacemaker2.load();
	    
	    assertTrue(pacemaker.getActivitiesIndex().keySet().equals(pacemaker2.getActivitiesIndex().keySet()));	    
	    assertTrue(pacemaker.getUserIndex().keySet().equals(pacemaker2.getUserIndex().keySet()));	    
	    assertTrue(pacemaker.getEmailIndex().keySet().equals(pacemaker2.getEmailIndex().keySet()));

	    assertEquals (pacemaker.getUsers().size(), pacemaker2.getUsers().size());
	    for (User user : pacemaker.getUsers())
	    {
	      assertTrue (pacemaker2.getUsers().contains(user));
	    }
	    deleteFile (TESTDATASTORE_XML);

	  }
	  
	  @Test
	  public void testJSONSerializer() throws Exception
	  { 
	    
	    deleteFile (TESTDATASTORE_JSON);

	    Serializer serializer = new JSONSerializer(new File (TESTDATASTORE_JSON));

	    pacemaker = new PacemakerAPI(serializer); 
	    populate(pacemaker);
	    pacemaker.store();
	    
	    PacemakerAPI pacemaker2 =  new PacemakerAPI(serializer);
	    pacemaker2.load();
	    
	    assertTrue(pacemaker.getActivitiesIndex().keySet().equals(pacemaker2.getActivitiesIndex().keySet()));	    
	    assertTrue(pacemaker.getUserIndex().keySet().equals(pacemaker2.getUserIndex().keySet()));	    
	    assertTrue(pacemaker.getEmailIndex().keySet().equals(pacemaker2.getEmailIndex().keySet()));	    

	    assertEquals (pacemaker.getUsers().size(), pacemaker2.getUsers().size());
	    for (User user : pacemaker.getUsers())
	    {
	      assertTrue (pacemaker2.getUsers().contains(user));
	    }

	    deleteFile (TESTDATASTORE_JSON);

	  }
	  
	  @Test
	  public void testBinarySerializer() throws Exception
	  { 
	   
	    deleteFile (TESTDATASTORE_BINARY);

	    Serializer serializer = new BinarySerializer(new File (TESTDATASTORE_BINARY));

	    pacemaker = new PacemakerAPI(serializer); 
	    populate(pacemaker);
	    pacemaker.store();

	    PacemakerAPI pacemaker2 =  new PacemakerAPI(serializer);
	    pacemaker2.load();
	    
	    assertTrue(pacemaker.getActivitiesIndex().keySet().equals(pacemaker2.getActivitiesIndex().keySet()));	    
	    assertTrue(pacemaker.getUserIndex().keySet().equals(pacemaker2.getUserIndex().keySet()));	    
	    assertTrue(pacemaker.getEmailIndex().keySet().equals(pacemaker2.getEmailIndex().keySet()));

	    assertEquals (pacemaker.getUsers().size(), pacemaker2.getUsers().size());
	    for (User user : pacemaker.getUsers())
	    {
	      assertTrue (pacemaker2.getUsers().contains(user));
	    }
	   
	    deleteFile (TESTDATASTORE_BINARY);

	  }
	  
}