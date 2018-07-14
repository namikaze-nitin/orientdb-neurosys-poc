package com.neurosys.orientdb.poc.dao.object;
/**
* @author Rajat Nayak 
* @author Nitin Sharma
 *
 */
import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.neurosys.orientdb.poc.domain.Teacher;
import com.neurosys.orientdb.poc.domain.Subject;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

@Component
public class TeacherObjectDao {

	@PersistenceContext
	OObjectDatabaseTx db;
	
	/*
	 * Set the database connection
	 */
	public TeacherObjectDao(OObjectDatabaseTx database) {	
		this.db=database;
	}
    
	/*
	 * Persist in database
	 */
	@Transactional
	public void saveTeacher(String teacherName,String teacherSurname , Subject subject){
		Teacher t1 = db.newInstance(Teacher.class,teacherName,teacherSurname,subject);  		
		db.save(t1);
	}
	/*
	 * Persist in database
	 */
	@Transactional
	public void saveTeacher(Teacher teacher){
		Teacher t1 = db.newInstance(Teacher.class,teacher.getName(),teacher.getSurname(),teacher.getSubjects());  		
		db.save(t1);
	}
	
	/*
	 * Extract all Teachers
	 */
	@Transactional
	public List<Teacher> getTeachers(){
		List<Teacher> tlist = new ArrayList<Teacher>();
		for (Teacher t : db.browseClass(Teacher.class)) {
			tlist.add(t);
        }
		return tlist;
	}
	
	/*
	 * Extract Teacher by name
	 */
	@Transactional
	public Teacher getTeacherByName(String name){
		for (Teacher t : db.browseClass(Teacher.class)) {
			if(name.equals(t.getName()))return t;
        }
		return null;
	}
	
}