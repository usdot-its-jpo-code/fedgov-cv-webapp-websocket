package gov.usdot.cv.websocket.mongo;

import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.WriteResult;

import gov.usdot.cv.common.database.mongodb.MongoClientBuilder;
import gov.usdot.cv.common.database.mongodb.dao.AbstractMongoDbDao;

public class CloseableInsertSitDataDao extends  AbstractMongoDbDao
{
    public static CloseableInsertSitDataDao newInstance(
                                               String mongoServerHost, 
                                               int mongoServerPort, 
                                               MongoOptions options,
                                               String dbname) throws UnknownHostException {
       MongoClientBuilder builder = new MongoClientBuilder();
       builder.setHost(mongoServerHost).setPort(mongoServerPort).setMongoOptions(options);
       return new CloseableInsertSitDataDao(builder.build(), dbname);
   }
   
   private CloseableInsertSitDataDao(Mongo mongo, String dbname) {
       super(mongo, dbname);
       this.mongo = mongo;
   }
   
   /**
    * Inserts the given document into a collection.
    */
   public WriteResult insert(String collectionName, DBObject doc) {
       DBCollection collection = get(collectionName);
       return collection.insert(doc);
   }
   
   /**
    * Updates a document in the collection or insert it if it doesn't exist.
    */
   public WriteResult upsert(String collectionName, DBObject query, DBObject doc) {
       DBCollection collection = get(collectionName);
       return collection.update(query, doc, true, false);
   }
   
   /**
    * Creates a expiration index on the given document field name for a collection..
    */
   public void createExpirationIndex(String collectionName, String fieldName, long durationInSec) {
       BasicDBObject index = new BasicDBObject(fieldName, 1);
       BasicDBObject options = new BasicDBObject(
           EXPIRE_AFTER_SECONDS_FIELD,
           TimeUnit.SECONDS.toSeconds(durationInSec));
       options.putAll(indexOptions);
       
       DBCollection collection = get(collectionName);
       collection.createIndex(index, options);
   }
   
   /**
    * Creates a 2d sphere index on the given document field name for a collection.
    */
   public void create2dSphereIndex(String collectionName, String fieldName) {
       BasicDBObject index = new BasicDBObject(fieldName, GEOSPATIAL_INDEX_VALUE);
       DBCollection collection = get(collectionName);
       collection.createIndex(index, indexOptions);
   }
   
   /**
    * Removes the given index from a collection.
    */
   public void dropIndex(String collectionName, String indexName) {
       DBCollection collection = get(collectionName);
       collection.dropIndex(indexName);
   }
   
   public void close() {
       mongo.close();
   }
   
   private Mongo mongo;
}
