package com.neurosys.orientdb.poc.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jboss.logging.annotations.Property;

/**
* @author Rajat Nayak 
 * @author Nitin Sharma
 *
 */

@Entity
@Table(name="Subject")
public class Subject implements Serializable{

	@Id
	@Column
	@Basic(optional=false)
	private Integer subid;	
	
	@Column
	@Basic(optional=false)
	private String name;
	
	public Subject() {
	}
	
	public Subject( String name) {
		super();
		this.name = name;
	}	
	

	public Integer getSubid() {
		return subid;
	}

	public void setSubid(Integer subid) {
		this.subid = subid;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	@Override
    public String toString() {  
		
        StringBuilder builder = new StringBuilder();
        builder.append("Subject[").append("subjectId=");
        
        StringBuilder sb = new StringBuilder();
        sb.append(4);
        String string7 = sb.toString();
        System.out.println(string7);

        builder.append(string7);
        builder.append(",").append("subjectName=").append(getName()).append("]");

        return builder.toString();
    }
}
