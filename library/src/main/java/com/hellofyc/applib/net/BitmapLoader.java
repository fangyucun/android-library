/*
 * Copyright (C) 2014 Jason Fang ( ijasonfang@gmail.com )
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hellofyc.applib.net;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.hellofyc.applib.util.FLog;
import com.hellofyc.applib.util.FileUtils;
import com.hellofyc.applib.util.ImageUtils;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 异步加载图片工具类
 * @author Fang Yucun
 *
 */
@SuppressLint("HandlerLeak")
public class BitmapLoader {
	private static final boolean DEBUG = false;
	
	private static final int LOAD_SUCCESS = 0;
	
	private ExecutorService mThreadPool = Executors.newFixedThreadPool(20);
	
	private HashMap<String, SoftReference<Bitmap>> mImageCacheMap;
	
	private File mImageCacheDir;
	
	private int mImageWidth;
	private int mImageHeight;
	
    public BitmapLoader(int width, int height) {
    	mImageWidth = width;
    	mImageHeight = height;
        mImageCacheMap = new HashMap<>();
        try {
			mImageCacheDir = FileUtils.getImageCacheDir();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public Bitmap loadBitmap(final ImageView imageView, final String imageUrl, 
    		final ImageCallBack imageCallBack) {
    	
    	final Handler handler = new Handler() {
        	
            @Override
            public void handleMessage(Message msg) {
            	switch (msg.what) {
            	case LOAD_SUCCESS:
            		imageCallBack.imageLoad(imageView, (Bitmap)msg.obj);
                	break;
            	}
            }
        };
        
    	try {
	        if(mImageCacheMap.containsKey(imageUrl) && mImageCacheMap.get(imageUrl).get() != null) {
	            return mImageCacheMap.get(imageUrl).get();
	        } else {
	            String bitmapName = FileUtils.getNameFromUrl(imageUrl);  
	            File[] cacheFiles = mImageCacheDir.listFiles();  
	            int i = 0;
	            for(; i<cacheFiles.length; i++) {  
	                if (bitmapName.equals(cacheFiles[i].getName())) {
	                    break;
	                }
	            }
	            if (i < cacheFiles.length) {
	            	Bitmap bitmap = ImageUtils.getBitmapFromFile(mImageCacheDir + "/" + bitmapName, mImageWidth, mImageHeight);
	            	mImageCacheMap.put(imageUrl, new SoftReference<Bitmap>(bitmap));
                	return bitmap;
	            }
	        }
    	} catch (Exception e) {
            e.printStackTrace();
        }
    	
    	loadBitmapFromUrl(handler, imageUrl);
    	
	    return null;
    }
    
    private void loadBitmapFromUrl(final Handler handler, final String imageUrl) {
    	
        Thread thread = new Thread() {
        	
            @Override  
            public void run() {
            	Bitmap bitmap;
                try {
	                bitmap = ImageUtils.getBitmapFromUrl(imageUrl, mImageWidth, mImageHeight);
                } catch (OutOfMemoryError e) {
        			if (DEBUG) FLog.e("OutOfMemoryError");
        			System.gc();
        			bitmap = ImageUtils.getBitmapFromUrl(imageUrl, mImageWidth, mImageHeight);
        		}
                Message msg = handler.obtainMessage(LOAD_SUCCESS);
                msg.obj = bitmap;
                msg.sendToTarget();
                
                mImageCacheMap.put(imageUrl, new SoftReference<>(bitmap));
                FileUtils.saveBitmapToFile(bitmap, mImageCacheDir, FileUtils.getNameFromUrl(imageUrl));
            }
        };
        mThreadPool.execute(thread);
    }
      
    /** 
     * 回调接口 
     * @author Fang Yucun 
     *
     */
    public interface ImageCallBack {
    	void imageLoad(ImageView imageView, Bitmap bitmap);
    }  
}
