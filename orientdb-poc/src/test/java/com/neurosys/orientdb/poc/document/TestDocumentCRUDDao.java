package com.neurosys.orientdb.poc.document;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.neurosys.orientdb.poc.dao.document.DocumentCRUDDao;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;

public class TestDocumentCRUDDao {
	private static ODatabaseDocumentTx db;
	private Logger log;
	
	@Before
    public void before() throws Exception {
		log = LoggerFactory.getLogger(TestDocumentCRUDDao.class);
		//Create in-memory Database for testing 
		db=new ODatabaseDocumentTx("memory:test").create();
		log.info("db.exists() : ");
		System.out.println("db.exists() : "+db.exists());
		
	}
	
	@After
	public void after(){
		log.info("drop database...");
		db.drop();
		System.out.println("database dropped...");
	}
	
	@Test
	public void testSingleRecordClass() throws Exception{
		log.info("Testing : testSingleRecordClass");
		DocumentCRUDDao docCheck=new DocumentCRUDDao(db);

		//create Document
		ODocument doc1=new ODocument("Teacher");
//		doc1.setClassName("Teacher");
		doc1.field("tId","1111");
		doc1.field("firstName","Nitin");
		doc1.field("sirName","Sharma");
		doc1.field("phone","8989813163");
		Assert.assertTrue(docCheck.saveDocument(doc1));
		Assert.assertSame(1,docCheck.noOfRecords("Teacher"));		
		Assert.assertTrue(docCheck.dropClass("Teacher"));
		Assert.assertSame(0,docCheck.noOfRecords("Teacher"));
		
		log.info("Test for \"testSingleRecordClass\" completed...");
	}
	
	@Test
	public void testMultipleRecordClass(){

		DocumentCRUDDao docCheck=new DocumentCRUDDao(db);

		ODocument doc1=new ODocument("Teacher");
		log.info("Testing : testMultipleRecordClass");
		
		//Create a class "King" consisting of 3 records
		ODocument king = new ODocument("King");
	    king.field("name", "John Snow");
	    king.field("location", "Winterfell");
	    Assert.assertTrue("John Document saved not saved... ",new DocumentCRUDDao(db).saveDocument(king));
	    
	    ODocument king1 = new ODocument("King");
	    king1.field("name", "Cersie Lannister");
	    king1.field("location", "Dragon Stone");
	    Assert.assertTrue("Cersie Document saved not saved... ",new DocumentCRUDDao(db).saveDocument(king1));	
	    
	    ODocument king2 = new ODocument("King");
	    king2.field("name", "Daenerys Targeryn");
	    king2.field("location", "Kings Landing");
	    Assert.assertTrue("Daenerys Document saved not saved... ",new DocumentCRUDDao(db).saveDocument(king2));
	    log.trace("Classes created...");

	    
	    //Fetch Results
	    ODocument forLoopDoc = new ODocument();
	    List<ODocument> result = new DocumentCRUDDao(db).getAllDocuments("King");
	    Assert.assertNotNull("Problem in loading documents from database : ",result);
	    for(ODocument docs : result){
	    	if(docs.field("location").toString().equals("Winterfell"))forLoopDoc=docs;
	    	break;
	    }
    	    
	    Assert.assertSame("John Snow",forLoopDoc.field("name").toString());
	    
	    Assert.assertSame(3,docCheck.noOfRecords("King"));
	    Assert.assertTrue(docCheck.dropRecord(king1));
	    Assert.assertSame("record might not be deleted : ",2,docCheck.noOfRecords("King"));
		Assert.assertTrue(docCheck.dropClass("King"));
	    Assert.assertSame("Some records still remains : ",0,docCheck.noOfRecords("King"));

//	    log.info("Test for \"testMultipleRecordClass\" completed...");
	}
}