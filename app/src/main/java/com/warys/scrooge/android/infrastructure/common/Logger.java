package com.warys.scrooge.android.infrastructure.common;

import android.util.Log;

public class Logger {

    private final Class<?> clazz;

    public Logger(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void error(String msg) {
        Log.e(clazz.getName(), msg);
    }

    public void error(Throwable throwable) {
        Log.e(clazz.getName(), "An error", throwable);
    }
}
