package com.neurosys.orientdb.poc.transform;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.neurosys.orientdb.poc.dao.object.SubjectObjectDao;
import com.neurosys.orientdb.poc.dao.object.TeacherObjectDao;
import com.neurosys.orientdb.poc.document.TestDocumentCRUDDao;
import com.neurosys.orientdb.poc.domain.Subject;
import com.neurosys.orientdb.poc.domain.Teacher;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.entity.OEntityManager;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

/**
 * @author : Nitin Sharma, Rajat Kumar Nayak
 */
public class ObjectToDocumentTransform{
	
	private static OObjectDatabaseTx objectDatabase;
	private static Logger log;
	
	@Before
    public void before() throws Exception {
		log = LoggerFactory.getLogger(ObjectToDocumentTransform.class);
		log.info("Logger Created...");
		objectDatabase=new OObjectDatabaseTx("memory:objecttodocument");
		objectDatabase.create();
		log.info("objectDatabase.create() : " + objectDatabase.exists());
	}
	
	@AfterClass
	public static void after() throws Exception {
		objectDatabase.drop();
		log.info("Database dropped : objecttodocument");
	}

	@Test
	public void ObjectToDocumentTransformer() throws Exception{
		
		OEntityManager em=objectDatabase.getEntityManager();
    	em.registerEntityClass(Subject.class);
    	em.registerEntityClass(Teacher.class);
    	log.info("Subject and Teacher Entity Classes registered...");
		
    	//Create new Subject and Teacher using their constructors
    	Subject subject=new Subject("Ninjutsu");
    	Teacher teacher=new Teacher("Hashirama","Senju",subject);
    	
    	/*
    	 * Create DAO class object for Subject and Teacher.
    	 * DAO classes are responsible for interacting with databases
    	 */
    	SubjectObjectDao subjectDao = new SubjectObjectDao(objectDatabase);
    	TeacherObjectDao teacherDao = new TeacherObjectDao(objectDatabase);
    	
    	//Persist data into database
    	subjectDao.saveSubject(subject);
    	teacherDao.saveTeacher(teacher);
    	log.info("Data inserted in database : ");

    	//Extract Teacher using 'name' from database
    	Teacher extractedTeacher=teacherDao.getTeacherByName(teacher.getName());
    	
    	//Convert extracted Teacher into ODocument format
    	//@Todo : reason behind name "getRecordByUserObject"
    	ODocument doc=objectDatabase.getRecordByUserObject(extractedTeacher, true);
    	log.info("Transformed...");
    	
    	//Testing for correct transformation of data
    	Assert.assertTrue("Object coming out is not of class \"Teacher\"","Teacher".equals(doc.getClassName()));
    	Assert.assertTrue(doc.containsField("name"));
    	Assert.assertEquals("Name is not \"Hashirama\" ","Hashirama",doc.field("name"));
    	Assert.assertSame("No of fields not equal to 3  ",3, doc.fields());
	}	
}