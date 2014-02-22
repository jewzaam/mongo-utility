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

import org.namal.mongo.model.MongoObject;

/**
 *
 * @author nmalik
 */
public class Location extends MongoObject {

    private double[][] coordinates;
    private String type;
    private transient LocationType _type;

    /**
     * @return the coordinates
     */
    public double[][] getCoordinates() {
        return coordinates;
    }

    /**
     * @param coordinates the coordinates to set
     */
    public void setCoordinates(double[][] coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * @return the _type
     */
    public LocationType getType() {
        return _type;
    }

    /**
     * @param _type the _type to set
     */
    public void setType(LocationType _type) {
        if (_type != null) {
            this.type = _type.toString();
        }
        this._type = _type;
    }

}
