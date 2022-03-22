package com.android.systemui.glwallpaper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
/* loaded from: classes.dex */
public final class ImageGLWallpaper {
    public int mAttrPosition;
    public int mAttrTextureCoordinates;
    public final ImageGLProgram mProgram;
    public final FloatBuffer mTextureBuffer;
    public int mTextureId;
    public int mUniTexture;
    public final FloatBuffer mVertexBuffer;
    public static final float[] VERTICES = {-1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f};
    public static final float[] TEXTURES = {0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};

    public ImageGLWallpaper(ImageGLProgram imageGLProgram) {
        this.mProgram = imageGLProgram;
        float[] fArr = VERTICES;
        FloatBuffer asFloatBuffer = ByteBuffer.allocateDirect(48).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mVertexBuffer = asFloatBuffer;
        asFloatBuffer.put(fArr);
        asFloatBuffer.position(0);
        float[] fArr2 = TEXTURES;
        FloatBuffer asFloatBuffer2 = ByteBuffer.allocateDirect(48).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mTextureBuffer = asFloatBuffer2;
        asFloatBuffer2.put(fArr2);
        asFloatBuffer2.position(0);
    }
}
