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
package org.jewzaam.mongo.model.geojson;

import java.util.HashMap;
import java.util.Map;
import org.jewzaam.mongo.model.MongoObject;

/**
 * Gson friendly representation GeoJSON of Feature.
 *
 * @author jewzaam
 */
public class Feature extends MongoObject {
    public static final String ATTRIBUTE_GEOMETRY = "geometry";

    private final String type = "Feature";
    private final Map<String, Object> properties = new HashMap<>();

    private final Geometry geometry;

    public static Feature createPoint() {
        return new Feature(new Geometry<double[]>(LocationType.Point));
    }

    public static Feature createLineString() {
        return new Feature(new Geometry<double[][]>(LocationType.LineString));
    }

    public static Feature createPolygon() {
        return new Feature(new Geometry<double[][]>(LocationType.Polygon));
    }

    private <T> Feature(Geometry<T> geometry) {
        this.geometry = geometry;
    }

    public <T> void setCoordinates(T coordiantes) {
        geometry.setCoordinates(coordiantes);
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public String getType() {
        return type;
    }
}
