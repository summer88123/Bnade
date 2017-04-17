package com.summer.lib.model.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.support.annotation.WorkerThread;

import com.summer.lib.model.entity.Realm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kevin.bai on 2017/4/16.
 */

public class RealmHelper {
    private static final String data = "[{\"id\":0,\"connected\":\"所有服务器\"},{\"id\":1," +
            "\"connected\":\"万色星辰-奥蕾莉亚-世界之树-布莱恩\"},{\"id\":2,\"connected\":\"丹莫德-克苏恩\"},{\"id\":3," +
            "\"connected\":\"主宰之剑-霍格\"},{\"id\":4,\"connected\":\"丽丽-四川\"},{\"id\":5,\"connected\":\"亚雷戈斯-银松森林\"}," +
            "{\"id\":6,\"connected\":\"亡语者\"},{\"id\":7,\"connected\":\"伊兰尼库斯-阿克蒙德-恐怖图腾\"},{\"id\":8," +
            "\"connected\":\"伊利丹-尘风峡谷\"},{\"id\":9,\"connected\":\"伊森利恩\"},{\"id\":10," +
            "\"connected\":\"伊森德雷-达斯雷玛-库尔提拉斯-雷霆之怒\"},{\"id\":11,\"connected\":\"伊瑟拉-艾森娜-月神殿-轻风之语\"},{\"id\":12," +
            "\"connected\":\"伊莫塔尔-萨尔\"},{\"id\":13,\"connected\":\"伊萨里奥斯-祖阿曼\"},{\"id\":14," +
            "\"connected\":\"元素之力-菲米丝-夏维安\"},{\"id\":15,\"connected\":\"克尔苏加德\"},{\"id\":16," +
            "\"connected\":\"克洛玛古斯-金度\"},{\"id\":17,\"connected\":\"军团要塞-生态船\"},{\"id\":18," +
            "\"connected\":\"冬拥湖-迪托马斯-达基萨斯\"},{\"id\":19,\"connected\":\"冬泉谷-寒冰皇冠\"},{\"id\":20," +
            "\"connected\":\"冰川之拳-双子峰-埃苏雷格-凯尔萨斯\"},{\"id\":21,\"connected\":\"冰霜之刃-安格博达\"},{\"id\":22," +
            "\"connected\":\"冰风岗\"},{\"id\":23,\"connected\":\"凤凰之神-托塞德林\"},{\"id\":24," +
            "\"connected\":\"凯恩血蹄-瑟莱德丝-卡德加\"},{\"id\":25,\"connected\":\"利刃之拳-黑翼之巢\"},{\"id\":26," +
            "\"connected\":\"刺骨利刃-千针石林\"},{\"id\":27,\"connected\":\"加兹鲁维-奥金顿-哈兰\"},{\"id\":28," +
            "\"connected\":\"加基森-黑暗虚空\"},{\"id\":29,\"connected\":\"加尔-黑龙军团\"},{\"id\":30," +
            "\"connected\":\"加里索斯-库德兰\"},{\"id\":31,\"connected\":\"勇士岛-达文格尔-索拉丁\"},{\"id\":32," +
            "\"connected\":\"卡德罗斯-符文图腾-黑暗魅影-阿斯塔洛\"},{\"id\":33,\"connected\":\"卡扎克-爱斯特纳-戈古纳斯-巴纳扎尔\"},{\"id\":34," +
            "\"connected\":\"卡拉赞-苏塔恩\"},{\"id\":35,\"connected\":\"卡珊德拉-暗影之月\"},{\"id\":36," +
            "\"connected\":\"厄祖玛特-奎尔萨拉斯\"},{\"id\":37,\"connected\":\"古加尔-洛丹伦\"},{\"id\":38," +
            "\"connected\":\"古尔丹-血顶\"},{\"id\":39,\"connected\":\"古拉巴什-安戈洛-深渊之喉-德拉诺\"},{\"id\":40," +
            "\"connected\":\"古达克-梅尔加尼\"},{\"id\":41,\"connected\":\"哈卡-诺森德-燃烧军团-死亡熔炉\"},{\"id\":42," +
            "\"connected\":\"嚎风峡湾-闪电之刃\"},{\"id\":43,\"connected\":\"回音山-霜之哀伤-神圣之歌-遗忘海岸\"},{\"id\":44," +
            "\"connected\":\"国王之谷\"},{\"id\":45,\"connected\":\"图拉扬-海达希亚-瓦里玛萨斯-塞纳里奥\"},{\"id\":46," +
            "\"connected\":\"圣火神殿-桑德兰\"},{\"id\":47,\"connected\":\"地狱之石-火焰之树-耐奥祖\"},{\"id\":48," +
            "\"connected\":\"地狱咆哮-阿曼尼-奈法利安\"},{\"id\":49,\"connected\":\"埃克索图斯-血牙魔王\"},{\"id\":50," +
            "\"connected\":\"埃加洛尔-鲜血熔炉-斩魔者\"},{\"id\":51,\"connected\":\"埃基尔松\"},{\"id\":52,\"connected\":\"埃德萨拉\"}," +
            "{\"id\":53,\"connected\":\"埃雷达尔-永恒之井\"},{\"id\":54,\"connected\":\"基尔加丹-奥拉基尔\"},{\"id\":55," +
            "\"connected\":\"基尔罗格-巫妖之王-迦顿\"},{\"id\":56,\"connected\":\"塔纳利斯-巴瑟拉斯-密林游侠\"},{\"id\":57," +
            "\"connected\":\"塞拉摩-暗影迷宫-麦姆\"},{\"id\":58,\"connected\":\"塞拉赞恩-太阳之井\"},{\"id\":59," +
            "\"connected\":\"塞泰克-罗曼斯-黑暗之矛\"},{\"id\":60,\"connected\":\"壁炉谷\"},{\"id\":61," +
            "\"connected\":\"外域-织亡者-阿格拉玛-屠魔山谷\"},{\"id\":62,\"connected\":\"大地之怒-恶魔之魂-希尔瓦娜斯\"},{\"id\":63," +
            "\"connected\":\"大漩涡-风暴之怒\"},{\"id\":64,\"connected\":\"天空之墙\"},{\"id\":65,\"connected\":\"天谴之门\"}," +
            "{\"id\":66,\"connected\":\"夺灵者-战歌-奥斯里安\"},{\"id\":67,\"connected\":\"奈萨里奥-红龙女王-菲拉斯\"},{\"id\":68," +
            "\"connected\":\"奎尔丹纳斯-艾莫莉丝-布鲁塔卢斯\"},{\"id\":69,\"connected\":\"奥妮克希亚-海加尔-纳克萨玛斯\"},{\"id\":70," +
            "\"connected\":\"奥尔加隆\"},{\"id\":71,\"connected\":\"奥杜尔-普瑞斯托-逐日者\"},{\"id\":72,\"connected\":\"奥特兰克\"}," +
            "{\"id\":73,\"connected\":\"奥达曼-甜水绿洲\"},{\"id\":74,\"connected\":\"守护之剑-瑞文戴尔\"},{\"id\":75," +
            "\"connected\":\"安东尼达斯\"},{\"id\":76,\"connected\":\"安其拉-弗塞雷迦-盖斯\"},{\"id\":77," +
            "\"connected\":\"安加萨-莱索恩\"},{\"id\":78,\"connected\":\"安威玛尔-扎拉赞恩\"},{\"id\":79," +
            "\"connected\":\"安纳塞隆-日落沼泽-风暴之鳞-耐普图隆\"},{\"id\":80,\"connected\":\"安苏\"},{\"id\":81," +
            "\"connected\":\"山丘之王-拉文霍德\"},{\"id\":82,\"connected\":\"巨龙之吼-黑石尖塔\"},{\"id\":83," +
            "\"connected\":\"巴尔古恩-托尔巴拉德\"},{\"id\":84,\"connected\":\"布兰卡德\"},{\"id\":85,\"connected\":\"布莱克摩-灰谷\"}," +
            "{\"id\":86,\"connected\":\"希雷诺斯-芬里斯-烈焰荆棘\"},{\"id\":87,\"connected\":\"幽暗沼泽\"},{\"id\":88," +
            "\"connected\":\"影之哀伤\"},{\"id\":89,\"connected\":\"影牙要塞-艾苏恩\"},{\"id\":90,\"connected\":\"恶魔之翼-通灵学院\"}," +
            "{\"id\":91,\"connected\":\"戈提克-雏龙之翼\"},{\"id\":92,\"connected\":\"拉文凯斯-迪瑟洛克\"},{\"id\":93," +
            "\"connected\":\"拉格纳洛斯-龙骨平原\"},{\"id\":94,\"connected\":\"拉贾克斯-荆棘谷\"},{\"id\":95," +
            "\"connected\":\"提尔之手-萨菲隆\"},{\"id\":96,\"connected\":\"提瑞斯法-暗影议会\"},{\"id\":97," +
            "\"connected\":\"摩摩尔-熵魔-暴风祭坛\"},{\"id\":98,\"connected\":\"斯坦索姆-穆戈尔-泰拉尔-格鲁尔\"},{\"id\":99," +
            "\"connected\":\"无尽之海-米奈希尔\"},{\"id\":100,\"connected\":\"无底海渊-阿努巴拉克-刀塔-诺莫瑞根\"},{\"id\":101," +
            "\"connected\":\"时光之穴\"},{\"id\":102,\"connected\":\"普罗德摩-铜龙军团\"},{\"id\":103,\"connected\":\"晴日峰-江苏\"}," +
            "{\"id\":104,\"connected\":\"暗影裂口\"},{\"id\":105,\"connected\":\"暮色森林-杜隆坦-狂风峭壁-玛瑟里顿\"},{\"id\":106," +
            "\"connected\":\"月光林地-麦迪文\"},{\"id\":107,\"connected\":\"末日祷告祭坛-迦罗娜-纳沙塔尔-火羽山\"},{\"id\":108," +
            "\"connected\":\"末日行者\"},{\"id\":109,\"connected\":\"朵丹尼尔-蓝龙军团\"},{\"id\":110," +
            "\"connected\":\"格瑞姆巴托-埃霍恩\"},{\"id\":111,\"connected\":\"格雷迈恩-黑手军团-瓦丝琪\"},{\"id\":112," +
            "\"connected\":\"梦境之树-诺兹多姆-泰兰德\"},{\"id\":113,\"connected\":\"森金-沙怒-血羽\"},{\"id\":114," +
            "\"connected\":\"死亡之翼\"},{\"id\":115,\"connected\":\"毁灭之锤-兰娜瑟尔\"},{\"id\":116," +
            "\"connected\":\"永夜港-翡翠梦境-黄金之路\"},{\"id\":117,\"connected\":\"沃金\"},{\"id\":118," +
            "\"connected\":\"法拉希姆-玛法里奥-麦维影歌\"},{\"id\":119,\"connected\":\"洛肯-海克泰尔\"},{\"id\":120," +
            "\"connected\":\"洛萨-阿卡玛-萨格拉斯\"},{\"id\":121,\"connected\":\"深渊之巢\"},{\"id\":122," +
            "\"connected\":\"激流之傲-红云台地\"},{\"id\":123,\"connected\":\"激流堡-阿古斯\"},{\"id\":124," +
            "\"connected\":\"火喉-雷克萨\"},{\"id\":125,\"connected\":\"火烟之谷-玛诺洛斯-达纳斯\"},{\"id\":126," +
            "\"connected\":\"烈焰峰-瓦拉斯塔兹\"},{\"id\":127,\"connected\":\"熊猫酒仙\"},{\"id\":128," +
            "\"connected\":\"熔火之心-黑锋哨站\"},{\"id\":129,\"connected\":\"燃烧之刃\"},{\"id\":130," +
            "\"connected\":\"燃烧平原-风行者\"},{\"id\":131,\"connected\":\"狂热之刃\"},{\"id\":132," +
            "\"connected\":\"玛多兰-银月-羽月-耳语海岸\"},{\"id\":133,\"connected\":\"玛洛加尔\"},{\"id\":134," +
            "\"connected\":\"玛里苟斯-艾萨拉\"},{\"id\":135,\"connected\":\"瓦拉纳\"},{\"id\":136,\"connected\":\"白银之手\"}," +
            "{\"id\":137,\"connected\":\"白骨荒野-能源舰\"},{\"id\":138,\"connected\":\"石爪峰-阿扎达斯\"},{\"id\":139," +
            "\"connected\":\"石锤-范达尔鹿盔\"},{\"id\":140,\"connected\":\"破碎岭-祖尔金\"},{\"id\":141," +
            "\"connected\":\"祖达克-阿尔萨斯\"},{\"id\":142,\"connected\":\"索瑞森-试炼之环\"},{\"id\":143,\"connected\":\"红龙军团\"}," +
            "{\"id\":144,\"connected\":\"罗宁\"},{\"id\":145,\"connected\":\"自由之风-达隆米尔-艾欧纳尔-冬寒\"},{\"id\":146," +
            "\"connected\":\"艾维娜-艾露恩\"},{\"id\":147,\"connected\":\"范克里夫-血环\"},{\"id\":148,\"connected\":\"萨洛拉丝\"}," +
            "{\"id\":149,\"connected\":\"藏宝海湾-阿拉希-塔伦米尔\"},{\"id\":150,\"connected\":\"蜘蛛王国\"},{\"id\":151," +
            "\"connected\":\"血吼-黑暗之门\"},{\"id\":152,\"connected\":\"血色十字军\"},{\"id\":153,\"connected\":\"贫瘠之地\"}," +
            "{\"id\":154,\"connected\":\"踏梦者-阿比迪斯\"},{\"id\":155,\"connected\":\"辛达苟萨\"},{\"id\":156," +
            "\"connected\":\"达克萨隆-阿纳克洛斯\"},{\"id\":157,\"connected\":\"达尔坎-鹰巢山\"},{\"id\":158," +
            "\"connected\":\"迅捷微风\"},{\"id\":159,\"connected\":\"远古海滩\"},{\"id\":160,\"connected\":\"迦拉克隆\"}," +
            "{\"id\":161,\"connected\":\"迦玛兰-霜狼\"},{\"id\":162,\"connected\":\"金色平原\"},{\"id\":163," +
            "\"connected\":\"阿拉索-阿迦玛甘\"},{\"id\":164,\"connected\":\"雷斧堡垒\"},{\"id\":165,\"connected\":\"雷霆之王\"}," +
            "{\"id\":166,\"connected\":\"雷霆号角-风暴之眼\"},{\"id\":167,\"connected\":\"风暴峭壁\"},{\"id\":168," +
            "\"connected\":\"鬼雾峰\"},{\"id\":169,\"connected\":\"黑铁\"},{\"id\":170,\"connected\":\"斯克提斯\"}]";

