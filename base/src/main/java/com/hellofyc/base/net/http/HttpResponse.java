package com.hellofyc.base.net.http;

import java.net.HttpCookie;
import java.util.List;

/**
 * Created on 2015/10/9.
 *
 * @author Yucun Fang
 */
public class HttpResponse {

    public static final int STATUS_CODE_OK       = 200;
    public static final int STATUS_CODE_UNKNOWN  = -1;
    public static final int STATUS_CODE_NET      = -2;

    public int code;
    public String text;
    public List<HttpCookie> cookies;

    public boolean isSuccess() {
        return code == STATUS_CODE_OK;
    }

}
