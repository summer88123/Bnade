package com.summer.bnade.result.all;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kevin.bai on 2017/4/14.
 */
@Module
public class SearchResultModule {

    @Provides
    SearchResultAdapter provideAdapter(SearchResultActivity view) {
        return new SearchResultAdapter(view.item);
    }

}
