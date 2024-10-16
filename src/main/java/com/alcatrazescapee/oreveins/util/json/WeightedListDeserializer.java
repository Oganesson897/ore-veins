/*
 * Part of the Ore Veins Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.oreveins.util.json;

import java.lang.reflect.Type;

import com.google.gson.*;

import com.alcatrazescapee.oreveins.util.IWeightedList;
import com.alcatrazescapee.oreveins.util.WeightedList;

public class WeightedListDeserializer<T> implements JsonDeserializer<IWeightedList<T>>
{
    private final Class<T> elementClass;

    public WeightedListDeserializer(Class<T> elementClass)
    {
        this.elementClass = elementClass;
    }

    @Override
    public IWeightedList<T> deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
    {
        if (json.isJsonPrimitive() || json.isJsonObject())
        {
            T element = context.deserialize(json, elementClass);
            return IWeightedList.singleton(element);
        }
        else if (json.isJsonArray())
        {
            JsonArray array = json.getAsJsonArray();
            IWeightedList<T> weightedList = new WeightedList<>();
            for (JsonElement element : array)
            {
                JsonObject obj = element.getAsJsonObject();
                double weight = obj.has("weight") ? obj.get("weight").getAsDouble() : 1;
                weightedList.add(weight, context.deserialize(element, elementClass));
            }
            return weightedList;
        }
        throw new JsonParseException("Unable to parse IBlockState List");
    }
}
