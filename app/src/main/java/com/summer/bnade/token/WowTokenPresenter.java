package com.summer.bnade.token;

import com.github.mikephil.charting.data.Entry;
import com.summer.bnade.base.mvp.BasePresenter;
import com.summer.bnade.data.BnadeRepo;
import com.summer.bnade.token.entity.WowTokenVO;
import com.summer.lib.model.entity.WowTokens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kevin.bai on 2017/4/9.
 */

public class WowTokenPresenter extends BasePresenter<WowTokenContract.View> implements WowTokenContract.Presenter {

    private static final long ONE_DAY = 24 * 3600 * 1000L;
    private Comparator<? super WowTokens> timeComparator;
    private Comparator<? super WowTokens> goldComparator;
    private long temp = 0;

    @Inject
    WowTokenPresenter(WowTokenContract.View view, BnadeRepo repo) {
        super(view, repo);
        timeComparator = (wowTokens, t1) -> (int) (wowTokens
                .getLastModified() - t1.getLastModified());
        goldComparator = (wowTokens, t1) -> wowTokens
                .getGold() - t1.getGold();
    }

    @Override
    public void load() {
        mRepo.getWowTokens()
                .flatMap(wowTokenses -> Single.zip(getOnedayData(wowTokenses), getSortedAllData(wowTokenses),
                        (oneDay, all) -> {
                            final WowTokenVO token = new WowTokenVO();
                            int last = oneDay.size() - 1;
                            token.setMinGold(oneDay.get(0).getGold());
                            token.setMaxGold(oneDay.get(last).getGold());

                            Collections.sort(oneDay, timeComparator);
                            token.setCurrentGold(oneDay.get(last).getGold());
                            token.setLastModified(oneDay.get(last).getLastModified());

                            token.setOneDayTokens(convert(oneDay));
                            token.setAllTokens(convert(all));
                            return token;
                        }))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mView.refreshStart())
                .subscribe(wowTokenVO -> {
                    mView.refreshOver();
                    mView.showCurrentToken(wowTokenVO);
                    mView.showOneDayChart(wowTokenVO.getOneDayTokens());
                    mView.showHistoryChart(wowTokenVO.getAllTokens());
                }, mErrorHandler);
    }

    private List<Entry> convert(List<WowTokens> list) {

        ArrayList<Entry> yVals = new ArrayList<>();
        for (WowTokens tokens : list) {
            yVals.add(new Entry(tokens.getLastModified(), tokens.getGold()));
        }
        return yVals;
    }

    private Single<List<WowTokens>> getOnedayData(List<WowTokens> all) {
        return Observable.fromIterable(all)
                .filter(wowTokens -> {
                    long current = System.currentTimeMillis();
                    return (current - wowTokens.getLastModified()) < ONE_DAY;
                })
                .toSortedList(goldComparator)
                .subscribeOn(Schedulers.computation());
    }

    private SingleSource<? extends List<WowTokens>> getSortedAllData(List<WowTokens> all) {
        return Observable.fromIterable(all)
                .sorted(timeComparator)
                .filter(wowTokens -> {
                    if (temp == 0 || wowTokens.getLastModified() > temp + ONE_DAY) {
                        temp = wowTokens.getLastModified();
                        return true;
                    } else {
                        return false;
                    }
                })
                .toList()
                .doOnSubscribe(disposable -> temp = 0)
                .subscribeOn(Schedulers.computation());
    }
}