    private List<Realm> mRealms;
    private Gson mGson;

    @Inject
    public RealmHelper(Gson gson) {
        this.mGson = gson;
    }

    @WorkerThread
    private List<Realm> getRealms() {
        if (mRealms == null) {
            synchronized (this) {
                if (mRealms == null) {
                    this.mRealms = mGson.fromJson(data, new TypeToken<List<Realm>>() {
                    }.getType());
                }
            }
        }
        return mRealms;
    }

    public Single<List<Realm>> getAll() {
        return Single.defer(new Callable<SingleSource<? extends List<Realm>>>() {
            @Override
            public SingleSource<? extends List<Realm>> call() throws Exception {
                getRealms();
                return Single.just(Collections.unmodifiableList(mRealms));
            }
        }).subscribeOn(Schedulers.io());
    }

    public Single<List<Realm>> getRealmsByName(final String name) {
        return Single.defer(new Callable<SingleSource<List<Realm>>>() {
            @Override
            public SingleSource<List<Realm>> call() throws Exception {
                List<Realm> realms = getRealms();
                List<Realm> result = new ArrayList<>();
                for (Realm realm : realms) {
                    if (realm.getConnected().contains(name)) {
                        result.add(realm);
                    }
                }
                return Single.just(result);
            }
        }).subscribeOn(Schedulers.io());

    }

    public Single<Realm> getRealmById(final long id) {
        return Single.defer(new Callable<SingleSource<? extends Realm>>() {
            @Override
            public SingleSource<? extends Realm> call() throws Exception {
                List<Realm> realms = getRealms();
                for (Realm realm : realms) {
                    if (realm.getId() == id) {
                        return Single.just(realm);
                    }
                }
                return Single.just(Realm.unKnowInstance(id));
            }
        }).subscribeOn(Schedulers.io());
    }
}
