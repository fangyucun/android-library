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
 */package com.hellofyc.base.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;

/**
 * Create on 2013年11月28日
 *
 * @author Fang Yucun
 */
public final class ResUtils {
	private static final boolean DEBUG = false;
	
	private static final String ATTR			 = "attr";
	private static final String ANIM			 = "anim";
	private static final String STYLE			 = "style";
	private static final String DIMEN			 = "dimen";
	private static final String COLOR			 = "color";
	private static final String ID				 = "id";
	private static final String STRING			 = "string";
	private static final String LAYOUT			 = "layout";
	private static final String DRAWABLE		 = "drawable";
	
    public static int getAttrResId(@NonNull Context context, String resName) {
    	return getResId(context, resName, ATTR);
    }
    
    public static int getAnimResId(@NonNull Context context, String resName) {
    	return getResId(context, resName, ANIM);
    }
    
    public static int getStyleResId(@NonNull Context context, String resName) {
    	return getResId(context, resName, STYLE);
    }
    
    public static int getDimenResId(@NonNull Context context, String resName) {
    	return getResId(context, resName, DIMEN);
    }
    
    public static int getColorResId(@NonNull Context context, String resName) {
    	return getResId(context, resName, COLOR);
    }
    
    public static int getIdResId(@NonNull Context context, String resName) {
    	return getResId(context, resName, ID);
    }
    
    public static int getStringResId(@NonNull Context context, String resName) {
    	return getResId(context, resName, STRING);
    }
    
    public static int getLayoutResId(@NonNull Context context, String resName) {
    	return getResId(context, resName, LAYOUT);
    }
    
    public static int getDrawableResId(@NonNull Context context, String resName) {
    	return getResId(context, resName, DRAWABLE);
    }
    
    public static int getResId(@NonNull Context context, String resName, String defType) {
    	Resources resources = context.getResources();
    	int resId = resources.getIdentifier(resName, defType, context.getPackageName());
    	if (DEBUG) FLog.i(defType + " resId:" + resId);
    	if (resId == 0) {
    		FLog.e(defType + " \"" + resName + "\" have not found!");
    	}
    	return resId;
    }

