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

package com.hellofyc.applib.media;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaFormat;

import com.hellofyc.applib.util.FLog;

import java.io.IOException;
import java.nio.ByteBuffer;

@TargetApi(16)
public class AvcEncoder {

	private static final String TAG = AvcEncoder.class.getSimpleName();
	
	private MediaCodec mCodec;
	private int mWidth;
	private int mHeight;
	
	private byte[] mYuv420;
	private byte[] mInfo;
	
	public AvcEncoder(int width, int height, int frameRate, int bitRate) {   
        
		mWidth  = width;  
		mHeight = height;  
		mYuv420 = new byte[width * height * 3 / 2];
      
        try {
			mCodec = MediaCodec.createEncoderByType("video/avc");
		} catch (IOException e) {
			FLog.e(e);
		}
        
        MediaFormat mediaFormat = MediaFormat.createVideoFormat("video/avc", width, height);
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, bitRate);
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, frameRate);
        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaToolBox.getColorFormat("video/avc")); 
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1); //关键帧间隔时间 单位s

        mCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        mCodec.start();
    }
	
	public void stopAndRelease() {
		if (mCodec != null) {
			mCodec.stop();
			mCodec.release();
			mCodec = null;
		}
	}
	
	@SuppressWarnings("deprecation")
	public int offerEncoder(byte[] input, byte[] output) {     
        int pos = 0;
        swapYV12toI420(input, mYuv420, mWidth, mHeight);
        try {
            ByteBuffer[] inputBuffers = mCodec.getInputBuffers();
            ByteBuffer[] outputBuffers = mCodec.getOutputBuffers();
            int inputBufferIndex = mCodec.dequeueInputBuffer(-1);
            if (inputBufferIndex >= 0) {
                ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];  
                inputBuffer.clear();
                inputBuffer.put(mYuv420);
                mCodec.queueInputBuffer(inputBufferIndex, 0, mYuv420.length, 0, 0);
            }
  
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int outputBufferIndex = mCodec.dequeueOutputBuffer(bufferInfo,0);
            
            while (outputBufferIndex >= 0) {
                ByteBuffer outputBuffer = outputBuffers[outputBufferIndex];
                byte[] outData = new byte[bufferInfo.size];
                outputBuffer.get(outData);
                  
                if(mInfo != null) {
                    System.arraycopy(outData, 0,  output, pos, outData.length);
                    pos += outData.length;
                } else {  //保存pps sps 只有开始时 第一个帧里有， 保存起来后面用  
                     ByteBuffer spsPpsBuffer = ByteBuffer.wrap(outData); 
                     if (spsPpsBuffer.getInt() == 0x00000001) {
                         mInfo = new byte[outData.length];
                         System.arraycopy(outData, 0, mInfo, 0, outData.length);
                     } else {
                         return -1;
                     }
                }
                mCodec.releaseOutputBuffer(outputBufferIndex, false);
                outputBufferIndex = mCodec.dequeueOutputBuffer(bufferInfo, 0);
            }
            
            if(output[4] == 0x65) { //key frame   编码器生成关键帧时只有 00 00 00 01 65 没有pps sps， 要加上  
                System.arraycopy(output, 0,  mYuv420, 0, pos);
                System.arraycopy(mInfo, 0,  output, 0, mInfo.length);
                System.arraycopy(mYuv420, 0,  output, mInfo.length, pos);
                pos += mInfo.length;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return pos;
    }
	
	private void swapYV12toI420(byte[] yv12bytes, byte[] i420bytes, int width, int height) {        
		System.arraycopy(yv12bytes, 0, i420bytes, 0, width * height);
		System.arraycopy(yv12bytes, width * height + width * height / 4, i420bytes, width * height, width * height / 4);
		System.arraycopy(yv12bytes, width * height, i420bytes, width * height + width * height / 4, width * height / 4);
	}
}
