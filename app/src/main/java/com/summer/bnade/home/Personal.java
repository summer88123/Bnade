package com.summer.bnade.home;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by kevin.bai on 2017/10/7.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@interface Personal {
}
