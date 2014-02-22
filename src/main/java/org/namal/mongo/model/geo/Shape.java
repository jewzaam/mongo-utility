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

/**
 * Modeled to work well with MongoDB's structure. Location attribute that is needed for index purposes in MongoDB is
 * captured in a constant.
 *
 * @author nmalik
 */
public class Shape {
    public static final String ATTRIBUTE_LOCATION = "loc";

    private final Location loc = new Location();
    private final Map<String, Object> properties = new HashMap<>();

    public Location getLocation() {
        return loc;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }
}
