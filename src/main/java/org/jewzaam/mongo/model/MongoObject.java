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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Semaphore;

/**
 * Base mongo object that sets a value for _id that's a string based on
 * System.currentTimeMillis().
 *
 * @author jewzaam
 */
public abstract class MongoObject {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    private static final Semaphore sem = new Semaphore(1);
    private static long idSequence = System.currentTimeMillis();

    private String _id;

    private String owner;

    private String creation_date;
    private transient Date _creation_date;

    private String created_by;

    private String last_update_date;
    private transient Date _last_update_date;

    private String last_updated_by;

    public MongoObject() {
    }
    
    /**
     * Sets new value for _id and defaults creation date and last update date to current timestamp.
     */
    public <T> T initialize() {
        // default the _id
        try {
            sem.acquire();
            _id = String.valueOf(idSequence++);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        } finally {
            sem.release();
        }

        Date date = new Date();
        setCreationDate(date);
        setLastUpdateDate(date);
        
        return (T) this;
    }

    /**
     * @return the _id
     */
    public String getId() {
        return _id;
    }

    /**
     * @param _id the _id to set
     */
    public void setId(String _id) {
        this._id = _id;
    }

    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @return the creation_date
     */
    public Date getCreationDate() throws ParseException {
        if (_creation_date == null && creation_date != null) {
            _creation_date = sdf.parse(creation_date);
        }
        return _creation_date;
    }

    /**
     * @param creation_date the creation_date to set
     */
    public final void setCreationDate(Date creation_date) {
        if (creation_date != null) {
            this._creation_date = creation_date;
            this.creation_date = sdf.format(creation_date);
        } else {
            this._creation_date = null;
            this.creation_date = null;
        }
    }

    /**
     * @return the created_by
     */
    public String getCreatedBy() {
        return created_by;
    }

    /**
     * @param created_by the created_by to set
     */
    public void setCreatedBy(String created_by) {
        this.created_by = created_by;
    }

    /**
     * @return the last_update_date
     */
    public Date getLastUpdateDate() throws ParseException {
        if (_last_update_date == null && last_update_date != null) {
            _last_update_date = sdf.parse(last_update_date);
        }
        return _last_update_date;
    }

    /**
     * @param last_update_date the last_update_date to set
     */
    public final void setLastUpdateDate(Date last_update_date) {
        if (last_update_date != null) {
            this._last_update_date = last_update_date;
            this.last_update_date = sdf.format(last_update_date);
        } else {
            this._last_update_date = null;
            this.last_update_date = null;
        }
    }

    /**
     * @return the last_updated_by
     */
    public String getLastUpdatedBy() {
        return last_updated_by;
    }

    /**
     * @param last_updated_by the last_updated_by to set
     */
    public void setLastUpdatedBy(String last_updated_by) {
        this.last_updated_by = last_updated_by;
    }
}
