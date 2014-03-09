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

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.Iterator;
import org.jewzaam.mongo.convert.Converter;
import org.jewzaam.mongo.convert.GsonConverter;

/**
 *
 * @author nmalik
 * @param <T>
 */
public class MongoIterator<T> implements Iterator<T> {
    private final DBCursor cur;
    private final Converter converter;
    private final Class clazz;

    /**
     * Create an iterator with converter set to GsonConverter.
     *
     * @param cur
     * @param clazz
     */
    public MongoIterator(DBCursor cur, Class<T> clazz) {
        this.cur = cur;
        this.clazz = clazz;
        converter = new GsonConverter();
    }

    @Override
    public boolean hasNext() {
        return cur.hasNext();
    }

    @Override
    public T next() {
        DBObject dbObj = cur.next();
        String json = converter.toJson(dbObj);
        return (T) converter.fromJson(json, clazz);
    }

    @Override
    public void remove() {
        cur.remove();
    }
}
