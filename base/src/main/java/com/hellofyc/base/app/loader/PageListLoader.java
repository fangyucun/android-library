package com.hellofyc.base.app.loader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hellofyc.base.loader.TaskLoader;
import com.hellofyc.base.model.PageInfo;
import com.hellofyc.base.net.http.HttpResponse;
import com.hellofyc.base.util.FLog;

import java.util.List;

/**
 * Created on 2016/2/16.
 *
 * @author Yucun Fang
 */
public abstract class PageListLoader<D> extends TaskLoader<PageListResult<D>> {

    private String mCurrentPageString = "current_page";
    private String mTotalPageString = "total_page";
    private PageInfo mPageInfo;

    protected PageListLoader(Context context, Bundle args) {
        super(context);
        mPageInfo = args.getParcelable("page_info");
    }

    public abstract HttpResponse onLoadData(ArrayMap<String, String> params);
    public abstract List<D> onResultFormat(JSONArray list);

    @Override
    public PageListResult<D> loadInBackground() {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("page", String.valueOf(mPageInfo.getCurrentPage() + 1));
        params.put("timestamp", String.valueOf(mPageInfo.getRefreshTimestamp()));
        return handlePageInfo(onLoadData(params));
    }

    private PageListResult<D> handlePageInfo(HttpResponse response) {
        FLog.i("http response:" + response.text);

        PageListResult<D> result = new PageListResult<>();
        if (response.isSuccess()) {
            try {
                JSONObject jsonObject = JSON.parseObject(response.text);
                if (jsonObject != null) {
                    int status = jsonObject.getIntValue("status");
                    if (status == 200) {
                        JSONObject pageObject = jsonObject.getJSONObject("page");
                        if (pageObject != null) {
                            mPageInfo.setCurrentPage(pageObject.getIntValue(mCurrentPageString));
                            mPageInfo.setTotalPage(pageObject.getIntValue(mTotalPageString));
                            if (pageObject.getIntValue(mCurrentPageString) <= 1) {
                                mPageInfo.setRefreshTimestamp(pageObject.getLongValue("timestamp"));
                            }
                            result.setPageInfo(mPageInfo);
                        }
                        result.setValue(onResultFormat(jsonObject.getJSONArray("list")));
                        result.setCode(ResultCode.SUCCESS);
                        result.setText(response.text);
                        return result;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.setCode(ResultCode.JSON);
            result.setText(ResultCode.getCodeString(ResultCode.JSON));
            return result;
        } else {
            result.setCode(response.code);
            result.setText(ResultCode.getCodeString(ResultCode.NET));
            return result;
        }
    }

}
