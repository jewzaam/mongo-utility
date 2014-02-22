/*
 * Copyright (C) 2014 Naveen Malik
 *
 * mongo-utility is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mongo-utility is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mongo-utility.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.namal.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import org.namal.mongo.convert.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jewzaam
 */
public class MongoCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoCRUD.class);

    private final Configuration config;
    private Converter converter;

    private MongoClient client;
    private DB db;

    public MongoCRUD(String dbName) {
        config = new Configuration();
        config.setName(dbName);
        initialize();
    }

    public MongoCRUD(Configuration config) {
        this.config = config;
    }

    private synchronized void initialize() {
        if (null != db) {
            return;
        }

        // call to coalesce has this priority: configuration, openshift, localhost defaults
        String dbHost = coalesce(config.getHost(), System.getenv("OPENSHIFT_MONGODB_DB_HOST"), null);
        String dbPort = coalesce(config.getPort(), System.getenv("OPENSHIFT_MONGODB_DB_PORT"), null);
        String dbName = coalesce(config.getName(), System.getenv("OPENSHIFT_APP_NAME"), null);
        String user = coalesce(config.getUser(), System.getenv("OPENSHIFT_MONGODB_DB_USERNAME"), null);
        String password = coalesce(config.getPassword(), System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD"), null);

        try {
            if (dbHost != null) {
                int port = Integer.decode(dbPort);

                client = new MongoClient(dbHost, port);
                db = client.getDB(dbName);
                if (user != null && db.authenticate(user, password.toCharArray()) == false) {
                    throw new RuntimeException("Failed to authenticate against db: " + dbName);
                }
            } else {
                client = new MongoClient();
                db = client.getDB(dbName);
            }
        } catch (UnknownHostException ex) {
            throw new RuntimeException("Unable to configure mongo", ex);
        }
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public void drop(String collectionName) {
        initialize();
        try {
            db.requestStart();
            DBCollection coll = db.getCollection(collectionName);
            coll.drop();
        } finally {
            db.requestDone();
        }
    }

    public void createIndex2dsphere(String collectionName, String locationProperty) {
        initialize();
        try {
            db.requestStart();
            DBCollection coll = db.getCollection(collectionName);

            BasicDBObject obj = new BasicDBObject(locationProperty, "2dsphere");

            coll.ensureIndex(obj);
        } finally {
            db.requestDone();
        }
    }

    public void createIndex(String collectionName, String property) {
        initialize();
        try {
            db.requestStart();
            DBCollection coll = db.getCollection(collectionName);

            BasicDBObject obj = new BasicDBObject(property, 1);

            coll.ensureIndex(obj);
        } finally {
            db.requestDone();
        }
    }

    /**
     * Convert Object to JSON String and call upsert.
     *
     * @param collectionName
     * @param obj
     * @return
     *
     * @see #upsert(java.lang.String, java.lang.String)
     */
    public Result upsert(String collectionName, Object obj) {
        // convert to json
        String json = converter.toJson(obj);

        // upsert
        return upsert(collectionName, json);
    }

    /**
     * Convert JSON String to DBObject and call upsert.
     *
     * @param collectionName
     * @param jsonString
     * @return
     */
    public Result upsert(String collectionName, String jsonString) {
        // convert json to BasicDBObject (a map!)
        DBObject dbObj = converter.fromJson(jsonString, BasicDBObject.class);

        // upsert
        return upsert(collectionName, dbObj);
    }

    /**
     * If an _id is supplied on the object it will do an upsert, else will do insert.
     *
     * @param collectionName
     * @param dbObj
     * @return
     */
    public Result upsert(String collectionName, DBObject dbObj) {
        initialize();
        try {
            db.requestStart();
            DBCollection coll = db.getCollection(collectionName);

            if (dbObj.get("_id") != null) {
                BasicDBObject query = new BasicDBObject()
                        .append("_id", dbObj.get("_id"));

                return new Result(coll.update(query, dbObj, true, false));
            } else {
                return new Result(coll.insert(dbObj));
            }
        } finally {
            db.requestDone();
        }
    }

    public <T> T findOne(String collectionName, String jsonQuery, String jsonProjection) {
        Iterator<T> result = find(collectionName, jsonQuery, jsonProjection, 1);
        if (result != null && result.hasNext()) {
            return result.next();
        } else {
            return null;
        }
    }

    public <T> Iterator<T> find(String collectionName, String jsonQuery, String jsonProjection) {
        return find(collectionName, jsonQuery, jsonProjection, -1);
    }

    public <T> Iterator<T> find(String collectionName, String jsonQuery, String jsonProjection, int limit) {
        initialize();
        DBCollection coll = db.getCollection(collectionName);

        DBObject query = converter.fromJson(jsonQuery, BasicDBObject.class);
        DBObject projection = converter.fromJson(jsonProjection, BasicDBObject.class);

        DBCursor cur = coll.find(query, projection);
        if (limit >= 0) {
            cur = cur.limit(limit);
        }

        return new MongoIterator<>(cur);
    }

    public <T> Iterator<T> distinct(String collectionName, String key, String jsonQuery) {
        initialize();
        DBCollection coll = db.getCollection(collectionName);

        DBObject query = null;

        if (jsonQuery != null && !jsonQuery.isEmpty()) {
            query = converter.fromJson(jsonQuery, BasicDBObject.class);
        }

        List<T> output = (List<T>) coll.distinct(key, query);
        return output.iterator();
    }

    private String coalesce(String a, String b, String c) {
        return a != null ? a : (b != null ? b : c);
    }
}
