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

package com.hellofyc.base.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.GLException;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.IntBuffer;

/**
 * Image工具类
 * @author Fang Yucun
 * @since 2014年1月26日
 */
public final class BitmapUtils {
	private static final boolean DEBUG = false;

    private static final Canvas sCanvas = new Canvas();
	
	public static final ColorDrawable TRANSPARENT_COLOR_DRAWABLE = new ColorDrawable(Color.TRANSPARENT);

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static Bitmap blur(@NonNull Context context,
							  @NonNull Bitmap srcBitmap,
							  @FloatRange(from = 0f, to = 25f, fromInclusive = false) float radius) {
		Bitmap destBitmap = srcBitmap.copy(srcBitmap.getConfig(), true);
		RenderScript renderScript = RenderScript.create(context);
		Allocation inputAllocation = Allocation.createFromBitmap(
				renderScript, srcBitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
		Allocation outputAllocation = Allocation.createTyped(renderScript, inputAllocation.getType());
		ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
		script.setRadius(radius);
		script.setInput(inputAllocation);
		script.forEach(outputAllocation);
		outputAllocation.copyTo(destBitmap);
		return destBitmap;
	}

    public static Bitmap createBitmap(int width, int height, @NonNull Config config, int retryCount) {
        try {
            return Bitmap.createBitmap(width, height, config);
        } catch (OutOfMemoryError error) {
            if (retryCount > 0) {
                System.gc();
                return createBitmap(width, height, config, retryCount - 1);
            }
            return null;
        }
    }

    public static Bitmap createBitmapFromView(@NonNull View view) {
        if (view instanceof ImageView) {
            Drawable drawable = ((ImageView) view).getDrawable();
            if (drawable != null && drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }
        }
        view.clearFocus();
        Bitmap bitmap = createBitmap(view.getWidth(), view.getHeight(), Config.ARGB_8888, 1);
        if (bitmap != null) {
            synchronized (sCanvas) {
                Canvas canvas = sCanvas;
                canvas.setBitmap(bitmap);
                view.draw(canvas);
                canvas.setBitmap(null);
            }
        }
        return bitmap;
    }

	public static Bitmap getImageBitmap(ImageView image) {
		if (image == null) return null;
		image.setDrawingCacheEnabled(true);
		Bitmap imageCache = image.getDrawingCache();
		if (imageCache == null) return null;
		Bitmap bitmap = Bitmap.createBitmap(imageCache);
		image.setDrawingCacheEnabled(false);
		return bitmap;
	}
	
	public static void setImageInFade(ImageView image, Bitmap bitmap) {
		if (image == null || bitmap == null) return;
		
		setImageInFade(image, new BitmapDrawable(image.getResources(), bitmap));
	}
	
	public static void setImageInFade(ImageView image, Drawable drawable) {
		if (image == null || drawable == null) return;
		
		final TransitionDrawable td = new TransitionDrawable(new Drawable[]{
				TRANSPARENT_COLOR_DRAWABLE, 
				drawable
		});
		td.startTransition(500);
		image.setImageDrawable(td);
	}
	
	/**
	 * 设置头像变灰
	 */
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static void setImageGray(Context context, int resId) {
		Drawable drawable;
		if (Build.VERSION.SDK_INT >= 21) {
			drawable = context.getResources().getDrawable(resId, null);
		} else {
			drawable = context.getResources().getDrawable(resId);
		}
		if (drawable == null) return;
		drawable.mutate();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter filter = new ColorMatrixColorFilter(cm);
		drawable.setColorFilter(filter);
	}
	
	/**
	 * 创建Bitmap倒影
	 */
	public static Bitmap createReflectedImage(Bitmap originalImage) {
	    final int reflectionGap = 1;
	    int width = originalImage.getWidth();
	    int height = originalImage.getHeight();
	    Matrix matrix = new Matrix();
	    matrix.preScale(1, -1);
	    Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height / 2, width, height / 2, matrix, false);
	    Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmapWithReflection);
	    canvas.drawBitmap(originalImage, 0, 0, null);
	    Paint defaultPaint = new Paint();
	    canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);
	    canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
	    Paint paint = new Paint();
	    LinearGradient shader = new LinearGradient(
	    		0, originalImage.getHeight(), 
	    		0, bitmapWithReflection.getHeight() + reflectionGap, 
	    		0X70FFFFFF, 0X00FFFFFF, TileMode.MIRROR);
	    paint.setShader(shader);
	    paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
	    canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);
	    return bitmapWithReflection; 
	}
	
	/**
	 * Drawable to Bitmap
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		return ((BitmapDrawable)drawable).getBitmap();
	}
	
	/**
	 * Bitmap to Drawable
	 */
	public static Drawable bitmapToDrawable(Context context, Bitmap bitmap) {
		return new BitmapDrawable(context.getResources(), bitmap);
	}

    public static byte[] bitmapToBytes(Bitmap bitmap) {
        return bitmapToBytes(bitmap, Bitmap.CompressFormat.PNG, 100);
    }

    public static byte[] bitmapToBytes(Bitmap bitmap, Bitmap.CompressFormat format, int quality) {
        if (bitmap == null || format == null) return null;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(format, quality, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static Bitmap bytesToBitmap(byte[] bytes) {
        if (bytes == null || bytes.length == 0) return null;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
	
	/**
	 * 从资源文件进行解码
	 */
	public static Bitmap getBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);
	}
	
	/**
	 * 从文件获取Bitmap
	 */
	public static Bitmap getBitmapFromFile(String path, int reqWidth, int reqHeight){
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(path, options);
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(path, options);
	}
	
	/**
	 * URL获取Bitmap
	 */
    public static Bitmap getBitmapFromUrl(String urlString, int width, int height) {
		Bitmap bitmap = null;
		try {
			URL url = new URL(urlString);
			HttpURLConnection connnection = (HttpURLConnection) url.openConnection();
			InputStream is = connnection.getInputStream();
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
            opts.inSampleSize = calculateInSampleSize(opts, width, height);
            opts.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeStream(is, null, opts);
		} catch (Exception e) {
			if (DEBUG) FLog.e(e);
		}
		return bitmap;
	}
    
	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	        final int heightRatio = Math.round((float) height / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }
	    return inSampleSize;
	}
	
	/**
	 * 获取Video缩略图
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
	public static Bitmap getVideoThumbnail(Context context, Uri uri) {
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		retriever.setDataSource(context, uri);
		return retriever.getFrameAtTime(2000);
	}
	
	/**
	 * 回收Bitmap
	 * @param bitmap bitmap
	 */
	public static void recycleBitmap(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
		}
	}
	
	/**
	 * Must call in SurfaceView's subclass onDraw();
	 * @param x x
	 * @param y y
	 * @param w w
	 * @param h h
	 * @return bitmap
	 * @throws OutOfMemoryError
	 */
	public static Bitmap createBitmapFromGLSurface(int x, int y, int w, int h)
	        throws OutOfMemoryError {
		
	    int bitmapBuffer[] = new int[w * h];
	    int bitmapSource[] = new int[w * h];
	    IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
	    intBuffer.position(0);
	    try {
	        GLES20.glReadPixels(x, y, w, h, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, intBuffer);
	        int offset1, offset2;
	        for (int i = 0; i < h; i++) {
	            offset1 = i * w;
	            offset2 = (h - i - 1) * w;
	            for (int j = 0; j < w; j++) {
	                int texturePixel = bitmapBuffer[offset1 + j];
	                int blue = (texturePixel >> 16) & 0xff;
	                int red = (texturePixel << 16) & 0x00ff0000;
	                int pixel = (texturePixel & 0xff00ff00) | red | blue;
	                bitmapSource[offset2 + j] = pixel;
	            }
	        }
	        return Bitmap.createBitmap(bitmapSource, w, h, Config.ARGB_8888);
	    } catch (GLException e) {
	    	FLog.e(e);
	    }
	    return null;
	}
	
	public static Point getImageViewSize(ImageView image) {
		int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		image.measure(width, height);
		
		Point p = new Point();
		p.x = image.getMeasuredWidth();
		p.y = image.getMeasuredHeight();
		return p;
	}
	
	private BitmapUtils(){/*Do not new me*/}
}
