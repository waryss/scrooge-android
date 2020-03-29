package com.warys.scrooge.android.common.service;


public interface ApiServiceLoader {

    <T> T load(Class<T> service);
}
