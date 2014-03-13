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
package org.jewzaam.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import org.jewzaam.mongo.command.MongoDropCommand;
import org.jewzaam.mongo.command.MongoFindCommand;
import org.jewzaam.mongo.command.MongoIndexCommand;
import org.jewzaam.mongo.command.MongoUpsertCommand;
import org.jewzaam.mongo.convert.Converter;
import org.jewzaam.mongo.convert.GsonConverter;
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
        initialize();
    }

    private synchronized void initialize() {
        if (null != db) {
            return;
        }

        // create default converter
        converter = new GsonConverter();

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

        // set default converter. can be overriden
        converter = new GsonConverter();
    }

    public DB getDB() {
        return db;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public Converter getConverter() {
        return converter;
    }

    public void drop(String collectionName) {
        new MongoDropCommand(db, collectionName).execute();
    }

    public void createIndex2dsphere(String collectionName, String locationProperty) {
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
        new MongoIndexCommand(db, collectionName, property).execute();
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
        return new MongoUpsertCommand(db, collectionName, obj, converter).execute();
    }

    /**
     * Convert JSON String to DBObject and call upsert.
     *
     * @param collectionName
     * @param jsonString
     * @return
     */
    protected Result upsert(String collectionName, String jsonString) {
        // convert json to BasicDBObject (a map!)
        DBObject dbObj = converter.fromJson(jsonString, BasicDBObject.class);

        // upsert
        return upsert(collectionName, dbObj);
    }

    /**
     * If an _id is supplied on the object it will do an upsert, else will do
     * insert.
     *
     * @param collectionName
     * @param dbObj
     * @return
     */
    public Result upsert(String collectionName, DBObject dbObj) {
        return new MongoUpsertCommand(db, collectionName, dbObj, converter).execute();
    }

    public <T> T findOne(String collectionName, T search) {
        MongoIterator<T> itr = new MongoFindCommand<>(db, collectionName, search, null, 1, converter, (Class<T>) search.getClass()).execute();
        return itr.hasNext() ? itr.next() : null;
    }

    public <T> T findOne(String collectionName, String jsonQuery, String jsonProjection, Class<T> clazz) {
        MongoIterator<T> itr = new MongoFindCommand<>(db, collectionName, jsonQuery, jsonProjection, 1, converter, clazz).execute();
        return itr.hasNext() ? itr.next() : null;
    }

    public <T> Iterator<T> find(String collectionName, T search) {
        return new MongoFindCommand<>(db, collectionName, search, null, -1, converter, (Class<T>) search.getClass()).execute();
    }

    public <T> Iterator<T> find(String collectionName, String jsonQuery, String jsonProjection, Class<T> clazz) {
        return find(collectionName, jsonQuery, jsonProjection, -1, clazz);
    }

    public <T> Iterator<T> find(String collectionName, T search, int limit) {
        return new MongoFindCommand<>(db, collectionName, search, null, limit, converter, (Class<T>) search.getClass()).execute();
    }

    public <T> Iterator<T> find(String collectionName, String jsonQuery, String jsonProjection, int limit, Class<T> clazz) {
        return new MongoFindCommand<>(db, collectionName, jsonQuery, jsonProjection, limit, converter, clazz).execute();
    }

    protected <T> Iterator<T> distinct(String collectionName, String key, String jsonQuery) {
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
