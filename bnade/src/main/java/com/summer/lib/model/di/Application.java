package com.summer.lib.model.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by kevin.bai on 2017/10/2.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface Application {
}
