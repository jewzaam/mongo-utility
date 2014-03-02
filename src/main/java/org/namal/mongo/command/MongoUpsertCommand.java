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

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.namal.mongo.Result;
import org.namal.mongo.convert.Converter;

/**
 * Uses Hystrix to isolate your application from a misbehaving database. Default
 * configuration is used.
 *
 * @author jewzaam
 */
public class MongoUpsertCommand extends HystrixCommand<Result> {

    private final DB db;
    private final String collectionName;
    private final Object upsert;
    private final Converter converter;

    public MongoUpsertCommand(DB db, String collectionName, Object upsert, Converter converter) {
        super(HystrixCommandGroupKey.Factory.asKey("MongoUpsert:" + upsert.getClass().getSimpleName()));
        this.db = db;
        this.collectionName = collectionName;
        this.upsert = upsert;
        this.converter = converter;
    }

    @Override
    protected Result run() {
        String json = converter.toJson(upsert);
        DBObject dbObj = converter.fromJson(json, BasicDBObject.class);

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
}