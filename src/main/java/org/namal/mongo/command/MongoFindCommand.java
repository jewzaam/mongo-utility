/*
 * Copyright (C) 2014 Naveen Malik
 *
 * Needless Compass is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Needless Compass is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Needless Compass.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.namal.mongo.command;

import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.namal.mongo.MongoIterator;
import org.namal.mongo.convert.Converter;

/**
 * Uses Hystrix to isolate your application from a misbehaving database. Default configuration is used.
 *
 * @author jewzaam
 */
public class MongoFindCommand<T> extends HystrixCommand<MongoIterator<T>> {
    private final DB db;
    private final String collectionName;
    private final T search;
    private String jsonQuery;
    private final String jsonProjection;
    private final int limit;
    private final Converter converter;

    public MongoFindCommand(DB db, String collectionName, T search, String jsonProjection, int limit, Converter converter) {
        super(HystrixConfiguration.Setter(MongoFindCommand.class, "MongoLoad:" + search.getClass().getSimpleName()));
        this.db = db;
        this.collectionName = collectionName;
        this.search = search;
        this.jsonQuery = null;
        this.jsonProjection = jsonProjection;
        this.limit = limit;
        this.converter = converter;
    }

    public MongoFindCommand(DB db, String collectionName, String jsonQuery, String jsonProjection, int limit, Converter converter) {
        // using gson reflection TypeToken to get at the generic class T at runtime.
        super(HystrixConfiguration.Setter(MongoFindCommand.class, "MongoLoad:" + new TypeToken<T>() {
        }.getRawType().getSimpleName()));
        this.db = db;
        this.collectionName = collectionName;
        this.search = null;
        this.jsonQuery = jsonQuery;
        this.jsonProjection = jsonProjection;
        this.limit = limit;
        this.converter = converter;
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

        return new MongoIterator<>(cur);
    }
}
