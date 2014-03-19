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
package org.jewzaam.mongo.command;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import org.jewzaam.mongo.Result;
import org.jewzaam.mongo.convert.Converter;
import org.jewzaam.mongo.model.Prepareable;

/**
 * Uses Hystrix to isolate your application from a misbehaving database. Default configuration is used.
 *
 * @author jewzaam
 */
public class MongoUpsertCommand extends HystrixCommand<Result> {

    private final DB db;
    private final String collectionName;
    private final Object upsert;
    private final Converter converter;

    public MongoUpsertCommand(DB db, String collectionName, Object upsert, Converter converter) {
        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("MongoUpsert"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("MongoUpsert"))
        );
        this.db = db;
        this.collectionName = collectionName;
        this.upsert = upsert;
        this.converter = converter;
    }

    @Override
    protected Result run() {
        if (upsert instanceof Prepareable) {
            ((Prepareable) upsert).prepare();
        }
        
        String json = converter.toJson(upsert);

        DBObject dbObj;
        if (upsert instanceof DBObject) {
            dbObj = (DBObject) upsert;
        } else {
            dbObj = (DBObject) JSON.parse(json);
        }

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
