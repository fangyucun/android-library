package com.hellofyc.base.app.loader;

import com.hellofyc.base.loader.LoaderResult;
import com.hellofyc.base.model.PageInfo;

import java.util.List;

/**
 * Created on 2016/2/16.
 *
 * @author Yucun Fang
 */
public class PageListResult<D> extends LoaderResult<List<D>> {

    private PageInfo pageInfo;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}
