package com.summer.lib.model.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import com.summer.lib.model.entity.AuctionItem;
import com.summer.lib.model.entity.LastTime;

import java.lang.reflect.Type;

/**
 * Created by kevin.bai on 2017/4/4.
 */

public class AuctionItemParser implements JsonDeserializer<AuctionItem> {
    @Override
    public AuctionItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws
            JsonParseException {
        JsonArray array = json.getAsJsonArray();
        AuctionItem item = new AuctionItem();
        item.setRealmId(array.get(0).getAsLong());
        item.setMinBuyOut(array.get(1).getAsLong());
        item.setName(array.get(2).getAsString());
        item.setTotal(array.get(3).getAsInt());
        item.setLastUpdateTime(array.get(4).getAsLong());
        item.setLastTime(LastTime.valueOf(array.get(5).getAsString()));
        return item;
    }

    public static AuctionItemParser create() {
        return new AuctionItemParser();
    }
}
