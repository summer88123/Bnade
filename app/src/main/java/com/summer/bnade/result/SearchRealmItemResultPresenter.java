package com.summer.bnade.result;

import com.summer.bnade.base.BasePresenter;
import com.summer.bnade.data.BnadeRepo;
import com.summer.bnade.search.entity.SearchResultVO;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by kevin.bai on 2017/4/28.
 */

public class SearchRealmItemResultPresenter extends BasePresenter<SearchResultContract.View> implements
        SearchResultContract.Presenter {

    SearchResultVO mResultVO;

    @Inject
    public SearchRealmItemResultPresenter(SearchResultContract.View view, BnadeRepo repo) {
        super(view, repo);
    }

    @Override
    public void filter(String query) {

    }

    @Override
    public void setData(SearchResultVO data) {
        this.mResultVO = data;
    }

    @Override
    public void load() {
        Single.just(mResultVO)
                .subscribe(new Consumer<SearchResultVO>() {
                    @Override
                    public void accept(@NonNull SearchResultVO searchResultVO) throws Exception {
                        mView.show(searchResultVO);
                    }
                });
    }
}
