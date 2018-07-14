package com.neurosys.orientdb.poc.geolocation;

/**
 * @author : Nitin Sharma
 */
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.neurosys.orientdb.poc.dao.document.DocumentCRUDDao;
import com.neurosys.orientdb.poc.document.TestDocumentCRUDDao;
import com.orientechnologies.orient.core.command.OCommandContext;
import com.orientechnologies.orient.core.command.OCommandRequest;
import com.orientechnologies.orient.core.command.OCommandContext.TIMEOUT_STRATEGY;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OResultSet;
import com.orientechnologies.orient.core.sql.query.OSQLAsynchQuery;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

public class GeoSpatialTest{
	@Test
	public void testPoint(){
		
		List<ODocument> execute;
		Logger log= LoggerFactory.getLogger(GeoSpatialTest.class);
		ODatabaseDocumentTx databaseDocumentTx = new ODatabaseDocumentTx("memory:testmycode").create();
		
		OSchema schema = databaseDocumentTx.getMetadata().getSchema();
		OClass oClass = schema.createClass("Restaurant");
		oClass.createProperty("name",OType.STRING);
		oClass.createProperty("location",OType.EMBEDDED);
	
		ODocument locationDoc = new ODocument("OPoint");
		locationDoc.field("coordinates", Arrays.asList(12.4684735, 41.8914114));		
		ODocument locationDoc1 = new ODocument("OPoint");
		locationDoc1.field("coordinates", Arrays.asList(12.5684735, 41.8914114));
		ODocument locationDoc2 = new ODocument("OPoint");
		locationDoc2.field("coordinates", Arrays.asList(0, 1));

		ODocument first= new ODocument("Restaurant");
		first.field("name","Dar Poeta");
		first.field("location",locationDoc);
		first.save();		
		ODocument third= new ODocument("Restaurant");
		third.field("name","Far Away");
		third.field("location",locationDoc2);
		third.save();
		ODocument second= new ODocument("Restaurant");
		second.field("name","San Fransisco");
		second.field("location",locationDoc1);
		second.save();
		
		//0022334780791423052
		databaseDocumentTx.command(new OCommandSQL("CREATE INDEX Restaurant.location ON Restaurant(location) SPATIAL ENGINE LUCENE"));
		
		//ST_geomFromText : geom from String
		String geomFromText="select ST_GeomFromText(\"POINT (12.4684635 41.8914114)\")";
		//String geomFromText="select ST_GeomFromText(\"POINT (12.4684635 41.8914114 33.3456343)\")";
		execute=databaseDocumentTx.query(new OSQLSynchQuery<ODocument>(geomFromText));
		System.out.println("OPOint from String : " + execute.get(0).field("ST_GeomFromText"));

		
		//ST_Equals : if geom 1 is equal to geom2
		String stEquals="SELECT ST_Equals(ST_GeomFromText('LINESTRING(0 0, 10 10)'), ST_GeomFromText('LINESTRING(0 0, 5 5, 10 10)'))";
		execute=databaseDocumentTx.query(new OSQLSynchQuery<ODocument>(stEquals));
		System.out.println("Entered lines coincide : " + execute.get(0));
		
		//ST_Within : all points within Polygon
		String within="select * from Restaurant where  ST_WITHIN(location,'POLYGON ((12.314015 41.8262816, 12.314015 41.963125, 12.6605063 41.963125, 12.6605063 41.8262816, 12.314015 41.8262816))') = true";
		execute=databaseDocumentTx.query(new OSQLSynchQuery<ODocument>(within));
		log.info("how many points within polygon : " + execute.size());
		System.out.println("First Loc : " + execute.get(0).field("name"));
		System.out.println("Second Loc : : " + execute.get(1).field("name"));
                                                                                                                        		
		//ST_Intersect : do they intersect?
		String intersect="SELECT ST_Intersects(location, ST_GeomFromText('LINESTRING ( 0 0, 0 2 )'))  from Restaurant";
		execute=databaseDocumentTx.query(new OSQLSynchQuery<ODocument>(intersect));
		System.out.println("Intersect: " + execute.get(0));
		System.out.println("Intersect: " + execute.get(1));
		System.out.println("Intersect: " + execute.get(2));
		
		
		//ST_Distance
		String distance="select ST_Distance(location,ST_GEOMFROMTEXT('POINT(12.4664632 41.8904382)')) as distance from Restaurant";
		execute=databaseDocumentTx.query(new OSQLSynchQuery<ODocument>(distance));
		log.info("distance :");
		System.out.println(execute.get(0));
		System.out.println(execute.get(1));
		System.out.println(execute.get(2));
		
		//ST_Contains : whether GEOM2 contains GEOM1
		String contain="SELECT ST_Contains(ST_Buffer(ST_GeomFromText('POINT(0 0)'),10),ST_GeomFromText('POINT(12 0)'))";
		execute=databaseDocumentTx.query(new OSQLSynchQuery<ODocument>(contain));
		log.info("contains? : " +execute.get(0).field("ST_Contains"));		
		
		
		databaseDocumentTx.drop();
	}	
}

