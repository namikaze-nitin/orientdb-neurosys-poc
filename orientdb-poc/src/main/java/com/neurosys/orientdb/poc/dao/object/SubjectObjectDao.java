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

import com.neurosys.orientdb.poc.domain.Subject;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

@Component
public class SubjectObjectDao {

	@PersistenceContext
	OObjectDatabaseTx db;
	
	/*
	 * Set the database connection
	 */
	public SubjectObjectDao(OObjectDatabaseTx database) {	
		this.db=database;
	}
    
	/*
	 * Persist in database
	 */
	@Transactional
	public void saveSubject( String subjectName){
		Subject su1 = db.newInstance(Subject.class,subjectName);  		
		db.save(su1);
	}

	/*
	 * Persist in database
	 */
	@Transactional
	public void saveSubject(Subject subject){
		Subject su1 = db.newInstance(Subject.class,subject.getName());
		db.save(su1);
	}
	
	/*
	 * Extract Subject according to `name`
	 */
	@Transactional
	public Subject getSubjectByName(String name){
		String sql = "select * from Subject where name = '"+name+"'";
        List<Subject> s =db.query(new OSQLSynchQuery<Subject>(sql));
		return s.get(0);        
	}
	
	/*
	 * Extract all Subjects
	 */
	@Transactional
	public List<Subject> getSubjects(){
		List<Subject> slist = new ArrayList<Subject>();
		for (Subject sub : db.browseClass(Subject.class)) {
			slist.add(sub);
        }
		return slist;
	}	
}