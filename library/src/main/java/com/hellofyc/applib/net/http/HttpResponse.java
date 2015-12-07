package com.hellofyc.applib.net.http;

/**
 * Created on 2015/10/9.
 *
 * @author Yucun Fang
 */
public class HttpResponse {

    public static final int STATUS_CODE_OK       = 200;
    public static final int STATUS_CODE_UNKNOWN  = -1;

    public int code;
    public String text;

    public boolean isSuccess() {
        return code == STATUS_CODE_OK;
    }

}
