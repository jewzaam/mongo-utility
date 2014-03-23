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

import java.util.ArrayList;
import java.util.List;
import org.jewzaam.mongo.model.MongoObject;

/**
 * Gson friendly representation of GeoJSON Feature Collection.
 *
 * @author jewzaam
 */
public class FeatureCollection extends MongoObject {
    private final String type = "FeatureCollection";
    private final List<Feature> features = new ArrayList<>();

    public String getType() {
        return type;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void addFeature(Feature feature) {
        features.add(feature);
    }

    public Feature addPoint() {
        Feature f = Feature.createPoint();
        addFeature(f);
        return f;
    }

    public Feature addLineString() {
        Feature f = Feature.createLineString();
        addFeature(f);
        return f;
    }

    public Feature addPolygon() {
        Feature f = Feature.createPolygon();
        addFeature(f);
        return f;
    }
}
