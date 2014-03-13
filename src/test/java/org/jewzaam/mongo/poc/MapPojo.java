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
import java.util.HashMap;

/**
 *
 * @author jewzaam
 */
public class MapPojo {
    public String something;
    public HashMap<String, String> properties = new HashMap<>();

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
