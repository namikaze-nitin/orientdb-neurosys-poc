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

import com.neurosys.orientdb.poc.domain.Student;
import com.neurosys.orientdb.poc.domain.Subject;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

@Component
public class StudentObjectDao {

	@PersistenceContext
	OObjectDatabaseTx db;
	
	/**
	 * Set the database connection by saving it into data member : db
	 * @author ms : rajat kumar nayak
	 * @param database : instance of database object
	 */
	public StudentObjectDao(OObjectDatabaseTx database) {	
		this.db=database;
	}
    
	/**
	 * @author Rajat kumar nayak
	 * @param studenName : name
	 * @param StudentSurname : surname
	 * @param subject : Subject that he would study
	 * Persist in database
	 */
	@Transactional
	public void persist(String studentName,String studentSurname , Subject subject){
		Student st1 = db.newInstance(Student.class,studentName,studentSurname,subject);  		
		db.save(st1);
	}
	
	/**
	 * @author rajat kumar nayak
	 * @use Extract all Students
	 * @return List of Students
	 */
	@Transactional
	public List<Student> getStudents(){
		List<Student> slist = new ArrayList<Student>();
		for (Student s : db.browseClass(Student.class)) {
			slist.add(s);
        }
		return slist;
	}	
}