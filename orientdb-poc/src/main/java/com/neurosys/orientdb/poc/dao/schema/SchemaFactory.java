package com.neurosys.orientdb.poc.dao.schema;

/**
 * @author Rajat Nayak 
 * @author Nitin Sharma
 *
 */

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.neurosys.orientdb.poc.domain.Subject;
import com.neurosys.orientdb.poc.schema.TestSchemaHybrid;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

/*
 * Creation and Modification of Schema of Classes
 */
@Component
public class SchemaFactory {
	
	OObjectDatabaseTx database;
	Logger log = LoggerFactory.getLogger(TestSchemaHybrid.class);
	
	/*
	 * Connecting to default database
	 */
	public SchemaFactory(){
		database = new OObjectDatabaseTx("remote:localhost/dummydatar").open("root","admin");
	}
	
	/*
	 * Connecting to user-assigned database
	 */
	public SchemaFactory(OObjectDatabaseTx db){
		database = db;
	}
	
	@Transactional
	public OObjectDatabaseTx getDB(){
		return database;
	}
	
	/*
	 * Register Classes
	 */
	@Transactional
	public void registerClass(Class<?> clas){
		database.getEntityManager().registerEntityClass(clas);
	}	
	
	/*
	 * Create Subject class
	 */
	@Transactional
	public void createSubjectClass(){	
		OClass subjectClass = database.getMetadata().getSchema().createClass("Subject");
		subjectClass.createProperty("subid", OType.INTEGER);
		subjectClass.createProperty("name", OType.STRING);
	}
	
	/*
	 * Create Student class
	 */
	@Transactional
	public void createStudentClass(){
		OClass subjectClass = database.getMetadata().getSchema().getClass(Subject.class);
		OClass studentClass = database.getMetadata().getSchema().createClass("Student");
		studentClass.createProperty("sid", OType.INTEGER);
		studentClass.createProperty("name", OType.STRING);
		studentClass.createProperty("surname", OType.STRING);
		studentClass.createProperty("subject", OType.LINK,subjectClass);
	}
	
	/*
	 * Create Teacher class
	 */
	@Transactional
	public void createTeacherClass(){
		OClass subjectClass = database.getMetadata().getSchema().getClass(Subject.class);
		OClass teacherClass = database.getMetadata().getSchema().createClass("Teacher");
		teacherClass.createProperty("tid", OType.INTEGER);
		teacherClass.createProperty("name", OType.STRING);
		teacherClass.createProperty("surname", OType.STRING);
		teacherClass.createProperty("subjects", OType.LINK,subjectClass);
	}
	
	/*
	 * Create class dynamically using arguments
	 * - name : Class Name
	 * - propname : Property name list
	 * - proptype : Property type list
	 * 
	 * TODO : Extract property directly from POJO classes
	 */
	@Transactional
	public void createClass(Class name ,List<String> propname,List<OType> proptype ,List<OClass> relatedClass){

		/*
		 * Create new class		
		 */
		OClass sClass = database.getMetadata().getSchema().createClass(name);
		
		/*
		 * Get list of all classes in database
		 */		
//		Collection<OClass> newClass = database.getMetadata().getSchema().getClasses();
//		System.out.println(newClass.toString());

		int i=0,j=0;
		
		/*
		 * Create properties 
		 */		
		for(OType p : proptype){
			if(proptype.get(i).name().equalsIgnoreCase("LINK")){
				sClass.createProperty(propname.get(i),proptype.get(i),relatedClass.get(j++));	
			}
			else{	
				sClass.createProperty(propname.get(i),proptype.get(i));
			}
			i++;
		}
	}
	
	/*
	 * Extract Class using Class Name 
	 */
	@Transactional
	public OClass getClassByName(Class name){
		log.info("Retrieved Class Details");
		log.info("Name :"+database.getMetadata().getSchema().getClass(name).getName());
		log.info("Properties :"+database.getMetadata().getSchema().getClass(name).declaredProperties());
		return database.getMetadata().getSchema().getClass(name);
		
	}
	
	/*
	 * Extract all Classes from the Database
	 */
	@Transactional
	public Collection<OClass> getClasses(){
		Collection<OClass> dbclasses=  database.getMetadata().getSchema().getClasses();
		for(OClass c : dbclasses){
			System.out.println(c.getName());
		}
		return dbclasses;
	}
	
	/*
	 * Extract property of a Class
	 */
	@Transactional
	public List getProperty(String name){
		String sql = "select expand(properties) from (select expand(classes) from metadata:schema) where name = '"+name+"'";
		List s =database.query(new OSQLSynchQuery(sql));
		int i=0 , size=s.size();
		while(i < size){
			System.out.println(s.get(i).toString());
			i++;
		}
		return s;        
	}	
	
	/*
	 * Close connection to database
	 */
	@Transactional
	public void destroy(){
		database.close();
	}
		
}
