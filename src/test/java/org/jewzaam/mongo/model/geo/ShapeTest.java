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
package org.jewzaam.mongo.model.geo;

import org.jewzaam.mongo.AbstractMongoTest;
import org.junit.Test;

/**
 * Verify things about point.
 *
 * @author jewzaam
 */
public class ShapeTest extends AbstractMongoTest {

    /**
     * Verify that 2dsphere index on point works
     */
    @Test
    public void test2dsphere() {
        String collectionName = "testpoint";
        Shape shape = new Shape();
        shape.setOwner("default");
        shape.getLocation().setCoordinates(new double[][]{{35.662709, -78.63383600000002}, {35.662708, -78.63383600000001}});
        crud.upsert(collectionName, shape);

        crud.createIndex2dsphere(collectionName, Shape.ATTRIBUTE_LOCATION);
    }
}
