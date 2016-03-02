package com.hellofyc.base.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created on 2016/2/16.
 *
 * @author Yucun Fang
 */
public class PageInfo implements Parcelable {

    private int totalPage;
    private int currentPage;
    private long refreshTimestamp;

    public PageInfo(){}

    protected PageInfo(Parcel in) {
        totalPage = in.readInt();
        currentPage = in.readInt();
        refreshTimestamp = in.readLong();
    }

    public static final Creator<PageInfo> CREATOR = new Creator<PageInfo>() {
        @Override
        public PageInfo createFromParcel(Parcel in) {
            return new PageInfo(in);
        }

        @Override
        public PageInfo[] newArray(int size) {
            return new PageInfo[size];
        }
    };

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getRefreshTimestamp() {
        return refreshTimestamp;
    }

    public void setRefreshTimestamp(long refreshTimestamp) {
        this.refreshTimestamp = refreshTimestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(totalPage);
        dest.writeInt(currentPage);
        dest.writeLong(refreshTimestamp);
    }
}
