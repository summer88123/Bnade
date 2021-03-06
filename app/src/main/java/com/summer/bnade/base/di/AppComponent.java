package com.summer.bnade.base.di;

import com.summer.bnade.base.AppModule;
import com.summer.bnade.base.BnadeApplication;
import com.summer.bnade.widget.RealmSelectButton;
import com.summer.lib.model.di.BnadeModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * 处理全局单例的依赖对象
 * Created by kevin.bai on 2017/5/4.
 */
@Singleton
@Component(modules = {
        AppModule.class,
        BnadeModule.class,
        AndroidSupportInjectionModule.class,
        BuilderModule.class,})
public interface AppComponent extends AndroidInjector<BnadeApplication> {

    void inject(BnadeApplication application);

    void inject(RealmSelectButton button);

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<BnadeApplication> {
        public abstract AppComponent build();
    }
}
