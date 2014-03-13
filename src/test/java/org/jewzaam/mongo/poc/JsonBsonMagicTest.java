/*
 * Copyright (C) 2014 Naveen Malik
 *
 * Carnation Trout is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carnation Trout is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Carnation Trout.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jewzaam.mongo.poc;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * Dumping ground for interesting things I might want to use later.
 *
 * @author jewzaam
 */
public class JsonBsonMagicTest {

    /**
     * A magical test for converting a pojo to BSONObject and back. Feels wrong
     * that it is this easy.
     */
    @Test
    public void single() throws JSONException {
        Gson g = new Gson();

        Testing s = new Testing();
        s.setFilename("/home/jewzaam/Downloads/north-carolina-latest.shp/roads.shp");
        s.setLastUpdateDate(new Date());
        s.dates.fileModifiedDate = "asdf";
        s.dates.lastUpdateDate = "fdsa";
        s.array = new String[]{"1", "2", "a", "b"};
        String json = g.toJson(s);

        BasicDBObject obj = g.fromJson(json, BasicDBObject.class);

        JSONAssert.assertEquals(json, obj.toString(), false);

        Type type = new TypeToken< Testing>() {
        }.getType();

        Testing x = new Gson().fromJson(obj.toString(), type);

        JSONAssert.assertEquals(json, x.toString(), false);

        System.out.println("magic: " + json);
    }

    /**
     * Seeing if I can get "dynamic" properties back out into a map from BSON.
     */
    @Test
    public void dynamicMap() throws JSONException {
        Gson g = new Gson();

        MapPojo mp = new MapPojo();
        mp.properties.put("key", "value");
        mp.something = "some value";
        mp.properties.put("something else", "foo");

        String json = g.toJson(mp);

        BasicDBObject obj = g.fromJson(json, BasicDBObject.class);

        JSONAssert.assertEquals(json, obj.toString(), false);

        Type type = new TypeToken< MapPojo>() {
        }.getType();

        MapPojo x = new Gson().fromJson(obj.toString(), type);

        JSONAssert.assertEquals(json, x.toString(), false);
        System.out.println("dynamicMap: " + json);
    }

    @Test
    public void doubleArray2json() throws JSONException {
        String output = new Gson().toJson(new double[]{1.0, 2.2});
        JSONAssert.assertEquals("[1.0, 2.2]", output, false);
    }

    @Test
    public void double2dArray2json() throws JSONException {
        String output = new Gson().toJson(new double[][]{{1.0, 2.2}, {4.5, 6.7}});
        JSONAssert.assertEquals("[[1.0, 2.2],[4.5, 6.7]]", output, false);
    }

    @Test
    public void stringList2json() throws JSONException {
        List<String> l = new ArrayList<>();
        l.add("asdf");
        l.add("1234");
        String output = new Gson().toJson(l);
        JSONAssert.assertEquals("[\"asdf\",\"1234\"]", output, false);
    }
}
