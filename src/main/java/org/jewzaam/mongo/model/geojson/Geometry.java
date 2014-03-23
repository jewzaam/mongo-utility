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

/**
 *
 * @author jewzaam
 * @param <T> double[] or double[][]
 */
public class Geometry<T> {

    private T coordinates;
    private final String type;

    public Geometry(LocationType type) {
        this.type = type.name();
    }

    public T getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(T coordinates) {
        this.coordinates = coordinates;
    }

    public LocationType getType() {
        return LocationType.valueOf(type);
    }
}
