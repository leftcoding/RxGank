package com.leftcoding.network.builder;

/**
 * Create by LingYan on 2019-05-07
 */
public interface Api {
    <T> T create(Class<T> t);
}
