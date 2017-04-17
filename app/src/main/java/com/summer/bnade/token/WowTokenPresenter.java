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
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kevin.bai on 2017/4/9.
 */

public class WowTokenPresenter extends BasePresenter<WowTokenContract.View> implements WowTokenContract.Presenter {

    @Inject
    WowTokenPresenter(WowTokenContract.View view, BnadeRepo repo) {
        super(view, repo);
    }

    @Override
    public void load() {
        final long oneDay = 24 * 3600 * 1000L;
        mRepo.getWowTokens()
                .flatMapObservable(new Function<List<WowTokens>, ObservableSource<WowTokens>>() {
                    @Override
                    public ObservableSource<WowTokens> apply(@NonNull List<WowTokens> wowTokenses) throws Exception {
                        return Observable.fromIterable(wowTokenses);
                    }
                })
                .filter(new Predicate<WowTokens>() {
                    @Override
                    public boolean test(@NonNull WowTokens wowTokens) throws Exception {
                        long current = System.currentTimeMillis();
                        return (current - wowTokens.getLastModified()) < oneDay;
                    }
                })
                .toSortedList(new Comparator<WowTokens>() {
                    @Override
                    public int compare(WowTokens wowTokens, WowTokens t1) {
                        return wowTokens.getGold() - t1.getGold();
                    }
                })
                .map(new Function<List<WowTokens>, WowTokenVO>() {
                    @Override
                    public WowTokenVO apply(@NonNull List<WowTokens> wowTokenses) throws Exception {
                        final WowTokenVO token = new WowTokenVO();
                        int last = wowTokenses.size() - 1;
                        token.setMinGold(wowTokenses.get(0).getGold());
                        token.setMaxGold(wowTokenses.get(last).getGold());

                        Collections.sort(wowTokenses, new Comparator<WowTokens>() {
                            @Override
                            public int compare(WowTokens wowTokens, WowTokens t1) {
                                return (int) (wowTokens.getLastModified() - t1.getLastModified());
                            }
                        });
                        token.setCurrentGold(wowTokenses.get(last).getGold());
                        token.setLastModified(wowTokenses.get(last).getLastModified());

                        token.setTokens(convert(wowTokenses));
                        return token;
                    }
                })
                .subscribeOn(Schedulers.io())
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
                        mView.showOneDayChart(wowTokenVO.getTokens());
                    }
                });
    }

    private List<Entry> convert(List<WowTokens> list) {

        ArrayList<Entry> yVals = new ArrayList<>();
        for (WowTokens tokens : list) {
            yVals.add(new Entry(tokens.getLastModified(), tokens.getGold()));
        }
        return yVals;
    }
}
