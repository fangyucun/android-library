/*
 *  Copyright (C) 2012-2015 Jason Fang ( ifangyucun@gmail.com )
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.hellofyc.base.content;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

/**
 * Create on 2015年4月17日 上午10:47:02
 *
 * @author Yucun Fang
 */
@SuppressLint("ParcelCreator")
public class IntentWrapper extends Intent {

    private Intent mIntent;

    private IntentWrapper() {}

    public static IntentWrapper newInstance() {
        return new IntentWrapper();
    }

    public IntentWrapper set(Intent intent) {
        mIntent = intent;
        return this;
    }

    @Override
    public <T extends Parcelable> T getParcelableExtra(String name) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getParcelableExtra(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getIntExtra(@NonNull String name, int defValue) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getIntExtra(name, defValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    @Override
    public int[] getIntArrayExtra(@NonNull String name) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getIntArrayExtra(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Integer> getIntegerArrayListExtra(@NonNull String name) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getIntegerArrayListExtra(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getLongExtra(@NonNull String name, long defValue) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getLongExtra(name, defValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    @Override
    public long[] getLongArrayExtra(@NonNull String name) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getLongArrayExtra(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getStringExtra(@NonNull String name) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getStringExtra(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public ArrayList<String> getStringArrayListExtra(@NonNull String name) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getStringArrayListExtra(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String[] getStringArrayExtra(@NonNull String name) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getStringArrayExtra(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean getBooleanExtra(@NonNull String name, boolean defaultValue) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getBooleanExtra(name, defaultValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    @Override
    public boolean[] getBooleanArrayExtra(@NonNull String name) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getBooleanArrayExtra(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public float getFloatExtra(@NonNull String name, float defaultValue) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getFloatExtra(name, defaultValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    @Override
    public float[] getFloatArrayExtra(@NonNull String name) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getFloatArrayExtra(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public double getDoubleExtra(@NonNull String name, double defaultValue) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getDoubleExtra(name, defaultValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    @Override
    public double[] getDoubleArrayExtra(@NonNull String name) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getDoubleArrayExtra(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Bundle getBundleExtra(@NonNull String name) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getBundleExtra(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte getByteExtra(@NonNull String name, byte defaultValue) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getByteExtra(name, defaultValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    @Override
    public byte[] getByteArrayExtra(@NonNull String name) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getByteArrayExtra(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public char getCharExtra(@NonNull String name, char defaultValue) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getCharExtra(name, defaultValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    @Override
    public char[] getCharArrayExtra(@NonNull String name) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getCharArrayExtra(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CharSequence getCharSequenceExtra(@NonNull String name) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getCharSequenceExtra(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CharSequence[] getCharSequenceArrayExtra(@NonNull String name) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getCharSequenceArrayExtra(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<CharSequence> getCharSequenceArrayListExtra(@NonNull String name) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getCharSequenceArrayListExtra(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public short getShortExtra(String name, short defaultValue) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getShortExtra(name, defaultValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    @Override
    public Parcelable[] getParcelableArrayExtra(String name) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getParcelableArrayExtra(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T extends Parcelable> ArrayList<T> getParcelableArrayListExtra(String name) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getParcelableArrayListExtra(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Serializable getSerializableExtra(String name) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getSerializableExtra(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public short[] getShortArrayExtra(String name) {
        try {
            if (mIntent.hasExtra(name)) {
                return mIntent.getShortArrayExtra(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Bundle getExtras() {
        return mIntent.getExtras();
    }

    @Override
    public int getFlags() {
        return mIntent.getFlags();
    }

    @Override
    public String getPackage() {
        return mIntent.getPackage();
    }

    @Override
    public ComponentName getComponent() {
        return mIntent.getComponent();
    }

    @Override
    public Rect getSourceBounds() {
        return mIntent.getSourceBounds();
    }

    @Override
    public Intent putCharSequenceArrayListExtra(String name, ArrayList<CharSequence> value) {
        return mIntent.putCharSequenceArrayListExtra(name, value);
    }

    @Override
    public Intent putExtra(String name, boolean value) {
        return mIntent.putExtra(name, value);
    }

    @Override
    public Intent putExtra(String name, byte value) {
        return mIntent.putExtra(name, value);
    }

    @Override
    public Intent putExtra(String name, char value) {
        return mIntent.putExtra(name, value);
    }

    @Override
    public Intent putExtra(String name, short value) {
        return mIntent.putExtra(name, value);
    }

    @Override
    public Intent putExtra(String name, int value) {
        return mIntent.putExtra(name, value);
    }

    @Override
    public Intent putExtra(String name, long value) {
        return mIntent.putExtra(name, value);
    }

    @Override
    public Intent putExtra(String name, float value) {
        return mIntent.putExtra(name, value);
    }

    @Override
    public Intent putExtra(String name, double value) {
        return mIntent.putExtra(name, value);
    }

    @Override
    public Intent putExtra(String name, String value) {
        return mIntent.putExtra(name, value);
    }

    @Override
    public Intent putExtra(String name, CharSequence value) {
        return mIntent.putExtra(name, value);
    }

    @Override
    public Intent putExtra(String name, Parcelable value) {
        return mIntent.putExtra(name, value);
    }

    @Override
    public Intent putExtra(String name, Parcelable[] value) {
        return mIntent.putExtra(name, value);
    }

    @Override
    public Intent putExtra(String name, Serializable value) {
        return mIntent.putExtra(name, value);
    }

    @Override
    public Intent putExtra(String name, boolean[] value) {
        return mIntent.putExtra(name, value);
    }

    @Override
    public Intent putExtra(String name, byte[] value) {
        return mIntent.putExtra(name, value);
    }

    @Override
    public Intent putExtras(Bundle extras) {
        return mIntent.putExtras(extras);
    }

    @Override
    public Intent putParcelableArrayListExtra(String name, ArrayList<? extends Parcelable> value) {
        return mIntent.putParcelableArrayListExtra(name, value);
    }

    @Override
    public Intent putIntegerArrayListExtra(String name, ArrayList<Integer> value) {
        return mIntent.putIntegerArrayListExtra(name, value);
    }

    @Override
    public Intent putStringArrayListExtra(String name, ArrayList<String> value) {
        return mIntent.putStringArrayListExtra(name, value);
    }

    @Override
    public Intent putExtra(String name, short[] value) {
        return mIntent.putExtra(name, value);
    }

    @Override
    public Intent putExtra(String name, char[] value) {
        return mIntent.putExtra(name, value);
    }

    @Override
    public Intent putExtra(String name, int[] value) {
        return mIntent.putExtra(name, value);
    }

    @Override
    public Intent putExtra(String name, long[] value) {
        return mIntent.putExtra(name, value);
    }

    @Override
    public Intent putExtra(String name, float[] value) {
        return mIntent.putExtra(name, value);
    }

    @Override
    public Intent putExtra(String name, double[] value) {
        return mIntent.putExtra(name, value);
    }

    @Override
    public Intent putExtra(String name, String[] value) {
        return mIntent.putExtra(name, value);
    }

    @Override
    public Intent putExtra(String name, CharSequence[] value) {
        return mIntent.putExtra(name, value);
    }

    @Override
    public Intent putExtra(String name, Bundle value) {
        return mIntent.putExtra(name, value);
    }

    @Override
    public Intent putExtras(Intent src) {
        return mIntent.putExtras(src);
    }

    @Override
    public Intent replaceExtras(Intent src) {
        return mIntent.replaceExtras(src);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public Intent setTypeAndNormalize(String type) {
        return mIntent.setTypeAndNormalize(type);
    }

    @Override
    public Intent setType(String type) {
        return mIntent.setType(type);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public Intent setDataAndNormalize(Uri data) {
        return mIntent.setDataAndNormalize(data);
    }

    @Override
    public Intent setData(Uri data) {
        return mIntent.setData(data);
    }

    @Override
    public Intent setAction(String action) {
        return mIntent.setAction(action);
    }

    @Override
    public ActivityInfo resolveActivityInfo(PackageManager pm, int flags) {
        return mIntent.resolveActivityInfo(pm, flags);
    }

    @Override
    public ComponentName resolveActivity(PackageManager pm) {
        return mIntent.resolveActivity(pm);
    }

    @Override
    public void setExtrasClassLoader(ClassLoader loader) {
        mIntent.setExtrasClassLoader(loader);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public ClipData getClipData() {
        return mIntent.getClipData();
    }

    @Override
    public Intent getSelector() {
        return mIntent.getSelector();
    }

    @Override
    public Set<String> getCategories() {
        return mIntent.getCategories();
    }

    @Override
    public boolean hasCategory(String category) {
        return mIntent.hasCategory(category);
    }

    @Override
    public String resolveTypeIfNeeded(ContentResolver resolver) {
        return mIntent.resolveTypeIfNeeded(resolver);
    }

    @Override
    public String resolveType(ContentResolver resolver) {
        return mIntent.resolveType(resolver);
    }

    @Override
    public String resolveType(Context context) {
        return mIntent.resolveType(context);
    }

    @Override
    public String getType() {
        return mIntent.getType();
    }

    @Override
    public String getScheme() {
        return mIntent.getScheme();
    }

    @Override
    public String getDataString() {
        return mIntent.getDataString();
    }

    @Override
    public Uri getData() {
        return mIntent.getData();
    }

    @Override
    public String getAction() {
        return mIntent.getAction();
    }

    @Override
    public Intent cloneFilter() {
        return mIntent.cloneFilter();
    }

    @Override
    public boolean hasExtra(String name) {
        return mIntent.hasExtra(name);
    }

    @Override
    public boolean hasFileDescriptors() {
        return mIntent.hasFileDescriptors();
    }

    @Override
    public Intent setDataAndType(Uri data, String type) {
        return mIntent.setDataAndType(data, type);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public Intent setDataAndTypeAndNormalize(Uri data, String type) {
        return mIntent.setDataAndTypeAndNormalize(data, type);
    }

    @Override
    public Intent addCategory(String category) {
        return mIntent.addCategory(category);
    }

    @Override
    public void removeCategory(String category) {
        mIntent.removeCategory(category);
    }

    @Override
    public void setSelector(Intent selector) {
        mIntent.setSelector(selector);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void setClipData(ClipData clip) {
        mIntent.setClipData(clip);
    }

    @Override
    public Intent replaceExtras(Bundle extras) {
        return mIntent.replaceExtras(extras);
    }

    @Override
    public void removeExtra(String name) {
        mIntent.removeExtra(name);
    }

    @Override
    public Intent setFlags(int flags) {
        return mIntent.setFlags(flags);
    }

    @Override
    public Intent addFlags(int flags) {
        return mIntent.addFlags(flags);
    }

    @Override
    public Intent setPackage(String packageName) {
        return mIntent.setPackage(packageName);
    }

    @Override
    public Intent setComponent(ComponentName component) {
        return mIntent.setComponent(component);
    }

    @Override
    public Intent setClassName(Context packageContext, String className) {
        return mIntent.setClassName(packageContext, className);
    }

    @Override
    public Intent setClassName(String packageName, String className) {
        return mIntent.setClassName(packageName, className);
    }

    @Override
    public Intent setClass(Context packageContext, Class<?> cls) {
        return mIntent.setClass(packageContext, cls);
    }

    @Override
    public void setSourceBounds(Rect r) {
        mIntent.setSourceBounds(r);
    }

    @Override
    public int fillIn(Intent other, int flags) {
        return mIntent.fillIn(other, flags);
    }

    @Override
    public boolean filterEquals(Intent other) {
        return mIntent.filterEquals(other);
    }

    @Override
    public int filterHashCode() {
        return mIntent.filterHashCode();
    }

    @Override
    public String toString() {
        return mIntent.toString();
    }

    @Override
    public String toUri(int flags) {
        return mIntent.toUri(flags);
    }

    @Override
    public int describeContents() {
        return mIntent.describeContents();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        mIntent.writeToParcel(out, flags);
    }

    @Override
    public void readFromParcel(Parcel in) {
        mIntent.readFromParcel(in);
    }
}
