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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Information about a source file.
 *
 * @author jewzaam
 */
public class Testing {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

    public class Dates {
        public String lastUpdateDate;
        public String fileModifiedDate;
    }

    public final Dates dates = new Dates();
    private String filename;
    private String lastUpdateDate;
    public String[] array;
    private transient Date _lastUpdateDate;

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return the lastUpdateDate
     * @throws java.text.ParseException
     */
    public Date getLastUpdateDate() throws ParseException {
        if (_lastUpdateDate == null && lastUpdateDate != null) {
            _lastUpdateDate = sdf.parse(lastUpdateDate);
        }
        return _lastUpdateDate;
    }

    /**
     * @param lastUpdateDate the lastUpdateDate to set
     */
    public void setLastUpdateDate(Date lastUpdateDate) {
        this._lastUpdateDate = lastUpdateDate;
        this.lastUpdateDate = sdf.format(this._lastUpdateDate);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
