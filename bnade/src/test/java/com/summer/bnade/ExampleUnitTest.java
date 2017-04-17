package com.summer.bnade;

import com.summer.lib.model.api.BnadeApi;
import com.summer.lib.model.di.ApplicationModule;
import com.summer.lib.model.di.BnadeApiModule;
import com.summer.lib.model.di.DaggerApplicationComponent;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void DItest(){

    }


    @Test
    public void addition_isCorrect() throws Exception {
        BnadeApi api =
        DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(null))
                .bnadeApiModule(new BnadeApiModule())
                .build().bnadeApi();

    }
}