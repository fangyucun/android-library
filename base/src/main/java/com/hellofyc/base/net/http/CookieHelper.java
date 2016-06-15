package com.hellofyc.base.net.http;

import java.net.HttpCookie;
import java.util.List;

/**
 * Created on 2016/6/14.
 *
 * @author Yucun Fang
 */

public class CookieHelper {

    public static String parse(List<HttpCookie> cookies) {
        if (cookies == null || cookies.size() == 0) return "";

        StringBuilder cookieString = new StringBuilder();
        int size = cookies.size();
        for (HttpCookie cookie : cookies) {
            size--;
            cookieString.append(cookie.toString());
            if (size != 0) {
                cookieString.append(";");
            }
        }
        return cookieString.toString();
    }

}