	public static CharSequence getString(@NonNull Context context, @NonNull String packageName, @NonNull String resName) {
		PackageManager packageManager = context.getPackageManager();
		try {
			Resources resources = packageManager.getResourcesForApplication(packageName);
			int resId = resources.getIdentifier(packageName.concat("/").concat(resName), null, null);
			if(resId != 0) {
				return packageManager.getText(packageName, resId, null);
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

    public static Drawable getDrawableFromJar(@NonNull Context context, String resName) {
    	InputStream inStream = ResUtils.class.getResourceAsStream("/res/drawable-hdpi/" + resName);
    	URL url = ResUtils.class.getResource("/res/drawable-hdpi/" + resName);
    	if (DEBUG) FLog.i("file:" + url.getFile());
    	return new BitmapDrawable(context.getResources(), inStream);
    }

    /**
     * 复制Assets中的文件
     */
    public static boolean copyFileFromAssets(@NonNull Context context, String assetsFilePath, String targetDir) {

    	File targetFile = new File(targetDir, StringUtils.getFileNameFromPath(assetsFilePath));
    	if (!targetFile.exists()) {
    		try {
				if (!targetFile.createNewFile()) return false;
			} catch (IOException e) {
				FLog.e(e);
			}
    	}

    	byte[] buffer = new byte[8 * 1024];
        InputStream inputStream = null;
        FileOutputStream foStream = null;
        try {
            inputStream = context.getAssets().open(assetsFilePath);
            foStream = new FileOutputStream(targetFile);
            int count;
            while ((count = inputStream.read(buffer)) > 0) {
                foStream.write(buffer, 0, count);
            }
            return true;
        } catch (IOException e) {
        	if (DEBUG) FLog.e(e);
        } finally {
        	IoUtils.close(foStream, inputStream);
        }
        return false;
    }

    /**
     * 复制Assets中的目录
     */
    public static boolean copyDirFromAssets(@NonNull Context context, String assetsDir, String targetDir) {
    	File targetFile = new File(targetDir);
    	if (!targetFile.exists()) {
    		if (!targetFile.mkdirs()) {
                return false;
            }
    	}

    	try {
    		String[] fileNames = context.getAssets().list(assetsDir);
    		for (String fileName : fileNames) {
    			if (DEBUG) FLog.i("fileName:" + fileName);
    			String targetPath = assetsDir + File.separator + fileName;
    			copyFileFromAssets(context, targetPath, targetDir);
    		}
    		return true;
    	} catch (IOException e) {
    		FLog.e(e);
    	}
    	return false;
    }

    /**
     * 获取Assets中的字符
     */
    public static String getFileStringFromAssets(@NonNull Context context, String filePath) {
    	InputStreamReader inputReader = null;
    	BufferedReader bufferReader = null;
    	try {
    		inputReader = new InputStreamReader(context.getAssets().open(filePath));
            bufferReader = new BufferedReader(inputReader);
            String line;
            StringBuilder result = new StringBuilder();
            while((line = bufferReader.readLine()) != null) {
            	result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
        	FLog.e(e);
        } finally {
        	IoUtils.close(bufferReader, inputReader);
        }
    	return "";
    }

    public static String getStringFromAssets(@NonNull Context context, String filePath) {
        if (TextUtils.isEmpty(filePath)) return "";
        String path;
        if (filePath.startsWith("/")) {
            path = "assets" + filePath;
        } else {
            path = "assets/" + filePath;
        }
        InputStream inputStream = context.getClassLoader().getResourceAsStream(path);
        return IoUtils.getStringFromStream(inputStream);
    }

    /**
     * 获取Assets文件路径
     */
    public static String getAssetsResPath(String fileName) {
    	return "file:///android_asset/" + fileName;
    }

    public static Uri getResIdUri(@NonNull Context context, int resId) {
    	return Uri.parse("res://" + context.getPackageName() + "/" + resId);
    }

    public static InputStream getAssetFile(@NonNull Context context, String path) {
    	AssetManager manager = context.getAssets();
    	try {
			return manager.open(path);
		} catch (IOException e) {
			FLog.e(e);
		}
    	return null;
    }

    public static boolean copyFileFromRaw(@NonNull Context context, int rawId, String targetDir) {
    	return copyFileFromRaw(context, rawId, targetDir, null);
    }

    /**
     * 复制Raw中的文件
     */
    public static boolean copyFileFromRaw(@NonNull Context context, int rawId, String targetDir, String extension) {

    	File targetFile;
    	Resources res = context.getResources();
    	if (TextUtils.isEmpty(extension)) {
    		targetFile = new File(targetDir, res.getResourceEntryName(rawId));
    	} else {
    		targetFile = new File(targetDir, res.getResourceEntryName(rawId) + "." + extension);
    	}

    	InputStream inputStream = null;
    	FileOutputStream foStream = null;

    	if (!targetFile.exists()) {
    		try {
				if (!targetFile.createNewFile()) {
                    return false;
                }
			} catch (IOException e) {
				FLog.e(e);
			}
    	}

    	try {
    		inputStream = res.openRawResource(rawId);
    		foStream = new FileOutputStream(targetFile);
    		byte[] buffer = new byte[8 * 1024];
            int count;
            while ((count = inputStream.read(buffer)) > 0) {
            	foStream.write(buffer, 0, count);
            }
    		return true;
    	} catch (Exception e) {
    		FLog.e(e);
    	} finally {
    		IoUtils.close(inputStream, foStream);
    	}
    	return false;
    }
    
    /**
     * 获取Raw中的字符
     */
    public static String getFileStringFromRaw(@NonNull Context context, int resId) {
		try { 
		    InputStreamReader isReader = new InputStreamReader(context.getResources().openRawResource(resId));
		    BufferedReader buffferReader = new BufferedReader(isReader);
		    String line;
		    StringBuilder result = new StringBuilder();
		    while((line = buffferReader.readLine()) != null) {
            	result.append(line);
            }
            return result.toString();
		} catch (Exception e) {
		    FLog.e(e);
		}
		return null;
    }
    
    /**
     * 获取Raw目录下文件的Uri
     */
    public static Uri getUriFromRaw(@NonNull Context context, int rawId) {
    	return Uri.parse("android.resource://" + context.getPackageName() + "/" + rawId);
    }
    
	public static Resources getApkResources(Context context, String apkPath) {
		File file = new File(apkPath);
		String packageParser = "android.content.pm.PackageParser";
		String assetManager = "android.content.res.AssetManager";
		try {
			Class<?> parserClass = Class.forName(packageParser);
			Class<?>[] typeArgs = { String.class };
			Constructor<?> parserConstructor = parserClass.getConstructor(typeArgs);
			Object[] valueArgs = { file.getAbsolutePath() };
			Object parser = parserConstructor.newInstance(valueArgs);

			DisplayMetrics metrics = new DisplayMetrics();
			metrics.setToDefaults();
			typeArgs = new Class<?>[] { File.class, String.class, DisplayMetrics.class, int.class };
			Method parserMethod = parserClass.getDeclaredMethod("parsePackage", typeArgs);
			valueArgs = new Object[] { file, file.getAbsolutePath(), metrics, 0 };

			Object parserObj = parserMethod.invoke(parser, valueArgs);

			if (parserObj == null) {
				return null;
			}
			Field appInfoField = parserObj.getClass().getDeclaredField("applicationInfo");

			if (appInfoField.get(parserObj) == null) {
				return null;
			}
			Class<?> assetMagCls = Class.forName(assetManager);
			Object assetMag = assetMagCls.newInstance();
			// 从assetMagCls类得到addAssetPath方法
			typeArgs = new Class[1];
			typeArgs[0] = String.class;
			Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod("addAssetPath", typeArgs);
			valueArgs = new Object[1];
			valueArgs[0] = file.getAbsolutePath();
			// 执行assetMag_addAssetPathMtd方法
			assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);
			// 得到Resources对象并实例化,有参数
			Resources res = context.getResources();
			typeArgs = new Class[3];
			typeArgs[0] = assetMag.getClass();
			typeArgs[1] = res.getDisplayMetrics().getClass();
			typeArgs[2] = res.getConfiguration().getClass();
			Constructor<Resources> resCt = Resources.class.getConstructor(typeArgs);
			valueArgs = new Object[3];
			valueArgs[0] = assetMag;
			valueArgs[1] = res.getDisplayMetrics();
			valueArgs[2] = res.getConfiguration();
			res = resCt.newInstance(valueArgs);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
    private ResUtils() {/*Do not new me*/}
}
