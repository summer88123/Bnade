package com.summer.lib.model.di;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by kevin.bai on 2017/4/4.
 */
@Scope
@Documented
@Retention(RUNTIME)
public @interface PreActivity {
}
