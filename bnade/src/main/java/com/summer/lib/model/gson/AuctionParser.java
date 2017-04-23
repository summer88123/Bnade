package com.summer.lib.model.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import com.summer.lib.model.entity.Auction;

import java.lang.reflect.Type;

/**
 * Created by kevin.bai on 2017/4/23.
 */

public class AuctionParser implements JsonDeserializer<Auction> {
    @Override
    public Auction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws
            JsonParseException {
        JsonArray array = json.getAsJsonArray();
        Auction item = new Auction();

        item.setItemId(array.get(0).getAsLong());
        item.setName(array.get(1).getAsString());
        item.setRealmName(array.get(2).getAsString());
        item.setBidPrece(array.get(3).getAsLong());
        item.setBuyOut(array.get(4).getAsLong());
        item.setCount(array.get(5).getAsInt());
        item.setLastTime(array.get(5).getAsString());
        item.setLastModified(array.get(5).getAsLong());
        return item;
    }

    public static AuctionParser create() {
        return new AuctionParser();
    }
}
