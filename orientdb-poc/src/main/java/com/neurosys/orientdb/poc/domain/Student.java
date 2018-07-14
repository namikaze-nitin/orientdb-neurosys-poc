package com.neurosys.orientdb.poc.domain;
/**
* @author Rajat Nayak 
 * @author Nitin Sharma
 *
 */

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="Student")
public class Student implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Integer sid;
	
	@Column
	@Basic(optional=false)
	private String name;
	
	@Column
	@Basic(optional=false)
	private String surname;
	
	@ManyToOne
	@JoinColumn(name="subid")
	private Subject subject;
	
	public Student() {	
	}

	public Student(String name, String surName , Subject subject) {
		super();
		this.name = name;
		this.surname = surName;
		this.subject = subject;
	}
	
	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
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
	
	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	@Override
    public String toString() {
    	
        StringBuilder builder = new StringBuilder();

        builder.append("Student[");
        builder.append("studentId=").append(getSid()).append(",");
        builder.append("Name=").append(getName()).append(",");
        builder.append("surName=").append(getSurname()).append(",");
        builder.append("studies=").append(getSubject());
        builder.append("]");

        return builder.toString();
    }
}


