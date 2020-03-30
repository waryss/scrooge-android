package com.warys.scrooge.android.infrastructure.service;


public interface ApiServiceLoader {

    <T> T load(Class<T> service);
}
