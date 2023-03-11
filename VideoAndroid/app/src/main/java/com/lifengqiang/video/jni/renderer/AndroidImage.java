package com.lifengqiang.video.jni.renderer;

import android.media.Image;

import com.lifengqiang.video.jni.interfaces.NativeObject;

import java.nio.ByteBuffer;

public class AndroidImage extends NativeObject {
    public AndroidImage(Image image) {
        super();
        Image.Plane[] planes = image.getPlanes();
        setImageSize(image.getWidth(), image.getHeight());
        setPlanesBuffer(
                planes[0].getBuffer(),
                planes[1].getBuffer(),
                planes[2].getBuffer()
        );
        setPlanesPixelStride(planes[0].getPixelStride(), planes[1].getPixelStride(), planes[2].getPixelStride());
        setPlanesRowStride(planes[0].getRowStride(), planes[1].getRowStride(), planes[2].getRowStride());
        setTimestampNS(image.getTimestamp());
    }

    private native void setImageSize(int width, int height);

    private native void setPlanesBuffer(ByteBuffer y, ByteBuffer u, ByteBuffer v);

    private native void setPlanesPixelStride(int y, int u, int v);

    private native void setPlanesRowStride(int y, int u, int v);

    private native void setTimestampNS(long timestampNS);
}
