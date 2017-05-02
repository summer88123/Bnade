package com.summer.bnade.token;

import com.github.mikephil.charting.data.Entry;
import com.summer.bnade.base.BasePresenter;
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
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kevin.bai on 2017/4/9.
 */

public class WowTokenPresenter extends BasePresenter<WowTokenContract.View> implements WowTokenContract.Presenter {

    private static final long ONE_DAY = 24 * 3600 * 1000L;
    private Comparator<? super WowTokens> timeComparator = new Comparator<WowTokens>() {
        @Override
        public int compare(WowTokens wowTokens, WowTokens t1) {
            return (int) (wowTokens.getLastModified() - t1.getLastModified());
        }
    };
    private Comparator<? super WowTokens> goldComparator = new Comparator<WowTokens>() {
        @Override
        public int compare(WowTokens wowTokens, WowTokens t1) {
            return wowTokens.getGold() - t1.getGold();
        }
    };
    private long temp = 0;

    @Inject
    WowTokenPresenter(WowTokenContract.View view, BnadeRepo repo) {
        super(view, repo);
    }

    @Override
    public void load() {
        mRepo.getWowTokens()
                .flatMap(new Function<List<WowTokens>, SingleSource<WowTokenVO>>() {
                    @Override
                    public SingleSource<WowTokenVO> apply(@NonNull List<WowTokens> wowTokenses) throws Exception {
                        return Single.zip(getOnedayData(wowTokenses), getSortedAllData(wowTokenses),
                                new BiFunction<List<WowTokens>, List<WowTokens>, WowTokenVO>() {
                                    @Override
                                    public WowTokenVO apply(@NonNull List<WowTokens> oneDay, @NonNull
                                            List<WowTokens> all) throws Exception {
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
                                    }
                                });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        mView.refreshStart();
                    }
                })
                .subscribe(new Consumer<WowTokenVO>() {
                    @Override
                    public void accept(@NonNull WowTokenVO wowTokenVO) throws Exception {
                        mView.refreshOver();
                        mView.showCurrentToken(wowTokenVO);
                        mView.showOneDayChart(wowTokenVO.getOneDayTokens());
                        mView.showHistoryChart(wowTokenVO.getAllTokens());
                    }
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
                .filter(new Predicate<WowTokens>() {
                    @Override
                    public boolean test(@NonNull WowTokens wowTokens) throws Exception {
                        long current = System.currentTimeMillis();
                        return (current - wowTokens.getLastModified()) < ONE_DAY;
                    }
                })
                .toSortedList(goldComparator)
                .subscribeOn(Schedulers.computation());
    }

    private SingleSource<? extends List<WowTokens>> getSortedAllData(List<WowTokens> all) {
        return Observable.fromIterable(all)
                .sorted(timeComparator)
                .filter(new Predicate<WowTokens>() {
                    @Override
                    public boolean test(@NonNull WowTokens wowTokens) throws Exception {
                        if (temp == 0 || wowTokens.getLastModified() > temp + ONE_DAY) {
                            temp = wowTokens.getLastModified();
                            return true;
                        } else {
                            return false;
                        }
                    }
                })
                .toList()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        temp = 0;
                    }
                })
                .subscribeOn(Schedulers.computation());
    }
}
