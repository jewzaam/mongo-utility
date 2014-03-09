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

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author jewzaam
 */
public class MongoIteratorTest extends AbstractMongoTest {

    @Test
    public void next() {
        String collectionName = "test";
        TestModel obj = new TestModel();
        obj.name = "foo";
        crud.upsert(collectionName, obj);

        DB db = crud.getDB();

        DBCollection coll = db.getCollection(collectionName);

        DBCursor cur = coll.find(null, null);

        MongoIterator<TestModel> itr = new MongoIterator<>(cur, TestModel.class);

        Assert.assertTrue(itr.hasNext());
        TestModel found = itr.next();

        Assert.assertNotNull(found);
    }
}
