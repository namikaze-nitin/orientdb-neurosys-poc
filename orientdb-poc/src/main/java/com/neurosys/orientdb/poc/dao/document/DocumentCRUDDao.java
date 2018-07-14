package com.neurosys.orientdb.poc.dao.document;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.neurosys.orientdb.poc.document.TestDocumentCRUDDao;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

/**
 * @author Nitin Sharma, Rajat Nayak
 * @class : To check basic CRUD operations on a Document database 
 */

public class DocumentCRUDDao {

	ODatabaseDocumentTx db;

	/**
	 * @author : Nitin Sharma
	 * Constructor to save database object into datamember:db
	 * @param database : database name
	 */
	public DocumentCRUDDao(ODatabaseDocumentTx database){
		this.db=database;		
	}
	
	/**
	 * @author Nitin Sharma
	 * Save the document created
	 * @return : Boolean : 'true'(document successfully saved) OR 'false'(document not saved)
	 * @param : ODocument : document to be saved
	 */
	public Boolean saveDocument(ODocument doc){
		try{
			doc.save();
		}catch(Exception e){
			System.out.println(e);
			return false;
		}
		return true;
	}	
	
	/**
	 * @author Nitin Sharma
	 * Get All Record of Document Type belonging to a Class
	 * @return : List< ODocument >
	 * @param className : name of Class whose records are to be fetched
	 */
	public List<ODocument> getAllDocuments(String className){
		List<ODocument> result ;
		try{
		result = db.query(new OSQLSynchQuery("select from ".toString().concat(className)));
		}
		catch(Exception e){
			System.out.println(e);
			return null;
		}
		return result;
		
	}
	
	/**
	 * @author Nitin Sharma
	 * @use : noOfRecords in a class
	 * @return : no of records or null(if query fails)
	 * @param : name : name of Class whose record total is to be returned
	 */
	public Integer noOfRecords(String className){
		Integer no;
		try{
			no = (int) db.countClass(className);
		}
		catch(Exception e){
			System.out.println(e);
			return null;
		}

		return no;
	}
	
	/**
	 * @author Nitin Sharma
	 * @param className : Name of class whose record needs to be updated
	 * @param key : name of attribute to be updated
	 * @param value : value to be set
	 * @return : true(if successfull update) OR false(query fails/ Exception occurs)
	 */
			
	public Boolean updateRecord(String className, String key, String value){
		try{
			new ODocument(className).field(key,value).save();
		}catch(Exception e){
			System.out.println(e);
			return false;
		}
		return true;		
	}
	
	/**
	 * @author Nitin Sharma
	 * @param doc : document to be deleted
	 * @return : true(if successfull delete) OR false(query fails/ Exception occurs)
	 */
	public Boolean dropClass(String name){		
		try{
			for (ODocument animal : db.browseClass(name)) {
				   animal.delete();
				}
		}
		catch(Exception e){
			System.out.println(e);
			return false;
		}
		return true;
	}

	/**
	 * @author Nitin Sharma
	 * @param className : Name of class whose record needs to be deleted
	 * @param key : name of attribute to be updated
	 * @param value : value to be set
	 * @return : true(if successfull update) OR false(query fails/ Exception occurs)
	 */
	public Boolean dropRecord(ODocument doc){		
		try{
			doc.delete();
		}
		catch(Exception e){
			System.out.println(e);
			return false;
		}
		return true;
	}
}