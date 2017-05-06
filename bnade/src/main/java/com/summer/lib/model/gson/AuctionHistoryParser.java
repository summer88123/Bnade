package com.summer.lib.model.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import com.summer.lib.model.entity.AuctionHistory;
import com.summer.lib.model.entity.Gold;

import java.lang.reflect.Type;

/**
 * Created by kevin.bai on 2017/4/4.
 */

public class AuctionHistoryParser implements JsonDeserializer<AuctionHistory> {
    @Override
    public AuctionHistory deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws
            JsonParseException {
        JsonArray array = json.getAsJsonArray();
        AuctionHistory item = new AuctionHistory();
        item.setMinBuyout(new Gold(array.get(0).getAsLong()));
        item.setCount(array.get(1).getAsInt());
        item.setLastModifited(array.get(2).getAsLong());
        return item;
    }

    public static AuctionHistoryParser create() {
        return new AuctionHistoryParser();
    }
}
