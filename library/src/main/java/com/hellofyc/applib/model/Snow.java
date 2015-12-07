package com.hellofyc.applib.model;

import android.graphics.Bitmap;
import android.support.v4.util.ArrayMap;

public class Snow {

	public float x;
	public float y;
	public int width;
	public int height;
	private float parentViewWidth = 0;
	public float rotation;
	public float speed;
	public float rotationSpeed;
	public Bitmap bitmap;
	static ArrayMap<Integer, Bitmap> bitmapMap = new ArrayMap<>();

	public static Snow createShow(float parentViewWidth, Bitmap showBitmap) {

		Snow snow = new Snow();
		snow.parentViewWidth = parentViewWidth;

		snow.width = (int) (5 + (float) Math.random() * 50);
		float hwRatio = showBitmap.getHeight() / showBitmap.getWidth();
		snow.height = (int) (snow.width * hwRatio);

		snow.x = (float) Math.random() * (parentViewWidth - snow.width);
		snow.y = 0 - (snow.height + (float) Math.random() * snow.height);

		snow.speed = 50 + (float) Math.random() * 150;
		snow.rotation = (float) Math.random() * 180 - 90;
		snow.rotationSpeed = (float) Math.random() * 90 - 45;

		snow.bitmap = bitmapMap.get(snow.height);
		if (snow.bitmap == null) {
			snow.bitmap = Bitmap.createScaledBitmap(showBitmap,
					snow.width, snow.height, true);
			bitmapMap.put(snow.width, snow.bitmap);
		}
		return snow;
	}

	public void resetX() {
		this.x = (float) Math.random() * (parentViewWidth - this.width);
	}

}
