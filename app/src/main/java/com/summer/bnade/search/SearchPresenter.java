package com.summer.bnade.search;

import com.summer.bnade.data.BnadeRepo;
import com.summer.bnade.data.HistoryRealmRepo;
import com.summer.bnade.data.HistorySearchRepo;
import com.summer.bnade.data.error.EmptyDataException;
import com.summer.bnade.search.entity.SearchResultVO;
import com.summer.bnade.search.entity.SearchVO;
import com.summer.bnade.utils.RxUtil;
import com.summer.lib.model.entity.Item;
import com.summer.lib.model.entity.Realm;

import java.util.Collections;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;

/**
 * Created by kevin.bai on 2017/4/13.
 */

public class SearchPresenter implements SearchContract.Presenter {
    private final HistorySearchRepo mHistorySearchRepo;
    private final HistoryRealmRepo mRealmRepo;
    private final SearchFragment mView;
    private final BnadeRepo mRepo;
    private RxUtil.BaseErrorHandler mErrorHandler;


    @Inject
    SearchPresenter(SearchFragment view, BnadeRepo repo, HistorySearchRepo historySearchRepo,
                    HistoryRealmRepo realmRepo) {
        this.mView = view;
        this.mRepo = repo;
        this.mHistorySearchRepo = historySearchRepo;
        this.mRealmRepo = realmRepo;
        mErrorHandler = new RxUtil.BaseErrorHandler(mView);
    }

    @Override
    public void load(int hotType) {
        Single.zip(mRepo.getHot(hotType), mHistorySearchRepo.getHistories().toList(), SearchVO::new)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mView::show, mErrorHandler);
    }

    @Override
    public void search(final String name, final Realm realm) {
        mRepo.getItem(name)
                .flatMap(item -> saveHistory(item, realm))
                .flatMap(item -> mRepo.search(item, realm))
                .onErrorResumeNext(throwable -> {
                    if (throwable instanceof EmptyDataException) {
                        return mRepo.getItemNames(name).map(SearchResultVO::new);
                    }
                    return Single.error(throwable);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchResultVO -> {
                    if (searchResultVO.getItem() == null) {
                        mView.showFuzzySearch(searchResultVO.getNames());
                    } else if (searchResultVO.getAuctionRealmItems() != null) {
                        if (searchResultVO.getAuctionRealmItems().isEmpty()) {
                            mView.showToast("无拍卖数据");
                        } else {
                            mView.showRealmItemResult(searchResultVO.getItem(), realm);
                        }
                    } else {
                        mView.showResult(searchResultVO.getItem(), realm);
                    }
                }, mErrorHandler);
    }

    @Override
    public void updateHistory() {
        mHistorySearchRepo.getHistories().toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mView::updateHistories);
    }

    @Override
    public void updateHotSearchType(int type) {
        mRepo.getHot(type).observeOn(AndroidSchedulers.mainThread())
                .subscribe(mView::updateHotSearch, mErrorHandler);
    }

    @Override
    public void clearHistories() {
        mHistorySearchRepo.clear()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> mView.updateHistories(Collections.emptyList()));
    }

    private Single<Item> saveHistory(@NonNull Item item, Realm realm) {
        Single<Item> temp = mHistorySearchRepo.add(item.getName()).andThen(Single.just(item));
        if (realm != null) {
            temp = mRealmRepo.add(realm).andThen(temp);
        }
        return temp;
    }
}
