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

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;

/**
 *
 * @author jewzaam
 */
public class MongoDropCommand extends HystrixCommand<Boolean> {
    private final DB db;
    private final String collectionName;

    public MongoDropCommand(DB db, String collectionName) {
        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("MongoDrop"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("MongoDrop"))
        );
        this.db = db;
        this.collectionName = collectionName;
    }

    @Override
    protected Boolean run() throws Exception {
        try {
            db.requestStart();
            DBCollection coll = db.getCollection(collectionName);
            coll.drop();
        } finally {
            db.requestDone();
        }

        return Boolean.TRUE;
    }
}
