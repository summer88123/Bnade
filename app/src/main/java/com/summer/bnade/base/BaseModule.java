package com.summer.bnade.base;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kevin.bai on 2017/5/16.
 */
@Module
public class BaseModule {

    @Singleton
    @Provides
    BaseActivityLifeCycleCallback provideActivityLifecycleCallbacks(BaseFragmentLifecycleCallbacks
                                                                            baseFragmentLifecycleCallbacks) {
        return new BaseActivityLifeCycleCallback(baseFragmentLifecycleCallbacks);
    }

    @Singleton
    @Provides
    BaseFragmentLifecycleCallbacks provideFragmentLifecycleCallbacks() {
        return new BaseFragmentLifecycleCallbacks();
    }
}
