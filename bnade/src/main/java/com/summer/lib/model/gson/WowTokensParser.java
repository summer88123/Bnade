package com.summer.lib.model.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import com.summer.lib.model.entity.WowTokens;

import java.lang.reflect.Type;

/**
 * Created by kevin.bai on 2017/4/4.
 */

public class WowTokensParser implements JsonDeserializer<WowTokens> {
    @Override
    public WowTokens deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray array = json.getAsJsonArray();
        WowTokens token = new WowTokens();
        token.setLastModified(array.get(0).getAsLong());
        token.setGold(array.get(1).getAsInt());
        return token;
    }

    public static WowTokensParser create() {
        return new WowTokensParser();
    }
}
