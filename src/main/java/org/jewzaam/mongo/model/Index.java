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

/**
 *
 * @author nmalik
 */
public class Index {
    private String[] fields;
    private boolean unique;
    private boolean sparse;
    private boolean _2dsphere;
    private boolean _2d;

    /**
     * @return the fields
     */
    public String[] getFields() {
        return fields;
    }

    /**
     * @param fields the fields to set
     */
    public void setFields(String[] fields) {
        this.fields = fields;
    }

    /**
     * @return the unique
     */
    public boolean isUnique() {
        return unique;
    }

    /**
     * @param unique the unique to set
     */
    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    /**
     * @return the sparse
     */
    public boolean isSparse() {
        return sparse;
    }

    /**
     * @param sparse the sparse to set
     */
    public void setSparse(boolean sparse) {
        this.sparse = sparse;
    }

    /**
     * @return the _2dsphere
     */
    public boolean is2dsphere() {
        return _2dsphere;
    }

    /**
     * @param _2dsphere the _2dsphere to set
     */
    public void set2dsphere(boolean _2dsphere) {
        this._2dsphere = _2dsphere;
    }

    /**
     * @return the _2d
     */
    public boolean is2d() {
        return _2d;
    }

    /**
     * @param _2d the _2d to set
     */
    public void set2d(boolean _2d) {
        this._2d = _2d;
    }

}
