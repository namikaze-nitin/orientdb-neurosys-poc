package com.neurosys.orientdb.poc.transform;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.neurosys.orientdb.poc.dao.document.DocumentCRUDDao;
import com.neurosys.orientdb.poc.document.TestDocumentCRUDDao;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 * @author : Nitin Sharma
 */

public class DocumentToObjectTransform {

	private static ODatabaseDocumentTx ddb;
	private static Logger log;
	
	@Before
    public void before() throws Exception {
		log = LoggerFactory.getLogger(TestDocumentCRUDDao.class);
		log.info("Logger Created...");
		ddb=new ODatabaseDocumentTx("memory:dzocumenttoobject").create();
		log.info("ObjectToDocument database created...");
	}
	
	@AfterClass
	public static void after() throws Exception {	
		ddb.drop();
		log.info("Database dropped : documenttoobject...");
	}
	
	@Test
	public void DocumentToObjectTransformer() throws Exception{
		
		ODocument sub=new ODocument("Subject");
		sub.field("name","Ninjutsu",OType.STRING);
		sub.save();
		
		ODocument doc1=new ODocument("Teacher");
		doc1.field("firstName","Hashirama",OType.STRING);
		doc1.field("sirName","Senju",OType.STRING);
		doc1.field("subject",sub,OType.LINK);
		doc1.save();
		
		Object ob=new DocumentCRUDDao(ddb).getAllDocuments("Teacher").get(0);
//		ODocument doc11=new DocumentCRUD(ddb).getAllDocuments("Teacher").get(0).field("subject");		
//		System.out.println(doc11.getSchemaClass());
		log.info("Document to Object Conversion : ");
	}
}