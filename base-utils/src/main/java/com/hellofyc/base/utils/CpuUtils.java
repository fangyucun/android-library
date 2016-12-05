package com.hellofyc.base.utils;

import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.hellofyc.base.utils.ShellUtils.execCommand;

/**
 * Created on 2016/12/5.
 *
 * @author Yucun Fang
 */

public class CpuUtils {

    private static final int MSG_CPU_USAGE       = 1;
    private static final int MSG_CPU_LOAD_AVG    = 2;

    private static CpuUtils sInstace;
    private Executor mThreadpool = Executors.newFixedThreadPool(5);

    public static CpuUtils getInstance() {
        if (sInstace == null) {
            sInstace = new CpuUtils();
        }
        return sInstace;
    }

    private CpuUtils() {
    }

    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CPU_LOAD_AVG: {
                    HandlerResult result = (HandlerResult) msg.obj;
                    OnCpuInfoCallback callback = (OnCpuInfoCallback) result.callback;
                    CpuInfo cpuInfo = (CpuInfo) result.value;
                    callback.onCpuInfoCallback(cpuInfo);
                    break;
                }
                case MSG_CPU_USAGE: {
                    HandlerResult result = (HandlerResult) msg.obj;
                    OnCpuInfoCallback callback = (OnCpuInfoCallback) result.callback;
                    CpuInfo cpuInfo = (CpuInfo) result.value;
                    callback.onCpuInfoCallback(cpuInfo);
                    break;
                }
            }
            return false;
        }

    });

    public void getCurrentCpuUsage(@NonNull OnCpuInfoCallback callback) {
        mThreadpool.execute(new CpuUsageTask(callback));
    }

    public void getCpuLoadAvg(@NonNull OnCpuInfoCallback callback) {
        mThreadpool.execute(new CpuLoadAvgTask(callback));
    }

    class HandlerResult {
        public Object callback;
        public Object value;
    }

    class CpuLoadAvgTask extends BaseThread {

        public CpuLoadAvgTask(OnCpuInfoCallback callback) {
            super(callback);
        }

        @Override
        public void run() {
            ShellUtils.CommandResult result = execCommand("cat /proc/loadavg", false);
            CpuInfo cpuInfo = new CpuInfo();
            if (result.result == 0) {
                String[] values = result.successMsg.trim().split(" ");
                FLog.i(values[0] + " " + values[1] + " " + values[2]);
                cpuInfo.setLoadAvg1(NumberUtils.parseFloat(values[0]));
                cpuInfo.setLoadAvg5(NumberUtils.parseFloat(values[1]));
                cpuInfo.setLoadAvg15(NumberUtils.parseFloat(values[2]));
            }

            Message msg = mHandler.obtainMessage(MSG_CPU_LOAD_AVG);
            HandlerResult handlerResult = new HandlerResult();
            handlerResult.callback = mCallback;
            handlerResult.value = cpuInfo;
            msg.obj = handlerResult;
            mHandler.sendMessage(msg);
        }
    }

    class CpuUsageTask extends BaseThread {

        public CpuUsageTask(OnCpuInfoCallback callback) {
            super(callback);
        }

        @Override
        public void run() {
            ShellUtils.CommandResult result = execCommand("top -m 1 -n 1", false);
            CpuInfo cpuInfo = new CpuInfo();
            if (result.result == 0) {
                String resultValue = result.successMsg.trim().split("\n")[0];
                String values[] = resultValue.split(", ");
                int[] usages = new int[values.length];
                for (int i=0; i<values.length; i++) {
                    usages[i] = NumberUtils.parseInt(values[i].subSequence(values[i].indexOf(' ') + 1, values[i].indexOf('%')).toString());
                }

                cpuInfo.setUserUsage(usages[0]);
                cpuInfo.setSystemUsage(usages[1]);
                cpuInfo.setIowUsage(usages[2]);
                cpuInfo.setIrqUsage(usages[3]);

                int sum = 0;
                for (int i : usages) {
                    sum += i;
                }
                cpuInfo.setTotalUsage(sum);
            }

            Message msg = mHandler.obtainMessage(MSG_CPU_USAGE);
            HandlerResult handlerResult = new HandlerResult();
            handlerResult.callback = mCallback;
            handlerResult.value = cpuInfo;
            msg.obj = handlerResult;
            mHandler.sendMessage(msg);
        }
    }

    abstract class BaseThread implements Runnable {

        protected OnCpuInfoCallback mCallback;

        public BaseThread(OnCpuInfoCallback callback) {
            mCallback = callback;
        }
    }

    public static class CpuInfo implements Parcelable {

        private int totalUsage;
        private int userUsage;
        private int systemUsage;
        private int iowUsage;
        private int irqUsage;

        //1 min loadavg
        private float loadAvg1;

        //5min loadavg
        private float loadAvg5;

        //15min loadavg
        private float loadAvg15;

        public CpuInfo(){}

        protected CpuInfo(Parcel in) {
            totalUsage = in.readInt();
            userUsage = in.readInt();
            systemUsage = in.readInt();
            iowUsage = in.readInt();
            irqUsage = in.readInt();
            loadAvg1 = in.readInt();
            loadAvg5 = in.readInt();
            loadAvg15 = in.readInt();
        }

        public static final Creator<CpuInfo> CREATOR = new Creator<CpuInfo>() {
            @Override
            public CpuInfo createFromParcel(Parcel in) {
                return new CpuInfo(in);
            }

            @Override
            public CpuInfo[] newArray(int size) {
                return new CpuInfo[size];
            }
        };

        public int getTotalUsage() {
            return totalUsage;
        }

        void setTotalUsage(int totalUsage) {
            this.totalUsage = totalUsage;
        }

        public int getUserUsage() {
            return userUsage;
        }

        void setUserUsage(int userUsage) {
            this.userUsage = userUsage;
        }

        public int getSystemUsage() {
            return systemUsage;
        }

        void setSystemUsage(int systemUsage) {
            this.systemUsage = systemUsage;
        }

        public int getIowUsage() {
            return iowUsage;
        }

        void setIowUsage(int iowUsage) {
            this.iowUsage = iowUsage;
        }

        public int getIrqUsage() {
            return irqUsage;
        }

        void setIrqUsage(int irqUsage) {
            this.irqUsage = irqUsage;
        }

        public float getLoadAvg1() {
            return loadAvg1;
        }

        void setLoadAvg1(float loadAvg1) {
            this.loadAvg1 = loadAvg1;
        }

        public float getLoadAvg5() {
            return loadAvg5;
        }

        void setLoadAvg5(float loadAvg5) {
            this.loadAvg5 = loadAvg5;
        }

        public float getLoadAvg15() {
            return loadAvg15;
        }

        void setLoadAvg15(float loadAvg15) {
            this.loadAvg15 = loadAvg15;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(totalUsage);
            dest.writeInt(userUsage);
            dest.writeInt(systemUsage);
            dest.writeInt(iowUsage);
            dest.writeInt(irqUsage);
            dest.writeFloat(loadAvg1);
            dest.writeFloat(loadAvg5);
            dest.writeFloat(loadAvg15);
        }
    }

    public interface OnCpuInfoCallback {
        void onCpuInfoCallback(CpuInfo cpuInfo);
    }

}
