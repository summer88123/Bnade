package com.summer.bnade.token;

import com.github.mikephil.charting.data.Entry;
import com.summer.bnade.data.BnadeRepo;
import com.summer.bnade.token.entity.WowTokenUIModel;
import com.summer.bnade.utils.ChartHelper;
import com.summer.lib.model.entity.WowTokens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin.bai on 2017/4/9.
 */

public class WowTokenTransformer {

    private static final long ONE_DAY = 24 * 3600 * 1000L;
    private final BnadeRepo mRepo;
    private Comparator<? super WowTokens> timeComparator;
    private Comparator<? super WowTokens> goldComparator;

    @Inject
    WowTokenTransformer(BnadeRepo repo) {
        this.mRepo = repo;
        timeComparator = (wowTokens, t1) -> (int) (wowTokens
                .getLastModified() - t1.getLastModified());
        goldComparator = (wowTokens, t1) -> wowTokens
                .getGold() - t1.getGold();
    }

    public ObservableTransformer<Object, WowTokenUIModel> load() {
        return observable -> observable
                .flatMap(o -> mRepo.getWowTokens()
                        .map(list -> {
                            WowTokenUIModel token = WowTokenUIModel.success();
                            List<WowTokens> oneDay = new ArrayList<>();
                            List<WowTokens> all = new ArrayList<>();
                            long temp = 0;
                            long current = System.currentTimeMillis();
                            for (WowTokens wowTokens : list) {
                                if (temp == 0 || wowTokens.getLastModified() > temp + ONE_DAY) {
                                    temp = wowTokens.getLastModified();
                                    all.add(wowTokens);
                                }
                                if ((current - wowTokens.getLastModified()) < ONE_DAY) {
                                    oneDay.add(wowTokens);
                                }
                            }
                            Collections.sort(oneDay, goldComparator);
                            int last = oneDay.size() - 1;
                            token.setMinGold(oneDay.get(0).getGold());
                            token.setMaxGold(oneDay.get(last).getGold());

                            Collections.sort(oneDay, timeComparator);
                            token.setCurrentGold(oneDay.get(last).getGold());
                            token.setLastModified(oneDay.get(last).getLastModified());

                            token.setOneDayTokens(ChartHelper
                                    .generateData(oneDay, (index, tokens) -> new Entry(tokens.getLastModified(), tokens
                                            .getGold())));

                            Collections.sort(list, timeComparator);

                            token.setAllTokens(ChartHelper
                                    .generateData(all, (index, tokens) -> new Entry(tokens.getLastModified(), tokens
                                            .getGold())));
                            return token;
                        })
                        .toObservable()
                        .onErrorReturn(e -> WowTokenUIModel.failure(e.getMessage()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .startWith(WowTokenUIModel.inProgress()));
    }
}
