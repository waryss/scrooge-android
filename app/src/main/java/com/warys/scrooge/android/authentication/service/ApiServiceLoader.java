package com.warys.scrooge.android.authentication.service;


public interface ApiServiceLoader {

    <T> T load(Class<T> service);
}
