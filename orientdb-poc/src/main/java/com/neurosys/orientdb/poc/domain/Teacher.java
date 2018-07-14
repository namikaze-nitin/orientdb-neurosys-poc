package com.neurosys.orientdb.poc.domain;
/**
* @author Rajat Nayak 
 * @author Nitin Sharma
 *
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="Teacher")
public class Teacher implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Integer tid;
	
	@Column
	@Basic(optional=false)
	private String name;
	
	@Column
	@Basic(optional=false)
	private String surname;
	
	@OneToOne(targetEntity=Subject.class, fetch=FetchType.EAGER)
	private Subject subjects;
	
	public Teacher() {	
	}

	/*public Teacher(String name, String surName , List<Subject> subject) {
		this.name = name;
		this.surname = surName;
		this.subjects = subject;
	}*/	
	
	public Teacher(String name, String surName , Subject subject) {
		this.name = name;
		this.surname = surName;		
		this.subjects = subject ;
	}	

	public Integer getTid() {
		return tid;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
	}	

	public Subject getSubjects() {
		return subjects;
	}

	public void setSubjects(Subject subjects) {
		this.subjects = subjects;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}	

	@Override
    public String toString() {
    	
        StringBuilder builder = new StringBuilder();

        builder.append("Teacher[");
        builder.append("teacherId=").append(getTid()).append(",");
        builder.append("Name=").append(getName()).append(",");
        builder.append("surName=").append(getSurname()).append(",");
        builder.append("studies=").append(getSubjects());
        builder.append("]");

        return builder.toString();
    }
}


