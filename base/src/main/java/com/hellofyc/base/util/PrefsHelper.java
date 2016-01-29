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

package com.hellofyc.base.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.text.TextUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * SharePreferences Tool
 *
 * Create on 2014年12月5日 下午5:28:22
 *
 * @author Jason Fang
 */
public final class PrefsHelper {

	private Context mContext;
    private boolean mIsKeyEncrypt = false;
    private boolean mIsValueEncrypt = false;
	private String mFileName;
    private int mMode = Context.MODE_PRIVATE;
    private String mEncryptKey = "iJasonFang";
    private String mKey;
    private Object mValue;

	private PrefsHelper(Context context) {
		mContext = context;
	}

	public static PrefsHelper create(Context context) {
		return new PrefsHelper(context);
	}

	public PrefsHelper keyEncrypt() {
		mIsKeyEncrypt = true;
		return this;
	}

	public PrefsHelper valueEncrypt() {
		mIsValueEncrypt = true;
		return this;
	}

    public PrefsHelper setFileName(String name) {
        mFileName = name;
        return this;
    }

    public PrefsHelper setEncryptKey(@Size(min=8) String key) {
        mEncryptKey = key;
        return this;
    }

    /**
     *
     * @param mode
     * {@link Context#MODE_PRIVATE}
     * {@link Context#MODE_APPEND}
     * {@link Context#MODE_ENABLE_WRITE_AHEAD_LOGGING}
     * @return this
     */
    public PrefsHelper setMode(int mode) {
        mMode = mode;
        return this;
    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        getSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener);
    }

    public PrefsHelper putValue(@NonNull String key, @NonNull Object value) {
        mKey = key;
        mValue = value;
        return this;
	}

    @SuppressWarnings("unchecked")
    private void putValue(Editor editor, String key, Object value) {
        key = getEncodeKey(key);
        if (mIsValueEncrypt) {
            if (!(value instanceof Set)) {
                editor.putString(key, getEncryptValue(String.valueOf(value)));
            } else {
                Set<String> sets = (Set<String>)value;
                Set<String> tempSets = new HashSet<>();
                for (String s : sets) {
                    tempSets.add(getEncryptValue(String.valueOf(s)));
                }
                editor.putStringSet(key, tempSets);
            }
        } else {
            if (value instanceof Boolean) {
                editor.putBoolean(key, Boolean.parseBoolean(String.valueOf(value)));
            } else if (value instanceof Float) {
                editor.putFloat(key, Float.parseFloat(String.valueOf(value)));
            } else if (value instanceof Integer) {
                editor.putInt(key, Integer.parseInt(String.valueOf(value)));
            } else if (value instanceof Long) {
                editor.putLong(key, Long.parseLong(String.valueOf(value)));
            } else if (value instanceof String) {
                editor.putString(key, String.valueOf(value));
            } else if (value instanceof Set) {
                editor.putStringSet(key, (Set<String>)value);
            } else {
                throw new IllegalArgumentException("Value type is not support!");
            }
        }
    }

	public boolean commit() {
        Editor editor = getSharedPreferences().edit();
        putValue(editor, mKey, mValue);
		return editor.commit();
	}

	public void apply() {
        Editor editor = getSharedPreferences().edit();
        putValue(editor, mKey, mValue);
        editor.apply();
	}

    public boolean contain(String key) {
        return getSharedPreferences().contains(getEncodeKey(key));
    }

    public void clear() {
        getSharedPreferences().edit().clear().apply();
    }

	public void removeKey(String key) {
		getSharedPreferences().edit().remove(getEncodeKey(key)).apply();
	}

    public Map<String, ?> getAll() {
        return getSharedPreferences().getAll();
    }

	public String getString(String key, String defValue) {
		String value = getSharedPreferences().getString(getEncodeKey(key), defValue);
		if (value.equals(defValue)) {
			return value;
		} else {
			return getDecryptValue(value, defValue);
		}
	}

	public int getInt(String key, int defValue) {
		if (mIsValueEncrypt) {
			String value = getString(key, String.valueOf(defValue));
			try {
				return Integer.valueOf(value);
			} catch (Exception e) {
				return defValue;
			}
		} else {
			return getSharedPreferences().getInt(getEncodeKey(key), defValue);
		}
	}

	public long getLong(String key, long defValue) {
		if (mIsValueEncrypt) {
			String value = getString(key, String.valueOf(defValue));
			try {
				return Long.valueOf(value);
			} catch (Exception e) {
				return defValue;
			}
		} else {
			return getSharedPreferences().getLong(getEncodeKey(key), defValue);
		}
	}

	public float getFloat(String key, float defValue) {
		if (mIsValueEncrypt) {
			String value = getString(key, String.valueOf(defValue));
			try {
				return Float.valueOf(value);
			} catch (Exception e) {
				return defValue;
			}
		} else {
			return getSharedPreferences().getFloat(getEncodeKey(key), defValue);
		}

	}

	public boolean getBoolean(String key, boolean defValue) {
		if (mIsValueEncrypt) {
			String valueString = getString(key, String.valueOf(defValue));
			try {
				return Boolean.valueOf(valueString);
			} catch (Exception e) {
				return defValue;
			}
		} else {
			return getSharedPreferences().getBoolean(getEncodeKey(key), defValue);
		}
	}

	public Set<String> getStringSet(String key, Set<String> defValues) {
		Set<String> valueSet = getSharedPreferences().getStringSet(getEncodeKey(key), defValues);
		Set<String> tempValueSet = new HashSet<>();
		for (String s : valueSet) {
			tempValueSet.add(getDecryptValue(s, ""));
		}
		return tempValueSet;
	}

    private String getEncodeKey(String key) {
        return mIsKeyEncrypt ? MD5Utils.encode(key) : key;
    }

    private String getEncryptValue(Object value) {
        return mIsValueEncrypt ? DESUtils.encryptWithDES(String.valueOf(value), mEncryptKey) : String.valueOf(value);
    }

    private String getDecryptValue(String value, String defValue) {
        if (mIsValueEncrypt) {
            String resultValue = DESUtils.decryptWithDES(value, mEncryptKey);
            return resultValue != null ? resultValue : defValue;
        } else {
            return value;
        }
    }

    private SharedPreferences getSharedPreferences() {
        SharedPreferences preferences;
        if (TextUtils.isEmpty(mFileName)) {
            preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        } else {
            preferences = mContext.getSharedPreferences(mFileName, mMode);
        }
        return preferences;
    }

}
