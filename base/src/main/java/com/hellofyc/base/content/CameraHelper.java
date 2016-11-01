package com.hellofyc.base.content;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.hellofyc.base.utils.ParseUtils;

import java.io.File;

/**
 * Created on 2016/5/23.
 *
 * @author Yucun Fang
 */
public class CameraHelper {

    private static final int REQUEST_CODE_OPEN_CAMERA    = 1;
    private static final int REQUEST_CODE_OPEN_ALBUM     = 2;
    private static final int REQUEST_CODE_OPEN_CROP      = 3;
    private Activity mHost;
    private Uri mOutputUri;
    private Bundle mBundle;
    private OnCaptureListener mCaptureListener;
    private boolean mNeedCrop = false;
    private int mOutputX;
    private int mOutputY;
    private int mAspectX = 1;
    private int mAspectY = 1;
    private Format mOutputFormat = Format.JPEG;

    public enum Format {
        JPEG,
        PNG,
        WEBP
    }

    private CameraHelper(Activity activity) {
        mHost = activity;
        mOutputX = ParseUtils.dpToPx(activity, 180);
        mOutputY = ParseUtils.dpToPx(activity, 180);
        mOutputUri = Uri.fromFile(new File(activity.getExternalCacheDir(), "temp.jpg"));
    }

    public static CameraHelper newInstance(@NonNull Activity activity) {
        return new CameraHelper(activity);
    }

    public CameraHelper setOutput(@NonNull String outputPath) {
        mOutputUri = Uri.fromFile(new File(outputPath));
        return this;
    }

    public CameraHelper setOutput(@NonNull Uri uri) {
        mOutputUri = uri;
        return this;
    }

    public CameraHelper setOutput(@NonNull File file) {
        mOutputUri = Uri.fromFile(file);
        return this;
    }

    public CameraHelper setOutputSize(int x, int y) {
        mNeedCrop = true;
        mOutputX = x;
        mOutputY = y;
        return this;
    }

    public CameraHelper setOutputFormat(Format format) {
        mOutputFormat = format;
        return this;
    }

    public CameraHelper setAspect(int x, int y) {
        mAspectX = x;
        mAspectY = y;
        return this;
    }

    public CameraHelper setActivityOptionsBundle(Bundle bundle) {
        mBundle = bundle;
        return this;
    }

    public CameraHelper setNeedCrop(boolean crop) {
        mNeedCrop = crop;
        return this;
    }

    public void openCamera(OnCaptureListener listener) {
        mCaptureListener = listener;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputUri);
        mHost.startActivityForResult(intent, REQUEST_CODE_OPEN_CAMERA, mBundle);
    }

    public void openAlbum(OnCaptureListener listener) {
        mCaptureListener = listener;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputUri);
        mHost.startActivityForResult(intent, REQUEST_CODE_OPEN_ALBUM, mBundle);
    }

    private void openCropActivity(Uri fileUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(fileUri, "image/*");
        intent.putExtra("output", mOutputUri);
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", mAspectX);
        intent.putExtra("aspectY", mAspectY);
        intent.putExtra("scale", true);
        intent.putExtra("outputX", mOutputX);
        intent.putExtra("outputY", mOutputY);
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("outputFormat", mOutputFormat.name());
        mHost.startActivityForResult(intent, REQUEST_CODE_OPEN_CROP, mBundle);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_OPEN_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    if (mNeedCrop) {
                        openCropActivity(mOutputUri);
                    } else {
                        if (mCaptureListener != null) {
                            mCaptureListener.onCapture(mOutputUri);
                        }
                    }
                }
                break;
            case REQUEST_CODE_OPEN_ALBUM:
                if (resultCode == Activity.RESULT_OK) {
                    Uri imageUri = null;
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = mHost.getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();
                        imageUri = Uri.fromFile(new File(picturePath));
                    }
                    if (mNeedCrop) {
                        openCropActivity(imageUri);
                    } else {
                        if (mCaptureListener != null) {
                            mCaptureListener.onCapture(imageUri);
                        }
                    }
                }
                break;
            case REQUEST_CODE_OPEN_CROP:
                if (mCaptureListener != null) {
                    mCaptureListener.onCapture(mOutputUri);
                }
                break;
        }
    }

    public interface OnCaptureListener {
        void onCapture(Uri imageUri);
    }
}
