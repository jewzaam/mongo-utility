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
package org.namal.mongo.command;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import org.jewzaam.mongo.MongoIterator;
import org.jewzaam.mongo.convert.Converter;

/**
 * Uses Hystrix to isolate your application from a misbehaving database. Default
 * configuration is used.
 *
 * @author jewzaam
 * @param <T>
 */
public class MongoFindCommand<T> extends HystrixCommand<MongoIterator<T>> {
    private final DB db;
    private final String collectionName;
    private final T search;
    private String jsonQuery;
    private final String jsonProjection;
    private final int limit;
    private final Converter converter;
    private final Class clazz;

    /**
     * Create a command to execute a Find.
     *
     * @param db the DB object
     * @param collectionName name of collection
     * @param search search object
     * @param jsonProjection projection if want subset of results
     * @param limit number of objects to return, less than 0 indicates all
     * @param converter converter implementation to use
     * @param clazz the Class of object finding
     */
    public MongoFindCommand(DB db, String collectionName, T search, String jsonProjection,
            int limit, Converter converter, Class<T> clazz) {
        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("MongoFind"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("MongoFind"))
        );

        this.db = db;
        this.collectionName = collectionName;
        this.search = search;
        this.jsonQuery = null;
        this.jsonProjection = jsonProjection;
        this.limit = limit;
        this.converter = converter;
        this.clazz = clazz;
    }

    /**
     * Create a command to execute a Find.
     *
     * @param db the DB object
     * @param collectionName name of collection
     * @param jsonQuery the query to execute
     * @param jsonProjection projection if want subset of results
     * @param limit number of objects to return, less than 0 indicates all
     * @param converter converter implementation to use
     * @param clazz the Class of object finding
     */
    public MongoFindCommand(DB db, String collectionName, String jsonQuery, String jsonProjection, int limit, Converter converter, Class<T> clazz) {
        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("MongoFind"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("MongoFind"))
        );
        this.db = db;
        this.collectionName = collectionName;
        this.search = null;
        this.jsonQuery = jsonQuery;
        this.jsonProjection = jsonProjection;
        this.limit = limit;
        this.converter = converter;
        this.clazz = clazz;
    }

    @Override
    protected MongoIterator<T> run() {
        if (null != search) {
            jsonQuery = converter.toJson(search);
        }
        DBCollection coll = db.getCollection(collectionName);

        DBObject query = converter.fromJson(jsonQuery, BasicDBObject.class);
        DBObject projection = null;

        if (jsonProjection != null) {
            projection = converter.fromJson(jsonProjection, BasicDBObject.class);
        }

        DBCursor cur = coll.find(query, projection);
        if (limit >= 0) {
            cur = cur.limit(limit);
        }

        return new MongoIterator<>(cur, clazz);
    }
}
