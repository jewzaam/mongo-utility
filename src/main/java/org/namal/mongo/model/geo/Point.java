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
package org.namal.mongo.model.geo;

import java.util.HashMap;
import java.util.Map;
import org.namal.mongo.model.MongoObject;

/**
 * Modeled to work well with MongoDB's structure. Location attribute that is
 * needed for index purposes in MongoDB is captured in a constant.
 *
 * @author nmalik
 */
public class Point extends MongoObject {
    public static final String ATTRIBUTE_LOCATION = "loc";

    private final Coordinate loc = new Coordinate();
    private final Map<String, Object> properties = new HashMap<>();

    public Coordinate getLocation() {
        return loc;
    }

    public void setCoordinates(double[] coordiantes) {
        loc.setCoordinates(coordiantes);
    }

    public Map<String, Object> getProperties() {
        return properties;
    }
}
