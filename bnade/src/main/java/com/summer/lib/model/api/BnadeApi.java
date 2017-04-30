package com.summer.lib.model.api;

import com.google.gson.JsonElement;

import com.summer.lib.model.entity.Auction;
import com.summer.lib.model.entity.AuctionItem;
import com.summer.lib.model.entity.AuctionRealm;
import com.summer.lib.model.entity.AuctionRealmItem;
import com.summer.lib.model.entity.Hot;
import com.summer.lib.model.entity.Item;
import com.summer.lib.model.entity.WowTokens;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by kevin.bai on 2017/4/3.
 */

public interface BnadeApi {

    String BASE_URL = "https://www.bnade.com/wow/";
    String BASE_ICON_URL = "http://content.battlenet.com.cn/wow/icons/56/";

    /**
     * 通过物品名查询物品，不支持模糊
     * @param name 物品名称
     */
    @GET("item/name/{name}")
    Single<List<Item>> getItem(@Path("name") String name);

    /**
     * 每日每周每月搜索次数最多的前10个物品
     */
    @GET("item/hot")
    Single<List<Hot>> getHot();

    /**
     * 模糊查询所有物品名，按物品每周搜索排行排序，返回10条记录
     * @param term 搜索关键字
     */
    @GET("item/names")
    Single<List<String>> getItemNames(@Query("term") String term);

    /**
     * 每天的物品搜索排行
     * @param offset 分页偏移
     * @param limit 限制条数
     */
    @GET("item/hotsearch")
    Observable<List<Hot>> getHotSearch(@Query("offset") int offset, @Query("limit") int limit);

    /**
     * 物品在所有服务器的最低一口价
     * @param id 物品ID
     * @param bl 物品奖励
     * @return
     */
    @GET("auction/item/{id}")
    Single<List<AuctionItem>> getAuctionItem(@Path("id") long id, @Query("bl") int bl);

    @GET("auction/item/{id}")
    Single<List<AuctionItem>> getAuctionItem(@Path("id") long id);

    /**
     * 物品在指定服务器2天内的所有最低一口价的历史数据,
     * 结果数组说明: arr[0] - 最低一口价(单位:铜) arr[1] - 总数量 arr[2] - 数据更新时间(单位:arr[0]
     * @param realmId
     * @param itemId
     */
    @GET("auction/past/realm/{realmId}/item/{itemId}")
    Single<List<JsonElement>> getAuctionPastRealmItem(@Path("realmId") long realmId, @Path("itemId") long itemId);

    /**
     * 物品在指定服务器1年内的所有最低一口价的历史数据,
     * 结果数组说明: arr[0] - 最低一口价(单位:铜) arr[1] - 总数量 arr[2] - 数据更新时间(单位:arr[0]
     * @param realmId
     * @param itemId
     */
    @GET("auction/history/realm/{realmId}/item/{itemId}")
    Single<List<JsonElement>> getAuctionHistoryRealmItem(@Path("realmId") long realmId, @Path("itemId") long itemId);

    /**
     * 所有服务器拍卖总数据
     * @return
     */
    @GET("auction/realms/summary")
    Single<List<AuctionRealm>> getAuctionRealmsSummary();

    @GET("auction/realm/{realmId}/owner/{name}")
    Single<List<Auction>> getAuctionRealmOwner(@Path("realmId") long realmId, @Path("name") String name);

    /**
     * 查询某个服务器某个物品的拍卖行的所有数据
     * @param realmId
     * @param itemId
     * @return
     */
    @GET("auction/realm/{realmId}/item/{itemId}")
    Single<List<AuctionRealmItem>> getAuctionRealmItem(@Path("realmId") long realmId, @Path("itemId") long itemId);

    @GET("ah/wowtokens")
    Single<List<WowTokens>> getAhWowtokens();

}
