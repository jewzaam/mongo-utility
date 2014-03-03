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
package org.namal.mongo;

import com.mongodb.WriteResult;

/**
 *
 * @author jewzaam
 */
public class Result {
    private final String error;
    private final int count;

    public Result(WriteResult result) {
        error = result.getError();
        count = result.getN();
    }

    /**
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    public boolean isError() {
        return null == error;
    }
}
