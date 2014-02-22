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
package org.namal.mongo.convert;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import org.namal.mongo.model.geo.LocationType;

/**
 * Simple converter that uses Gson. Performance is not great per various performance benchmarks but it works.
 *
 * @author nmalik
 */
public class GsonConverter implements Converter {
    private static final Gson GSON = new Gson();

    /**
     * If using this API you must be careful about the return type of your code, use the concrete class you need.
     *
     * @param <T>
     * @param jsonString
     * @return
     */
    @Override
    public <T> T fromJson(String jsonString) {
        Type type = new TypeToken<T>() {
        }.getType();
        return GSON.fromJson(jsonString, type);
    }

    @Override
    public <T> T fromJson(String jsonString, Class<T> clazz) {
        return GSON.fromJson(jsonString, clazz);
    }

    @Override
    public String toJson(Object obj) {
        return GSON.toJson(obj);
    }
}
