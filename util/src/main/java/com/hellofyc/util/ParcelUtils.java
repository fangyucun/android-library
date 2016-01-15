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

package com.hellofyc.util;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Create on 2015年4月17日 上午10:47:02
 *
 * @author Yucun Fang
 */
public class ParcelUtils {
    private static final boolean DEBUG = false;

    public static <T extends  Parcelable> T getParcelalbeExtra(@NonNull Intent intent, String name) {
        try {
            if (intent.hasExtra(name)) {
                return intent.getParcelableExtra(name);
            }
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return null;
    }

    public static int getIntExtra(@NonNull Intent intent, @NonNull String name, int defValue) {
        try {
            if (intent.hasExtra(name)) {
                return intent.getIntExtra(name, defValue);
            }
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return defValue;
    }

    public static int[] getIntArrayExtra(@NonNull Intent intent, @NonNull String name) {
        try {
            if (intent.hasExtra(name)) {
                return intent.getIntArrayExtra(name);
            }
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return null;
    }

    public static ArrayList<Integer> getIntegerArrayListExtra(@NonNull Intent intent, @NonNull String name) {
        try {
            if (intent.hasExtra(name)) {
                return intent.getIntegerArrayListExtra(name);
            }
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return null;
    }

    public static long getLongExtra(@NonNull Intent intent, @NonNull String name, int defValue) {
        try {
            if (intent.hasExtra(name)) {
                return intent.getLongExtra(name, defValue);
            }
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return defValue;
    }

    public static long[] getLongArrayExtra(@NonNull Intent intent, @NonNull String name) {
        try {
            if (intent.hasExtra(name)) {
                return intent.getLongArrayExtra(name);
            }
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return null;
    }

    public static String getStringExtra(@NonNull Intent intent, @NonNull String name) {
        try {
            if (intent.hasExtra(name)) {
                return intent.getStringExtra(name);
            }
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return "";
    }

    public static ArrayList<String> getStringArrayListExtra(@NonNull Intent intent, @NonNull String name) {
        try {
            if (intent.hasExtra(name)) {
                return intent.getStringArrayListExtra(name);
            }
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return null;
    }

    public static String[] getStringArrayExtra(@NonNull Intent intent, @NonNull String name) {
        try {
            if (intent.hasExtra(name)) {
                return intent.getStringArrayExtra(name);
            }
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return null;
    }

    public static boolean getBooleanExtra(@NonNull Intent intent, @NonNull String name, boolean defaultValue) {
        try {
            if (intent.hasExtra(name)) {
                return intent.getBooleanExtra(name, defaultValue);
            }
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return defaultValue;
    }

    public static boolean[] getBooleanArrayExtra(@NonNull Intent intent, @NonNull String name) {
        try {
            if (intent.hasExtra(name)) {
                return intent.getBooleanArrayExtra(name);
            }
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return null;
    }

    public static float getFloatExtra(@NonNull Intent intent, @NonNull String name, float defaultValue) {
        try {
            if (intent.hasExtra(name)) {
                return intent.getFloatExtra(name, defaultValue);
            }
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return defaultValue;
    }

    public static float[] getFloatArrayExtra(@NonNull Intent intent, @NonNull String name) {
        try {
            if (intent.hasExtra(name)) {
                return intent.getFloatArrayExtra(name);
            }
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return null;
    }

    public static double getDoubleExtra(@NonNull Intent intent, @NonNull String name, double defaultValue) {
        try {
            if (intent.hasExtra(name)) {
                return intent.getDoubleExtra(name, defaultValue);
            }
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return defaultValue;
    }

    public static double[] getDoubleArrayExtra(@NonNull Intent intent, @NonNull String name) {
        try {
            if (intent.hasExtra(name)) {
                return intent.getDoubleArrayExtra(name);
            }
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return null;
    }

    public static Bundle getBundleExtra(@NonNull Intent intent, @NonNull String name) {
        try {
            if (intent.hasExtra(name)) {
                return intent.getBundleExtra(name);
            }
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return null;
    }

    public static byte getByteExtra(@NonNull Intent intent, @NonNull String name, byte defaultValue) {
        try {
            if (intent.hasExtra(name)) {
                return intent.getByteExtra(name, defaultValue);
            }
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return defaultValue;
    }

    public static byte[] getByteArrayExtra(@NonNull Intent intent, @NonNull String name) {
        try {
            if (intent.hasExtra(name)) {
                return intent.getByteArrayExtra(name);
            }
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return null;
    }

    public static char getCharExtra(@NonNull Intent intent, @NonNull String name, char defaultValue) {
        try {
            if (intent.hasExtra(name)) {
                return intent.getCharExtra(name, defaultValue);
            }
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return defaultValue;
    }

    public static char[] getCharArrayExtra(@NonNull Intent intent, @NonNull String name) {
        try {
            if (intent.hasExtra(name)) {
                return intent.getCharArrayExtra(name);
            }
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return null;
    }

    public static CharSequence getCharSequenceExtra(@NonNull Intent intent, @NonNull String name) {
        try {
            if (intent.hasExtra(name)) {
                return intent.getCharSequenceExtra(name);
            }
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return null;
    }

    public static CharSequence[] getCharSequenceArrayExtra(@NonNull Intent intent, @NonNull String name) {
        try {
            if (intent.hasExtra(name)) {
                return intent.getCharSequenceArrayExtra(name);
            }
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return null;
    }

    public static ArrayList<CharSequence> getCharSequenceArrayListExtra(@NonNull Intent intent, @NonNull String name) {
        try {
            if (intent.hasExtra(name)) {
                return intent.getCharSequenceArrayListExtra(name);
            }
        } catch (Exception e) {
            if (DEBUG) FLog.e(e);
        }
        return null;
    }

	public static <T> void createFromParcel(@NonNull Parcel parcel, @NonNull T t) {
		Class<?> cls = t.getClass();
		Field[] fields = cls.getDeclaredFields();
		for (Field f : fields) {
			f.setAccessible(true);
			setFieldValue(f, t, parcel);
		}
	}
	
	public static <T> void writeToParcel(@NonNull Parcel parcel, @NonNull T t) {
		Class<?> cls = t.getClass();
		Field[] fields = cls.getDeclaredFields();
		for (Field f : fields) {
			f.setAccessible(true);
			setParcelValue(f, t, parcel);
		}
	}
	
	public static void writeToParcel(@NonNull Parcel dest, @NonNull Object... args) {
		if (args.length == 0) return;
		
        for (Object obj : args) {
            dest.writeValue(obj);
        }
	}
	
	public static void setParcelValue(@NonNull Field f, @NonNull Object obj, @NonNull Parcel parcel) {
		try {
			if ("Boolean".equals(f.getType().getSimpleName())) {
				parcel.writeByte(f.getBoolean(obj) ? (byte)1 : 0);
			} else if ("Integer".equals(f.getType().getSimpleName())) {
				parcel.writeInt(f.getInt(obj));
			} else if ("Float".equals(f.getType().getSimpleName())) {
				parcel.writeFloat(f.getFloat(obj));
			} else if ("Double".equals(f.getType().getSimpleName())) {
				parcel.writeDouble(f.getDouble(obj));
			} else if ("Long".equals(f.getType().getSimpleName())) {
				parcel.writeLong(f.getLong(obj));
			} else if ("Short".equals(f.getType().getSimpleName())) {
				parcel.writeInt(f.getInt(obj));
			} else if ("Character".equals(f.getType().getSimpleName())) {
				throw new RuntimeException("not support!");
			} else if ("Byte".equals(f.getType().getSimpleName())) {
				parcel.writeByte(f.getByte(obj));
			}
		} catch (IllegalAccessException | IllegalArgumentException e) {
			FLog.e(e);
		}
	}
	
	public static void setFieldValue(@NonNull Field f, @NonNull Object obj, @NonNull Parcel parcel) {
		try {
			if ("Boolean".equals(f.getType().getSimpleName())) {
				f.setBoolean(obj, parcel.readByte() != 0);
			} else if ("Integer".equals(f.getType().getSimpleName())) {
				f.setInt(obj, parcel.readInt());
			} else if ("Float".equals(f.getType().getSimpleName())) {
				f.setFloat(obj, parcel.readFloat());
			} else if ("Double".equals(f.getType().getSimpleName())) {
				f.setDouble(obj, parcel.readDouble());
			} else if ("Long".equals(f.getType().getSimpleName())) {
				f.setLong(obj, parcel.readLong());
			} else if ("Short".equals(f.getType().getSimpleName())) {
				f.setInt(obj, parcel.readInt());
			} else if ("Character".equals(f.getType().getSimpleName())) {
				throw new RuntimeException("not support!");
			} else if ("Byte".equals(f.getType().getSimpleName())) {
				f.setByte(obj, parcel.readByte());
			}
		} catch (IllegalAccessException | IllegalArgumentException e) {
			FLog.e(e);
		}
	}
	
	public static Bundle[] getBundleArrayFromBundle(@NonNull Bundle bundle, @NonNull String key) {
        Parcelable[] array = bundle.getParcelableArray(key);
        if (array instanceof Bundle[] || array == null) {
            return (Bundle[]) array;
        }
        Bundle[] typedArray = Arrays.copyOf(array, array.length, Bundle[].class);
        bundle.putParcelableArray(key, typedArray);
        return typedArray;
    }
	
}
