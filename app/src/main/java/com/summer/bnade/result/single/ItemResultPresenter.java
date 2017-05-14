package com.summer.bnade.result.single;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.summer.bnade.base.BasePresenter;
import com.summer.bnade.data.BnadeRepo;
import com.summer.bnade.search.entity.SearchResultVO;
import com.summer.bnade.utils.ChartHelper;
import com.summer.lib.model.entity.AuctionHistory;
import com.summer.lib.model.entity.AuctionRealmItem;
import com.summer.lib.model.entity.Item;
import com.summer.lib.model.entity.Realm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kevin.bai on 2017/4/28.
 */

class ItemResultPresenter extends BasePresenter<ItemResultContract.View> implements
        ItemResultContract.Presenter {

    private Function<SearchResultVO, SearchResultVO> polySameRealmItem = new Function<SearchResultVO, SearchResultVO>
            () {
        @Override
        public SearchResultVO apply(@NonNull SearchResultVO resultVO) throws Exception {
            List<AuctionRealmItem> realmItems = resultVO.getAuctionRealmItems();
            List<AuctionRealmItem> result = new ArrayList<>();
            for (AuctionRealmItem realmItem : realmItems) {
                int index = result.indexOf(realmItem);
                if (index != -1) {
                    result.get(index).add(realmItem);
                } else {
                    result.add(realmItem);
                }
            }

            Collections.sort(result, new Comparator<AuctionRealmItem>() {
                @Override
                public int compare(AuctionRealmItem o1, AuctionRealmItem o2) {
                    long result = o1.getUnitBuyout().getMoney() - o2.getUnitBuyout().getMoney();
                    if (result == 0) {
                        result = o1.getUnitBidPrice().getMoney() - o2.getUnitBidPrice().getMoney();
                    }
                    if (result > 0) return 1;
                    else return result == 0 ? 0 : -1;
                }
            });
            resultVO.setAuctionRealmItems(result);
            return resultVO;
        }
    };

    @Inject
    ItemResultPresenter(ItemResultContract.View view, BnadeRepo repo) {
        super(view, repo);
    }

    @Override
    public void load(Item item, Realm realm) {
        mRepo.search(item, realm)
                .map(new Function<SearchResultVO, SearchResultVO>() {
                    @Override
                    public SearchResultVO apply(@NonNull SearchResultVO resultVO) throws Exception {
                        CombinedData data = new CombinedData();

                        data.setData(ChartHelper.generateLineData(resultVO
                                .getAuctionHistories(), new BiFunction<Integer, AuctionHistory, Entry>() {
                            @Override
                            public Entry apply(Integer index, @NonNull AuctionHistory auctionItem) throws
                                    Exception {
                                return new Entry(index, auctionItem.getMinBuyout().getMoney(), auctionItem);
                            }
                        }));
                        data.setData(ChartHelper.generateBarData(resultVO
                                .getAuctionHistories(), new BiFunction<Integer, AuctionHistory, BarEntry>() {
                            @Override
                            public BarEntry apply(@NonNull Integer integer, @NonNull AuctionHistory auctionItem)
                                    throws Exception {
                                return new BarEntry(integer, auctionItem.getCount(), auctionItem);
                            }
                        }));
                        resultVO.setCombinedData(data);
                        return resultVO;
                    }
                })
                .map(polySameRealmItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SearchResultVO>() {
                    @Override
                    public void accept(@NonNull SearchResultVO searchResultVO) throws Exception {
                        mView.show(searchResultVO);
                    }
                }, mErrorHandler);
    }
}
