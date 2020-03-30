package com.warys.scrooge.android.infrastructure.common;

public class StringUtil {

    public static boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public static boolean isPasswordValid(String password) {
        return password.length() > 7 && password.length() < 100;
    }

}
