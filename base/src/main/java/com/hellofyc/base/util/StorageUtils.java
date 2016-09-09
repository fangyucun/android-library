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
 */package com.hellofyc.base.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.os.EnvironmentCompat;

import java.io.File;
import java.io.IOException;

/**
 * 内存工具类
 * Create on 2013年11月26日
 *
 * @author Fang Yucun
 */
public final class StorageUtils {
	static final boolean DEBUG = false;
	
	/**
	 * 判断SD卡是否挂起
	 */
	public static boolean isExternalStorageAvailable(Context context) {
		return EnvironmentCompat.getStorageState(context.getExternalCacheDir()).equals(Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * 获取存储卡根目录
	 */
	public static File getExternalStorageRootDir() throws IOException {
		return Environment.getExternalStorageDirectory();
	}
	
	public static File getExternalStorageDCIMDir() throws IOException {
		return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
	}
	
	/**
	 * 获取根目录
	 */
	public static File getRootDir() {
		return Environment.getRootDirectory();
	}
	

	/**
	 * 获取存储卡可用空间
	 */
	public static long getExternalStorageAvailableSpace() {
        try {
            StatFs stat = new StatFs(getExternalStorageRootDir().getAbsolutePath());
            long avaliableSize;
            if (Build.VERSION.SDK_INT >= 18) {
            	avaliableSize = stat.getAvailableBlocksLong() * stat.getBlockSizeLong();
            } else {
            	avaliableSize = ((long)stat.getAvailableBlocks() * (long)stat.getBlockSize());
            }
            return avaliableSize;
        } catch (Exception e) {
			e.printStackTrace();
        }
        return 0;
    }
    
	public static long getExternalStorageTotalSize() {
		File dir = Environment.getExternalStorageDirectory();
		StatFs statFs = new StatFs(dir.getPath());
		long blockSize;
		long blockCount;
		if (Build.VERSION.SDK_INT >= 18) {
			blockSize = statFs.getBlockSizeLong();
			blockCount = statFs.getBlockCountLong();
		} else {
			blockSize = statFs.getBlockSize();
			blockCount = statFs.getBlockCount();
		}
		return blockSize * blockCount;
	}
    
    private StorageUtils(){/*Do not new me*/}
}
