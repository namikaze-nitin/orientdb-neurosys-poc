package com.neurosys.orientdb.poc.object;
/**
* @author Rajat Nayak 
 * @author Nitin Sharma
 *
 */
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.neurosys.orientdb.poc.dao.object.StudentObjectDao;
import com.neurosys.orientdb.poc.dao.object.SubjectObjectDao;
import com.neurosys.orientdb.poc.dao.object.TeacherObjectDao;
import com.neurosys.orientdb.poc.dao.schema.SchemaFactory;
import com.neurosys.orientdb.poc.domain.Student;
import com.neurosys.orientdb.poc.domain.Subject;
import com.neurosys.orientdb.poc.domain.Teacher;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

public class TestDatabaseCreation 
{
	private static OObjectDatabaseTx db;
	SchemaFactory schemaFactory;
	Logger log = LoggerFactory.getLogger(TestDatabaseCreation.class);
	
	/*
	 * Open a connection to database , then Create & Register classes 
	 */
	@Before
    public void before(){		
		
        db = new OObjectDatabaseTx("memory:test");
	    
        System.out.println("db.exists():" + db.exists());
	    if(!db.exists()) {
	        db.create();
	        log.info("Database created...");
	    }
	    System.out.println("db: " + db.getClass().getName() + "@" + Integer.toHexString(db.hashCode()));
	    
	    schemaFactory = new SchemaFactory(db);
//	    log.info("Create classes");
	    schemaFactory.createSubjectClass();
	    schemaFactory.createStudentClass();
	    schemaFactory.createTeacherClass();
    	log.info("...Classes created");
	    
//	    log.info("Register Entity Classes...");	   
	    schemaFactory.registerClass(Subject.class);
	    schemaFactory.registerClass(Student.class);
	    schemaFactory.registerClass(Teacher.class);
    	log.info("...Classes registered");
    	
    	log.info("Class Properties via query");
		schemaFactory.getClassByName(Subject.class);
		schemaFactory.getProperty("Subject");
		schemaFactory.getClassByName(Student.class);
		schemaFactory.getProperty("Student");
		schemaFactory.getClassByName(Teacher.class);
		schemaFactory.getProperty("Teacher");
		log.info("...End of Class Properties");
		
	}		
	
	/*
	 * Make entries into registered classes
	 */
	@Test
    public void TestSubject() throws Exception
    {   
		log.info("Database Entries");
    	SubjectObjectDao sub1 = new SubjectObjectDao(db);
    	sub1.saveSubject("Math");
    	sub1.getSubjects();
    	Assert.assertEquals("Check for Math in database :  ","Math",sub1.getSubjectByName("Math").getName());    	
    	
    	StudentObjectDao st1 = new StudentObjectDao(db);
    	st1.persist("First","Student",sub1.getSubjectByName("Math"));
    	Assert.assertNotNull(st1.getStudents());
    	
    	TeacherObjectDao t11 = new TeacherObjectDao(db);
    	t11.saveTeacher("First","teacher",sub1.getSubjectByName("Math"));
    	Assert.assertNotNull(t11.getTeachers());  	
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