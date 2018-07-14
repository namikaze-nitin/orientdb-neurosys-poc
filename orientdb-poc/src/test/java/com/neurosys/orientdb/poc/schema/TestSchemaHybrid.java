package com.neurosys.orientdb.poc.schema;

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


public class TestSchemaHybrid {

	private static OObjectDatabaseTx db;
	SchemaFactory schemaFactory;
	Logger log = LoggerFactory.getLogger(TestSchemaHybrid.class);
	
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
	    
	    log.info("Create classes");
	    schemaFactory.createSubjectClass();
    	log.info("...Classes created");
    	
    	log.info("Register Entity Classes...");	   
	    schemaFactory.registerClass(Subject.class);
    	log.info("...Classes registered");
    	
    	log.info("Class Properties via query");
		schemaFactory.getClassByName(Subject.class);
		schemaFactory.getProperty("Subject");
		log.info("...End of Class Properties");
	    
//	    System.out.println("db: " + db.getClass().getName() + "@" + Integer.toHexString(db.hashCode()));
	    
	}
	
	/*
	 * Dynamically create class by passing the property list
	 */
	@Test
	public void testHybrid(){
		
//		Subject s = db.newInstance(Subject.class);
//		s.setSubid(10);
//		s.setName("new subject");		
//		db.save(s);
//		SubjectObjectDao sub1 = new SubjectObjectDao(db);
//		sub1.getSubjects();
//		log.info("subject id :"+s.getSubid());
//		Assert.assertNotNull(sub1.getSubjectByName("new subject"));
		
		db.getEntityManager().registerEntityClass(Student.class);
		db.getEntityManager().registerEntityClass(Subject.class);
		
		log.info("Before declaring properties");
		log.info("Subject properties :"+db.getMetadata().getSchema().getClass(Subject.class).declaredProperties());
		log.info("Student properties :"+db.getMetadata().getSchema().getClass(Student.class).declaredProperties());
		
		OClass stuClass = db.getMetadata().getSchema().getClass(Student.class);
		stuClass.createProperty("subject", OType.LINK,db.getMetadata().getSchema().getClass(Subject.class));
		
		log.info("After declaring properties");
		Assert.assertNotNull(db.getMetadata().getSchema().getClass(Subject.class).declaredProperties());
		log.info("Subject properties :"+db.getMetadata().getSchema().getClass(Subject.class).declaredProperties());
		Assert.assertNotNull(db.getMetadata().getSchema().getClass(Student.class).declaredProperties());
		log.info("Student properties :"+db.getMetadata().getSchema().getClass(Student.class).declaredProperties());
	}
	
	/*
	 * Close connection to Database
	 */
	@After
    public void after() throws Exception {
		log.info("Close database connection");
        db.drop();
        log.info("db.drop()");
    }
}
