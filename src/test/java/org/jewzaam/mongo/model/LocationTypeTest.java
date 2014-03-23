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
package org.jewzaam.mongo.model;

import org.json.JSONException;
import org.junit.Test;
import org.jewzaam.mongo.convert.Converter;
import org.jewzaam.mongo.convert.GsonConverter;
import org.jewzaam.mongo.model.geojson.LocationType;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * Test the bits of Gson that we need documented or verified.
 *
 * @author nmalik
 */
public class LocationTypeTest {

    Converter converter = new GsonConverter();

    @Test
    public void gson() throws JSONException {
        // easiest to just name enum values the same as the value you want serialized
        LocationType type = LocationType.LineString;
        String json = converter.toJson(type);

        JSONAssert.assertEquals('"' + type.toString() + '"', json, false);
    }
}
