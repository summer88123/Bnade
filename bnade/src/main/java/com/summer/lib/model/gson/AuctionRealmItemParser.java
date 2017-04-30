package com.summer.lib.model.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import com.summer.lib.model.entity.AuctionRealmItem;
import com.summer.lib.model.entity.Gold;
import com.summer.lib.model.entity.LastTime;

import java.lang.reflect.Type;

/**
 * Created by kevin.bai on 2017/4/28.
 */

public class AuctionRealmItemParser implements JsonDeserializer<AuctionRealmItem> {
    private AuctionRealmItemParser() {
    }

    public static AuctionRealmItemParser create() {
        return new AuctionRealmItemParser();
    }

    @Override
    public AuctionRealmItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws
            JsonParseException {
        JsonArray array = json.getAsJsonArray();
        AuctionRealmItem item = new AuctionRealmItem();
        item.setPlayerName(array.get(0).getAsString());
        item.setRealmName(array.get(1).getAsString());
        item.setBidPrice(new Gold(array.get(2).getAsLong()));
        item.setBuyout(new Gold(array.get(3).getAsLong()));
        item.setCount(array.get(4).getAsInt());
        item.setLastTime(LastTime.valueOf(array.get(5).getAsString()));
        return item;
    }
}
