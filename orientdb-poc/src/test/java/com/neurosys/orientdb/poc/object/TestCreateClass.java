package com.neurosys.orientdb.poc.object;
/**
 * @author Rajat Nayak 
 * @author Nitin Sharma
 *
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.neurosys.orientdb.poc.dao.schema.SchemaFactory;
import com.neurosys.orientdb.poc.domain.Student;
import com.neurosys.orientdb.poc.domain.Subject;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;


public class TestCreateClass {
	
	private static OObjectDatabaseTx db;
	SchemaFactory schemaFactory;
	Logger log = LoggerFactory.getLogger(TestDatabaseCreation.class);
	
	/*
	 * Open a connection to database 
	 */
	@Before
    public void before(){
		
        db = new OObjectDatabaseTx("memory:test");
//        db = new OObjectDatabaseTx("remote:localhost/dummydatar").open("root","admin");
	    schemaFactory = new SchemaFactory(db);
        System.out.println("db.exists():" + db.exists());
	    if(!db.exists()) {
	        db.create();
	        log.info("Database created...");
	    }
	    System.out.println("db: " + db.getClass().getName() + "@" + Integer.toHexString(db.hashCode()));
	    
	}
	
	/*
	 * Dynamically create class by passing the property list
	 */
	@Test
	public void TestClass()
    {    	

    	List<String> subPropName = Arrays.asList("subid","name");
    	List<String> stuPropName = Arrays.asList("sid","name","surname","studies");
    	
    	List<OType> subPropType = Arrays.asList(OType.INTEGER,OType.STRING);
    	List<OType> stuPropType = Arrays.asList(OType.INTEGER,OType.STRING,OType.STRING,OType.LINK);
    	
    	List<OClass> subClasses = new ArrayList<OClass>();
    	ArrayList<OClass> stuClasses = new ArrayList<OClass>();    	
    	
    	schemaFactory.createClass(Subject.class,subPropName,subPropType, subClasses);
    	schemaFactory.registerClass(Subject.class);
    	
    	stuClasses.add(db.getMetadata().getSchema().getClass(Subject.class)); 
    	
    	schemaFactory.createClass(Student.class,stuPropName,stuPropType, stuClasses);
    	log.info("Class Properties");
    	Assert.assertNotNull(schemaFactory.getClassByName(Subject.class)); 	   	
    	Assert.assertNotNull(schemaFactory.getClassByName(Student.class)); 	 
    	
//    	log.info("List of classes in database");
//    	schemaFactory.getClasses();
    	
/*		List<String> propName = new ArrayList<String>();
		List<OType> propType = new ArrayList<OType>();
		String t="OType.";
		
    	Subject s = new Subject();
    	Class<?> clazz = s.getClass();
    	for(Field field : clazz.getDeclaredFields()) {
	       System.out.println(field.getName()+" "+field.getType().getSimpleName());
	       propName.add(field.getName());
	       t.concat(field.getType().getName());
	       
	       propType.add(t);
    	}   */
    }	
	
	/*
	 * Close connection to Database
	 */
	@After
    public void after() throws Exception {
		log.info("Close database connection");
        db.close();
        log.info("db.drop()");
    }
}
